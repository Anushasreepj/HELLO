package de.hdm_stuttgart.mi.gameoflife.core.engine;
import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.IGrid;
import java.util.List;

class StateCalculatorRunnable implements Runnable{
    public final CellStateChange cellStateChange;
    private final IGrid grid;

    /**
     * Generates a Runnable for calculating the state of a alive cell in the next generation.
     * @param cell The cell to calculate
     * @param grid The grid the cell lives on
     */
    public StateCalculatorRunnable(Cell cell, IGrid grid) {
        this.grid = grid;
        this.cellStateChange = new CellStateChange(cell, true);
    }

    /**
      Rules:
      1. Alive + 2/3 alive neighbours => Stays Alive
      2. Else: Dies.
    */
    @Override
    public void run() {
        List<Cell> neighbours = cellStateChange.getCell().getNeighbours();
        byte aliveNeighbours = 0;

        for (Cell cell: neighbours) {
            if(grid.getState(cell)){
                aliveNeighbours++;
                if(aliveNeighbours>3){
                    //Cell will die.
                    cellStateChange.setAlive(false);
                    return;
                }
            }
        }

        if(aliveNeighbours<2){
            //Cell will die
            cellStateChange.setAlive(false);
        }
    }
}