package de.hdm_stuttgart.mi.gameoflife;

public interface IGrid {

    void setState(Position position, boolean isAlive);

    boolean getState(Position position);

    Cell[] getAliveCells();

}
