package de.hdm_stuttgart.mi.gameoflife;

public class Cell {
    private Position position;

    public Position getPosition()  {
        return position;
    }

    public Cell(Position position) {
        this.position = position;
    }
}
