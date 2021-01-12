package de.hdm_stuttgart.mi.gameoflife.core.engine;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;

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