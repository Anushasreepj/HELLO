package de.hdm_stuttgart.mi.gameoflife.core;

public interface IGrid {

    void setState(Cell cell, boolean isAlive);

    boolean getState(Cell cell);

    Cell[] getAliveCells();

}
