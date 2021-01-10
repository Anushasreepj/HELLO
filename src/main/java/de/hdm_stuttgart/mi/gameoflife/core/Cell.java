package de.hdm_stuttgart.mi.gameoflife.core;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x) + Integer.hashCode(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell otherCell = (Cell) o;
        return otherCell.x == this.x && otherCell.y == this.y;
    }

    public List<Cell> getNeighbours(){
        List<Cell> neighbours = new ArrayList<Cell>();
        for (int i = -1; i<2; i++){
            for (int k = -1; k<2; k++){
                if(!(i==0&&k==0)){
                    neighbours.add(new Cell(x+i,y+k));
                }
            }
        }

        return neighbours;
    }
}
