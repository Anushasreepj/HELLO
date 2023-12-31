package de.hdm_stuttgart.mi.gameoflife.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CellTest {


    @Test
    public void NeighbourTest() {
        Cell cell = new Cell(-4,4);
        List<Cell> neighbours = cell.getNeighbours();
        assertFalse(neighbours.isEmpty());
        assertTrue(neighbours.contains(new Cell(-3,3)));
        assertTrue(neighbours.contains(new Cell(-3,4)));
        assertTrue(neighbours.contains(new Cell(-3,5)));
        assertTrue(neighbours.contains(new Cell(-4,3)));
        assertTrue(neighbours.contains(new Cell(-4,5)));
        assertTrue(neighbours.contains(new Cell(-5,3)));
        assertTrue(neighbours.contains(new Cell(-5,4)));
        assertTrue(neighbours.contains(new Cell(-5,5)));
        assertEquals(8, neighbours.size());
    }
}
