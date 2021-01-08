package de.hdm_stuttgart.mi.gameoflife.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class Grid implements IGrid{

    HashSet<Cell> aliveCells;

    @Override
    public void setState(Cell cell, boolean isAlive) {
        if(isAlive) {
            //Only change if the cell isn't already marked as alive
            if(!aliveCells.contains(cell)) {

            }
        } else {
            aliveCells.remove(cell);
        }
    }

    @Override
    public boolean getState(Cell cell) {
        return aliveCells.contains(cell);
    }

    @Override
    public Cell[] getAliveCells() {
        return aliveCells.toArray(new Cell[0]);
    }
}
