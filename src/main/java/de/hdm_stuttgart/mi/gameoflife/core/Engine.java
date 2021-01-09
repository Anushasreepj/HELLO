package de.hdm_stuttgart.mi.gameoflife.core;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Engine implements IEngine{

    private IGrid gameGrid = new Grid();
    private Timer timer = new Timer();

    private Stack<FutureCellState> changes = new Stack<FutureCellState>();

    private static final int msPerTick = 50; //Placeholder for reaching 20tps. TODO: Use Settings Object to Control.



    public Engine(IGrid grid){
        gameGrid = grid;
    }

    public Engine(){
        gameGrid = new Grid();
    }


    @Override
    public void startCalculation() {
        startInterval(msPerTick);
    }

    @Override
    public void stopCalculation() {
        stopInterval();
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            nextGeneration();
        }
    };

    @Override
    public void nextGeneration() {
        synchronized (gameGrid){ //locks gamegrid. Will still be used in calculations, but only to get, without setters.

            //Highly W.I.P. Solution, will reiterate after presentation from felix' team.

            ExecutorService es = Executors.newCachedThreadPool();

            HashMap<Cell,Byte> deadCellsToCheck = new HashMap<Cell,Byte>();
            HashSet<FutureCellState> futureCellStates = new HashSet<FutureCellState>();

            //Get all neighbour-Cells that are dead and start processes for alive cells
            for (Cell cell: gameGrid.getAliveCells()) {
                for (Cell neighbourCell:cell.getNeighbours()){
                    if(!gameGrid.getState(neighbourCell)) {
                        if(deadCellsToCheck.containsKey(neighbourCell)){
                            deadCellsToCheck.put(neighbourCell, (byte) (deadCellsToCheck.get(neighbourCell)+1));
                        } else {
                            deadCellsToCheck.put(neighbourCell, (byte) 1);
                        }
                    }
                }

                StateCalculatorRunnable stateCalculator = new StateCalculatorRunnable(cell,gameGrid);
                futureCellStates.add(stateCalculator.futureCellState);

                es.execute(stateCalculator);
            }

            for(Map.Entry<Cell,Byte> entry: deadCellsToCheck.entrySet()){
                if(entry.getValue()==3){
                    futureCellStates.add(new FutureCellState(entry.getKey(), true, true));
                }
            }

            es.shutdown(); //Stop new tasks being submitted

            try {
                es.awaitTermination(msPerTick, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                //TODO: Log Error
            }

            //TODO: Execute changes.

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

        //Won't replace grid if calculations are ongoing

        synchronized(gameGrid){
            gameGrid = grid;
        }

        //startCalculation(); ??

    }


    /**
     * Starts or Restarts the Interval
     * @param speed
     */
    private void startInterval(long speed){
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0, speed);
    }

    private void stopInterval(){
        timer.cancel();
    }


}


//Internal classes to add abstraction layer for State Calculation. Only uses get from the grid to stay thread safe.
class StateCalculatorRunnable implements Runnable{
    public final FutureCellState futureCellState;
    private final IGrid grid;

    public StateCalculatorRunnable(Cell cell, IGrid grid) {
        this.grid = grid;
        this.futureCellState = new FutureCellState(cell, true);
    }

    /*
      Rules:
      1. Alive + 2/3 alive neighbours => Stays Alive
      2. Else: Dies.
    */
    @Override
    public void run() {
        List<Cell> neighbours = futureCellState.getCell().getNeighbours();
        byte aliveNeighbours = 0;

        for (Cell cell: neighbours) {
            if(grid.getState(cell)){
                aliveNeighbours++;
                if(aliveNeighbours>3){
                    //Cell will die.
                    futureCellState.setAlive(false);
                    return;
                }
            }
        }

        if(aliveNeighbours<2){
            //Cell will die
            futureCellState.setAlive(false);
        }
    }
}


/**
 * A class representing the state of a cell for the coming generation
 */
class FutureCellState {
    private Cell cell;
    private boolean alive = true;
    private boolean changed = false;

    public FutureCellState(Cell cell, boolean isAlive){
        this.cell = cell;
        this.alive = isAlive;
    }

    public FutureCellState(Cell cell, boolean isAlive, boolean changed){
        this.cell = cell;
        this.alive = isAlive;
        this.changed = changed;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        changed = true;
    }
}