package de.hdm_stuttgart.mi.gameoflife.core.presets;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.IPreset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RLEDecoderTest {

    private RLEDecoder decoder;

    @Before
    public void before(){
        decoder = new RLEDecoder();
    }

    @Test
    public void testSquare() throws InvalidPresetFileException {
        IPreset outputPreset =
                decoder.convertToPreset("#N Square\n" +
                        "#R 4 4\n" + //Shift everything by 4 in both directions.
                        "2\no$\n2o!"); //Line breaks don't matter here!

        assertEquals("Square", outputPreset.getName());


        Cell[] cells = outputPreset.getCells();

        assertFalse(cells.length>4);

        //Offset by 4 in both directions.
        assertEquals(cells[0], new Cell(4, 4));
        assertEquals(cells[1], new Cell(5, 4));
        assertEquals(cells[2], new Cell(4, 5));
        assertEquals(cells[3], new Cell(5, 5));
    }
}
