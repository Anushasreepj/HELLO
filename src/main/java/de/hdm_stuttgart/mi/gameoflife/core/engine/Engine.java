package de.hdm_stuttgart.mi.gameoflife.core.engine;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.Grid;
import de.hdm_stuttgart.mi.gameoflife.core.IEngine;
import de.hdm_stuttgart.mi.gameoflife.core.IGrid;

import java.util.*;
import java.util.concurrent.*;

public class Engine implements IEngine {

    private IGrid gameGrid = new Grid();
    private ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> timer;

    private Stack<FutureCellState> changes = new Stack<FutureCellState>();

    private static final int msPerTick = 50; //Placeholder for reaching 20tps. TODO: Use Settings Object/Config to Control.


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

    public void startCalculation(Runnable onSuccess) {
        startInterval(msPerTick, () -> {
            nextGeneration();

            // Notify caller that calculation step was successful
            onSuccess.run();
        });
    }

    public void stopCalculation() {
        stopInterval();
    }

    public void nextGeneration() {
        synchronized (gameGrid){ //locks gamegrid to let loadGamegrid() wait until this generation is completed

            ExecutorService es = Executors.newCachedThreadPool();

            HashMap<Cell,Byte> deadCellsToCheck = new HashMap<Cell,Byte>();
            HashSet<FutureCellState> futureCellStates = new HashSet<FutureCellState>();

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
                futureCellStates.add(stateCalculator.futureCellState);

                es.execute(stateCalculator);
            }


            es.shutdown(); //Stop new tasks being submitted

            //Iterate over all dead cells
            for(Map.Entry<Cell,Byte> entry: deadCellsToCheck.entrySet()){
                if(entry.getValue()==3){
                    futureCellStates.add(new FutureCellState(entry.getKey(), true, true));
                }
            }

            //Wait for all processes to finish
            try {
                es.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                //TODO: Log Error
            }

            // Execute changes.

            for (FutureCellState state: futureCellStates) {
                if(state.isChanged()){
                    changes.add(state);
                    gameGrid.setState(state.getCell(), state.isAlive());
                }
            }
        }
    }

    @Override
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





