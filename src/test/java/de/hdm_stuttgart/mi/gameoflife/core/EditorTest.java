package de.hdm_stuttgart.mi.gameoflife.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EditorTest {
    Editor editor;
    IPreset spaceShip = StandardPreset.getLightWeightSpaceShip();
    IPreset tub = StandardPreset.getShortTub();

    @Before
    public void before(){
        editor = new Editor();
    }

    @Test
    public void testLoadLWSS(){
        editor.clear();
        editor.loadPreset(spaceShip);

        IGrid grid = editor.getGrid();

        for (Cell cell: spaceShip.getCells()) {
            assertTrue(grid.getState(cell));
        }
        assertTrue(grid.getAliveCells().length==spaceShip.getCells().length);
    }

    @Test
    public void testLoadShortTub(){
        editor.clear();
        editor.loadPreset(tub);

        IGrid grid = editor.getGrid();

        for (Cell cell: tub.getCells()) {
            assertTrue(grid.getState(cell));
        }
        assertTrue(grid.getAliveCells().length==tub.getCells().length);
    }
}
