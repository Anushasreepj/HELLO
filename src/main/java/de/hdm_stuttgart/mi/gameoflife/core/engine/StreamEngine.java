package de.hdm_stuttgart.mi.gameoflife.core.engine;
import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.Grid;
import de.hdm_stuttgart.mi.gameoflife.core.IEngine;
import de.hdm_stuttgart.mi.gameoflife.core.IGrid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StreamEngine implements IEngine {
    private static final Logger logger = LogManager.getLogger(StreamEngine.class);

    private IGrid gameGrid;
    private ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> timer;

    private static final int msPerTick = 50; //Placeholder for reaching 20tps. TODO: Use Settings Object/Config to Control.

    private Runnable timerTask = () -> nextGeneration();

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

    public void startCalculation() {
        startInterval(msPerTick);
        logger.trace("Started automated Calculation");
    }


    public void stopCalculation() {
        stopInterval();
        logger.trace("Stopped automated Calculation");
    }


    public void nextGeneration() {
        synchronized (gameGrid){

            logger.trace("Started calculating next generation");

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
            futureCellStates.addAll(Arrays.stream(gameGrid.getAliveCells())
                    .parallel()
                    .map(cell ->
            {
                FutureCellState futureCellState = new FutureCellState(cell,true);

                byte aliveNeighbourCount = (byte) cell.getNeighbours().stream().filter(neighbourCell -> gameGrid.getState(neighbourCell)).count();

                if(aliveNeighbourCount<2 | aliveNeighbourCount>3) futureCellState.setAlive(false);
                return futureCellState;

            }
            ).collect(Collectors.toList()));


            //3. Apply Changes
            futureCellStates.stream().filter(futureCellState -> futureCellState.isChanged()).forEach(futureCellState -> {
                gameGrid.setState(futureCellState.getCell(), futureCellState.isAlive());
            });

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


    /**
     * Starts or Restarts the Interval
     * @param speed
     */
    private void startInterval(long speed){
        timer = ses.scheduleAtFixedRate(timerTask,0, speed, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the Interval
     */
    private void stopInterval(){
        timer.cancel(false);
    }
}
