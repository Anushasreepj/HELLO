package de.hdm_stuttgart.mi.gameoflife.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Basic representation of the grid.
 * Doesn't know generations but is data structure to be used with a generational system.
 */
public class Grid implements IGrid{

    private HashSet<Cell> aliveCells = new HashSet<Cell>();

    /**
     * Sets the state of a cell to dead or alive.
     * @param cell The cell to be altered
     * @param isAlive The new state of the cell
     */
    public synchronized void setState(Cell cell, boolean isAlive) {
        if(isAlive) {
            //Since using a HashSet, duplicates will be ignored so an extra contains call is not necessary.
            aliveCells.add(cell);
        } else {
            aliveCells.remove(cell);
        }
    }

    /**
     * Gets the state of a cell.
     * @param cell The cell to be informed about
     * @return The state of the cell
     */
    public boolean getState(Cell cell) {
        return aliveCells.contains(cell);
    }

    /**
     *
     * @return A full list of all alive cells
     */
    public Cell[] getAliveCells() {
        return aliveCells.toArray(new Cell[0]);
    }
}