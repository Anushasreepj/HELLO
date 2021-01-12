package de.hdm_stuttgart.mi.gameoflife.core;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.List;

public class CellTest {


    @Test
    public void NeighbourTest() {
        Cell cell = new Cell(-4,4);
        List<Cell> neighbours = cell.getNeighbours();
        assertTrue(!neighbours.isEmpty());
        assertTrue(neighbours.contains(new Cell(-3,3)));
        assertTrue(neighbours.contains(new Cell(-3,4)));
        assertTrue(neighbours.contains(new Cell(-3,5)));
        assertTrue(neighbours.contains(new Cell(-4,3)));
        assertTrue(neighbours.contains(new Cell(-4,5)));
        assertTrue(neighbours.contains(new Cell(-5,3)));
        assertTrue(neighbours.contains(new Cell(-5,4)));
        assertTrue(neighbours.contains(new Cell(-5,5)));
        assertTrue(neighbours.size()==8);
    }
}
