package de.hdm_stuttgart.mi.gameoflife.core;

/**
 * Basic representation of the grid.
 * Doesn't know generations but is data structure to be used with a generational system.
 */
public interface IGrid {
    /**
     * Sets the state of a cell to dead or alive.
     * @param cell The cell to be altered
     * @param isAlive The new state of the cell
     */
    void setState(Cell cell, boolean isAlive);

    /**
     * Gets the state of a cell.
     * @param cell The cell to be informed about
     * @return The state of the cell
     */
    boolean getState(Cell cell);

    /**
     *
     * @return A full list of all alive cells
     */
    Cell[] getAliveCells();

}