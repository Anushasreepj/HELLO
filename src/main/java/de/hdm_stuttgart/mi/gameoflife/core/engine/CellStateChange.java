package de.hdm_stuttgart.mi.gameoflife.core.engine;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;

/**
 * A class representing the state of a cell for the coming generation
 */
public class CellStateChange {
    private Cell cell;
    private boolean alive = true;
    private boolean changed = false;

    /**
     * Generates FutureCellState with changed=false
     * @param cell Subject of this Change
     * @param isAlive Current State of the Subject
     */
    public CellStateChange(Cell cell, boolean isAlive){
        this.cell = cell;
        this.alive = isAlive;
    }

    /**
     * Generates FutureCellState with this.changed=changed
     * @param cell Subject of this Change
     * @param isAlive Current State of the Subject
     * @param changed Has the state already changed?
     */
    public CellStateChange(Cell cell, boolean isAlive, boolean changed){
        this.cell = cell;
        this.alive = isAlive;
        this.changed = changed;
    }

    /**
     *
     * @return Cell that is subject to this change
     */
    public Cell getCell() {
        return cell;
    }

    /**
     *
     * @return State of the Cell
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     *
     * @return Has the state changed?
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     *
     * @param alive Sets the current state to this value
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
        changed = true;
    }
}