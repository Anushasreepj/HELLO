package de.hdm_stuttgart.mi.gameoflife.core.engine;
import de.hdm_stuttgart.mi.gameoflife.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamEngine implements IEngine {
    private static final Logger logger = LogManager.getLogger(StreamEngine.class);

    private IGrid gameGrid;
    private ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> timer;

    private boolean calculateParallel;
    private boolean isRunning = false;
    private HashSet<FutureCellState> changes = new HashSet<FutureCellState>();
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

    public void startCalculation(Runnable onSuccess, SimulationSettings settings) {
        isRunning = true;
        startInterval(settings.getMsPerTick(), () -> {
            nextGeneration();

            // Notify caller that calculation step was successful
            onSuccess.run();
        });

        this.settings = settings;
        calculateParallel = settings.getParallelCalculations();
    }


    public void loadSettings(SimulationSettings settings) {
        calculateParallel = settings.getParallelCalculations();
    }


    public void stopCalculation() {
        isRunning = false;
        stopInterval();
        logger.trace("Stopped automated Calculation");
    }

    public boolean isRunning(){
        return isRunning;
    }


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

                    if(settings.isInBounds(cell)) changes.add(new FutureCellState(cell, newState));

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

            List<FutureCellState> futureCellStates =
            deadCellsToCheck.entrySet().stream().filter(cellByteEntry -> cellByteEntry.getValue()==3)
                    .map(cellByteEntry -> new FutureCellState(cellByteEntry.getKey(), true, true)).collect(Collectors.toList());


            //2. Check for deaths
            Stream<Cell> aliveCellStream = Arrays.stream(gameGrid.getAliveCells());

            if (calculateParallel){
                aliveCellStream = aliveCellStream.parallel();
            }

            futureCellStates.addAll(aliveCellStream
                    .map(cell ->
                            {
                                FutureCellState futureCellState = new FutureCellState(cell,true);

                                byte aliveNeighbourCount = (byte) cell.getNeighbours().stream().filter(neighbourCell -> gameGrid.getState(neighbourCell)).count();

                                if(aliveNeighbourCount<2 | aliveNeighbourCount>3) futureCellState.setAlive(false);
                                return futureCellState;

                            }
                    ).collect(Collectors.toList()));


            //3. Apply Changes
            synchronized (changes){
                futureCellStates.stream().filter(futureCellState -> futureCellState.isChanged() || settings.isUninitialized()).forEach(futureCellState -> {
                    if(settings.isInBounds(futureCellState.getCell())) changes.add(futureCellState);

                    gameGrid.setState(futureCellState.getCell(), futureCellState.isAlive());
                });

            }
            settings.setUninitialized(false);
            logger.trace("Finished calculating next generation");
        }
    }


    public void loadGrid(IGrid grid) {
        stopCalculation();

        if(grid == null)
            throw new IllegalArgumentException("Grid cannot be null.");

        synchronized(gameGrid){
            gameGrid = grid;
        }
        logger.trace("Replaced game grid");
    }


    public FutureCellState[] getChanges() {
        synchronized (changes){
            FutureCellState[] returnValue = changes.toArray(new FutureCellState[0]);
            changes.clear();
            return returnValue;
        }
    }

    public void scheduleCellFlip(Cell cell) {
        synchronized (scheduledChanges){
            scheduledChanges.add(cell);
        }

        if(!isRunning){ //Directly place on UI side, handle simulation in the next calculated generation.
            synchronized (gameGrid){
                boolean newState = !gameGrid.getState(cell);
                if(settings.isInBounds(cell)) changes.add(new FutureCellState(cell, newState));
            }
        }
    }


    /**
     * Starts or Restarts the Interval
     * @param speed
     */
    private void  startInterval(long speed, Runnable timerTask){
        timer = ses.scheduleAtFixedRate(timerTask,0, speed, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the Interval
     */
    private void stopInterval(){
        if (timer != null) {
            timer.cancel(false);
        }
    }
}
