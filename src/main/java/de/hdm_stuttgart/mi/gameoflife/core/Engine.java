package de.hdm_stuttgart.mi.gameoflife.core;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Engine implements IEngine{

    private IGrid gameGrid = new Grid();
    private Timer timer = new Timer();

    private Stack<CellState> changes = new Stack<CellState>();

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

            HashSet<Cell> deadCellsToCheck = new HashSet<Cell>();
            HashSet<CellState> cellStates = new HashSet<CellState>();

            //Get all neighbour-Cells that are dead and start processes for alive cells
            for (Cell cell: gameGrid.getAliveCells()) {


                for (Cell neighbourCell:cell.getNeighbours()){
                    if(!gameGrid.getState(neighbourCell)) deadCellsToCheck.add(neighbourCell);
                }

                StateCalculatorRunnable stateCalculator = new StateCalculatorRunnableAlive(cell,gameGrid);
                cellStates.add(stateCalculator.cellState);

                es.execute(stateCalculator);
            }

            for(Cell cell: deadCellsToCheck){
                StateCalculatorRunnable stateCalculator = new StateCalculatorRunnableDead(cell,gameGrid);
                cellStates.add(stateCalculator.cellState);

                es.execute(stateCalculator);
            }

            es.shutdown(); //Stop new tasks being submitted

            try {
                es.awaitTermination(msPerTick, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                //TODO: Log Error
            }

            //TODO: Execute changes.

            for (CellState state:cellStates) {
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

abstract class StateCalculatorRunnable implements Runnable{
    public final CellState cellState;
    protected final IGrid grid;

    protected StateCalculatorRunnable(Cell cell, boolean isAlive, IGrid grid) {
        this.grid = grid;
        this.cellState = new CellState(cell, isAlive);
    }
}

/**
 * Target: Only needs to calculate if neighbour-count is >1 and <4 (will stop checking on 4th alive cell found)
 */
class StateCalculatorRunnableAlive extends StateCalculatorRunnable{

    public StateCalculatorRunnableAlive(Cell cell, IGrid grid) {
        super(cell,true, grid);
    }

    /*
      Rules:
      1. Alive + 2/3 alive neighbours => Stays Alive
      2. Else: Dies.
    */
    @Override
    public void run() {
        List<Cell> neighbours = cellState.getCell().getNeighbours();
        byte aliveNeighbours = 0;

        for (Cell cell: neighbours) {
            if(grid.getState(cell)){
                aliveNeighbours++;
                if(aliveNeighbours>3){
                    //Cell will die.
                    cellState.setAlive(false);
                    return;
                }
            }
        }

        if(aliveNeighbours<2){
            //Cell will die
            cellState.setAlive(false);
        }
    }

}

/**
 * Target: Only needs to calculate if it will be alive or not.
 */
class StateCalculatorRunnableDead extends StateCalculatorRunnable{

    public StateCalculatorRunnableDead(Cell cell, IGrid grid) {
        super(cell,false, grid);
    }

    /*
      Rules:
      1. Dead + 3 alive neighbours => Becomes Alive
      2. Else: Stays Dead.
    */
    @Override
    public void run() {
        List<Cell> neighbours = cellState.getCell().getNeighbours();
        byte aliveNeighbours = 0;

        for (Cell cell: neighbours) {
            if(grid.getState(cell)){
                aliveNeighbours++;
            }
        }

        if(aliveNeighbours==3){
            //Cell will be resurrected/born
            cellState.setAlive(true);
        }

    }

}

/**
 * A class representing a cell change.
 */
class CellState {
    private Cell cell;
    private boolean isAlive = true;
    private boolean changed = false;

    public CellState(Cell cell, boolean isAlive){
        this.cell = cell;
        this.isAlive = isAlive;
    }

    public Cell getCell() {
        return cell;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        changed = true;
    }
}