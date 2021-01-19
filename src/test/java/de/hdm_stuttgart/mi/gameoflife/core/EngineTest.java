package de.hdm_stuttgart.mi.gameoflife.core;


import de.hdm_stuttgart.mi.gameoflife.core.engine.Engine;
import de.hdm_stuttgart.mi.gameoflife.core.engine.StreamEngine;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineFactory;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EngineTest {


    IEngine engine;

    @Before
    public void EngineTest(){
        try{
            engine = EngineFactory.loadByReflection(StreamEngine.class.getName());
        } catch (EngineNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Tests for correct calculation of different small structures that won't change while untouched:
     * ▯ => alive
     * 1. Square:
     *      ▯▯      0,0/0,1
     *      ▯▯      1,0/1,1
     *
     * 2. Small Tub:
     *      _▯__    0,1
     *      ▯_▯_    1,0/1,2
     *      _▯_▯    2,1/2,3
     *      __▯_    3,2
     *
     * 3. Hive:
     *      _▯▯_    0,1/0,2
     *      ▯__▯    1,0/1,3
     *      _▯▯_    2,1/2,2
     *
     */
    public void testPersistentStructures(){
        int squareOffsetX = 0;
        Cell[] square = {new Cell(0+squareOffsetX,0), new Cell(0+squareOffsetX,1),new Cell(1+squareOffsetX,0),new Cell(1+squareOffsetX,1)};

        int tubOffsetX = 4;
        Cell[] smallTub = {new Cell(0+tubOffsetX,1),new Cell(1+tubOffsetX,0),new Cell(1+tubOffsetX,2),new Cell(2+tubOffsetX,1),new Cell(2+tubOffsetX,3),new Cell(3+tubOffsetX,2)};

        int hiveOffsetX = 10;
        Cell[] hive = {new Cell(0+hiveOffsetX,1),new Cell(0+hiveOffsetX,2),new Cell(1+hiveOffsetX,0),new Cell(1+hiveOffsetX,3),new Cell(2+hiveOffsetX,1),new Cell(2+hiveOffsetX,2)};


        //Load structures onto test grid

        IGrid grid = new Grid();

        for (Cell cell:square){
            grid.setState(cell,true);
        }

        for (Cell cell:smallTub){
            grid.setState(cell,true);
        }

        for (Cell cell:hive){
            grid.setState(cell,true);
        }

        engine.loadGrid(grid);
        engine.nextGeneration();

        for (Cell cell:square){
            assertTrue(grid.getState(cell));
        }

        for (Cell cell:smallTub){
            assertTrue(grid.getState(cell));
        }

        for (Cell cell:hive){
            assertTrue(grid.getState(cell));
        }

        assertTrue(grid.getAliveCells().length==(square.length+smallTub.length+hive.length));

    }


    @Test
    /**
     * Tests for successfully calculated generation of the blinker.
     * ▯ => alive
     * _▯_          ___
     * _▯_   =>     ▯▯▯
     * _▯_          ___
     * 1,1          0,2
     * 1,2   =>     1,2
     * 1,3          2,2
     */
    public void testBlinker(){

        Cell[] initialPositions = {new Cell(1,1),new Cell(1,2),new Cell(1,3)};
        Cell[] targetPositions = {new Cell(0,2),new Cell(1,2),new Cell(2,2)};

        IGrid grid = new Grid();
        for(Cell cell: initialPositions){
            grid.setState(cell, true);
        }

        engine.loadGrid(grid);

        //Calculate ONE Generation
        engine.nextGeneration();

        for(Cell cell: targetPositions){
            assertTrue(grid.getState(cell));
        }

        assertTrue(grid.getAliveCells().length==targetPositions.length);
    }

    @Test
    /**
     * Tests for successfully calculated generation of the LWSS
     * ▯ => alive
     * __▯__▯    0,2/0,5
     * _▯____    1,1
     * _▯___▯    2,1/2,5
     * _▯▯▯▯_    3,1/3,2/3,3/3,4
     *
     * Expected result:
     * _____
     * _▯▯__    1,1/1,2
     * ▯▯_▯▯    2,0/2,1/2,3/2,4
     * _▯▯▯▯    3,1/3,2/3,3/3,4
     * __▯▯_    4,2/4,3
     */
    public void testLightWeightSpaceship(){
        Cell[] start = stringToCells("0,2/0,5/1,1/2,1/2,5/3,1/3,2/3,3/3,4");
        Cell[] expected = stringToCells("1,1/1,2/2,0/2,1/2,3/2,4/3,1/3,2/3,3/3,4/4,2/4,3");

        IGrid grid = new Grid();
        for(Cell cell: start){
            grid.setState(cell, true);
        }

        engine.loadGrid(grid);
        engine.nextGeneration();

        for(Cell cell: expected){
            assertTrue(grid.getState(cell));
        }

        assertTrue(grid.getAliveCells().length==expected.length);
    }

    @Test
    /**
     * Places one 100k Cells spaced out so each cell has to die.
     * Doubles as performance test since the space is that big, that EACH neighbour cell has to get calculated too.
     */
    public void testLonelyCellsDie(){

        IGrid grid = new Grid();

        int cellsToPlace = 100000;

        for (int i = 0; i<cellsToPlace; i++){
            grid.setState(new Cell(i*2,0),true); //Really spaced out => many neighbours to calculate too.
        }

        engine.loadGrid(grid);

        engine.nextGeneration();

        assertTrue(grid.getAliveCells().length == 0);
    }


    /**
     engine.loadGrid(grid);

     engine.nextGeneration();
     * Small helper function that converts a String of "x1,y1/x2,y2/.../xn/yn" to an Array of Cells.
     * @param string The preformatted string of x,y/x,y/...
     * @return An Array of Cells generated out of the String.
     */
    private Cell[] stringToCells(String string){
        List<Cell> cells = new ArrayList<>();
        String[] cellParts = string.split("/");
        for(String cellPart:cellParts){
            String[] xAndY = cellPart.split(",");
            cells.add(new Cell(Integer.parseInt(xAndY[0]),Integer.parseInt(xAndY[1])));
        }

        return cells.toArray(new Cell[0]);
    }


}
