package de.hdm_stuttgart.mi.gameoflife.core.engine;
import de.hdm_stuttgart.mi.gameoflife.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamEngine implements IEngine {
    private static final Logger logger = LogManager.getLogger(StreamEngine.class);

    private IGrid gameGrid;
    private EngineTimer engineTimer = new EngineTimer();
    private boolean calculateParallel;
    private boolean isRunning = false;
    private HashMap<Cell, CellStateChange> changes = new HashMap<Cell, CellStateChange>();
    private HashSet<Cell> scheduledChanges = new HashSet<Cell>();

    private SimulationSettings settings = new SimulationSettings();

    /**
     * Generates a Engine with a prefilled grid
     * @param grid The grid to use
     */
    public StreamEngine(IGrid grid){
        gameGrid = grid;
    }

    /**
     * Generates a Engine with a new clean grid
     */
    public StreamEngine(){
        gameGrid = new Grid();
    }

    /**
     * Starts calculation
     * @param onSuccess Runnable to be executed on success
     * @param settings The settings object
     */
    public void startCalculation(Runnable onSuccess, SimulationSettings settings) {
        isRunning = true;
        engineTimer.startInterval(settings.getMsPerTick(), () -> {
            nextGeneration();

            // Notify caller that calculation step was successful
            onSuccess.run();
        });

        this.settings = settings;
        calculateParallel = settings.getParallelCalculations();
    }

    /**
     * Loads in the settings for the engine.
     * @param settings
     */
    public void loadSettings(SimulationSettings settings) {
        calculateParallel = settings.getParallelCalculations();
    }

    /**
     * Will stop automatic calculation.
     * Won't do anything if none is running at all.
     */
    public void stopCalculation() {
        isRunning = false;
        engineTimer.stopInterval();
        logger.trace("Stopped automated Calculation");
    }

    /**
     *
     * @return Is this engine running right now?
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     * Calculates one single next generation.
     */
    public void nextGeneration() {
        synchronized (gameGrid){

            logger.trace("Started calculating next generation");

            Cell[] states;
            synchronized (scheduledChanges){ //Thread Safe Copy to working array followed by clear.
                states = scheduledChanges.toArray(new Cell[0]);
                scheduledChanges.clear();
            }

            synchronized (changes){
                Arrays.stream(states).forEach(cell -> {
                    boolean newState = !gameGrid.getState(cell);

                    if(settings.isInBounds(cell)) {
                        if(changes.containsKey(cell)){
                            changes.remove(cell);
                        }
                        changes.put(cell, new CellStateChange(cell, newState));
                    }

                    gameGrid.setState(cell, newState);
                });
            }



            //1. Check for births
            HashMap<Cell,Integer> deadCellsToCheck = new HashMap<Cell,Integer>();

            Arrays.stream(gameGrid.getAliveCells()).forEach(cell -> {
                cell.getNeighbours().stream()
                        .filter(neighbourCell -> !gameGrid.getState(neighbourCell)) //Only get dead cells
                        .forEach(neighbourCell -> {
                            if(deadCellsToCheck.containsKey(neighbourCell))
                                deadCellsToCheck.put(neighbourCell, deadCellsToCheck.get(neighbourCell)+1); //Add one to neighbour count
                            else
                                deadCellsToCheck.put(neighbourCell, 1); //Introduce neighbour count as 1
                });
            });

            List<CellStateChange> cellStateChanges =
            deadCellsToCheck.entrySet().stream().filter(cellByteEntry -> cellByteEntry.getValue()==3)
                    .map(cellByteEntry -> new CellStateChange(cellByteEntry.getKey(), true, true)).collect(Collectors.toList());


            //2. Check for deaths
            Stream<Cell> aliveCellStream = Arrays.stream(gameGrid.getAliveCells());

            if (calculateParallel){
                aliveCellStream = aliveCellStream.parallel();
            }

            cellStateChanges.addAll(aliveCellStream
                    .map(cell ->
                            {
                                CellStateChange cellStateChange = new CellStateChange(cell,true);

                                byte aliveNeighbourCount = (byte) cell.getNeighbours().stream().filter(neighbourCell -> gameGrid.getState(neighbourCell)).count();

                                if(aliveNeighbourCount<2 | aliveNeighbourCount>3) cellStateChange.setAlive(false);
                                return cellStateChange;

                            }
                    ).collect(Collectors.toList()));


            //3. Apply Changes
            synchronized (changes){
                cellStateChanges.stream().filter(futureCellState -> futureCellState.isChanged() || settings.isUninitialized()).forEach(state -> {
                    if(settings.isInBounds(state.getCell())){
                        if(changes.containsKey(state.getCell())){
                            changes.remove(state.getCell());
                        }
                        changes.put(state.getCell(), state);
                    }

                    gameGrid.setState(state.getCell(), state.isAlive());

                    gameGrid.setState(state.getCell(), state.isAlive());
                });

            }
            settings.setUninitialized(false);
            logger.trace("Finished calculating next generation");
        }
    }

    /**
     * Used to stop calculations and replace the internal grid with another one.
     * @param grid The new grid to use
     */
    public void loadGrid(IGrid grid) {
        stopCalculation();

        if(grid == null)
            throw new IllegalArgumentException("Grid cannot be null.");

        synchronized(gameGrid){
            gameGrid = grid;
        }
        logger.trace("Replaced game grid");
    }

    /**
     * Get's and clears the internal changes buffer
     * @return changes since last got changes
     */
    public CellStateChange[] getChanges() {
        synchronized (changes){
            CellStateChange[] returnValue = changes.values().toArray(new CellStateChange[0]);
            changes.clear();
            return returnValue;
        }
    }

    /**
     * Will flip this cell before calculating the next generation
     * @param cell Cell to flip
     */
    public void scheduleCellFlip(Cell cell) {
        synchronized (scheduledChanges){
            scheduledChanges.add(cell);
        }

        if(!isRunning){ //Directly place on UI side, handle simulation in the next calculated generation.
            synchronized (changes){
                boolean newState = !gameGrid.getState(cell);
                if(settings.isInBounds(cell)) {
                    if(changes.containsKey(cell)){
                        changes.remove(cell);
                    }
                    changes.put(cell, new CellStateChange(cell, newState));
                }
            }
        }
    }
}
