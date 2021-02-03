package de.hdm_stuttgart.mi.gameoflife.core.presets;
import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PlainTextDecoderTest {

    private PlainTextDecoder plainTextDecoder;

    @Before
    public void before(){
        plainTextDecoder = new PlainTextDecoder();
    }

    @Test
    public void testSquare() throws InvalidPresetFileException {
        StandardPreset outputPreset =
                plainTextDecoder.convertToPreset("!Name: Square\n" +
                "OO......\n" +
                "OO..\n\n\n");

        assertEquals("Square", outputPreset.getName());


        Cell[] cells = outputPreset.getCells();

        assertFalse(cells.length>4);
        assertEquals(cells[0], new Cell(0, 0));
        assertEquals(cells[1], new Cell(1, 0));
        assertEquals(cells[2], new Cell(0, 1));
        assertEquals(cells[3], new Cell(1, 1));
    }


}
