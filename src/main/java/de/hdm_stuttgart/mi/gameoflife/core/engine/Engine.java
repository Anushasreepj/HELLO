package de.hdm_stuttgart.mi.gameoflife.core.engine;

import de.hdm_stuttgart.mi.gameoflife.core.*;

import java.util.*;
import java.util.concurrent.*;

public class Engine implements IEngine {

    private IGrid gameGrid = new Grid();

    private boolean calculateParallel;
    private boolean isRunning = false;

    private EngineTimer engineTimer = new EngineTimer();

    private HashMap<Cell, CellStateChange> changes = new HashMap<Cell, CellStateChange>();

    private HashSet<Cell> scheduledChanges = new HashSet<Cell>();

    private SimulationSettings settings = new SimulationSettings();

    /**
     * Generates a Engine with a prefilled grid
     * @param grid The grid to use
     */
    public Engine(IGrid grid){
        gameGrid = grid;
    }

    /**
     * Generates a Engine with a new clean grid
     */
    public Engine(){
        gameGrid = new Grid();
    }

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

    public void loadSettings(SimulationSettings settings) {
        calculateParallel = settings.getParallelCalculations();
    }


    public void stopCalculation() {
        engineTimer.stopInterval();
        isRunning = false;
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void nextGeneration() {
        synchronized (gameGrid){ //locks gamegrid to let loadGamegrid() wait until this generation is completed


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



            ExecutorService es = Executors.newCachedThreadPool();

            HashMap<Cell,Byte> deadCellsToCheck = new HashMap<Cell,Byte>();
            HashSet<CellStateChange> cellStateChanges = new HashSet<CellStateChange>();

            //Iterate over alive cells
            for (Cell cell: gameGrid.getAliveCells()) {

                //Iterate over the neighbours of the cell
                // => Dead cells next to living ones need to be recalculated too.
                for (Cell neighbourCell:cell.getNeighbours()){

                    //Check if the neighbourCell is alive
                    if(!gameGrid.getState(neighbourCell)) {

                        //Check if the dead cell already is in the deadCellsToCheck map
                        if(deadCellsToCheck.containsKey(neighbourCell)){
                            //If it is, increase it's counter of living neighbours by one
                            deadCellsToCheck.put(neighbourCell, (byte) (deadCellsToCheck.get(neighbourCell)+1));
                        } else {
                            //It it's not, add it with a living neighbour count of one
                            deadCellsToCheck.put(neighbourCell, (byte) 1);
                        }
                    }
                }


                StateCalculatorRunnable stateCalculator = new StateCalculatorRunnable(cell,gameGrid);
                cellStateChanges.add(stateCalculator.cellStateChange);

                if(calculateParallel){
                    es.execute(stateCalculator);
                } else {
                    stateCalculator.run();
                }
            }


            es.shutdown(); //Stop new tasks being submitted

            //Iterate over all dead cells
            for(Map.Entry<Cell,Byte> entry: deadCellsToCheck.entrySet()){
                if(entry.getValue()==3){
                    cellStateChanges.add(new CellStateChange(entry.getKey(), true, true));
                }
            }

            //Wait for all processes to finish
            try {
                es.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //TODO: Log Error
            }

            // Execute changes.

            synchronized (changes){
                for (CellStateChange state: cellStateChanges) {
                    if(state.isChanged() || settings.isUninitialized()){
                        if(settings.isInBounds(state.getCell())){
                            if(changes.containsKey(state.getCell())){
                                changes.remove(state.getCell());
                            }
                            changes.put(state.getCell(), state);
                        }

                        gameGrid.setState(state.getCell(), state.isAlive());
                    }
                }
            }
            settings.setUninitialized(false);
        }
    }

    
    public void loadGrid(IGrid grid) {
        stopCalculation();

        if(grid == null)
            throw new IllegalArgumentException("Grid cannot be null.");

        //Don't replace grid if calculations are ongoing => synchronized
        synchronized(gameGrid){
            gameGrid = grid;
        }

        //startCalculation(); ??

    }


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


    public CellStateChange[] getChanges() {
        synchronized (changes){
            CellStateChange[] returnValue = changes.values().toArray(new CellStateChange[0]);
            changes.clear();
            return returnValue;
        }
    }

}





