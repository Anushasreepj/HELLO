package de.hdm_stuttgart.mi.gameoflife;

import java.util.HashMap;

public class Grid implements IGrid{

    HashMap<Position, Cell> aliveCells;

    @Override
    public void setState(Position position, boolean isAlive) {
        if(isAlive) {
            //Only change if the cell isn't already marked as alive
            if(!aliveCells.containsKey(position)) {
                aliveCells.put(position, new Cell(position));
            }
        } else {
            aliveCells.remove(position);
        }
    }

    @Override
    public boolean getState(Position position) {
        return aliveCells.containsKey(position);
    }

    @Override
    public Cell[] getAliveCells() {
        return aliveCells.values().toArray(new Cell[0]);
    }
}
