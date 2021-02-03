package de.hdm_stuttgart.mi.gameoflife.core.presets;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlainTextDecoder implements IPresetDecoder {

    @Override
    public StandardPreset convertToPreset(String rawData) throws InvalidPresetFileException {
        //Plain Text Explained by https://www.conwaylife.com/wiki/Plaintext:
        /*
             The first line is a header line, which has the form
             !Name: Something
             This is followed by optional "!" lines that describe the pattern.

             Basically all variants use * or O (big o) for alive cells.
         */

        List<Cell> cells = new ArrayList<Cell>();


        //1. Separate Lines.
        String[] lines = rawData.split("\n");

        //2. Find out name.
        String name;
        List<String> nameLines = Arrays.stream(lines).filter(s -> s.startsWith("!Name:")).collect(Collectors.toList());
        if(nameLines.isEmpty()) name = "New Preset";
        else
        {
            name = nameLines.get(0).replace("!Name:","").strip();
        }

        //3. Convert to list of alive cells.
        List<Cell> aliveCells = new ArrayList<>();

        List<String> validLines = Arrays.stream(lines)
                .filter(s -> !s.startsWith("!")) //Don't translate 'commented' lines
                .collect(Collectors.toList());


        for (int y = 0; y < validLines.size(); y++){
            aliveCells.addAll(convertLineToCells(validLines.get(y),y));
        }

        return new StandardPreset(aliveCells.toArray(new Cell[0]), name);
    }


    private List<Cell> convertLineToCells(String line, int yPos){
        List<Cell> lineCells = new ArrayList<>();

        char[] chars = line.toCharArray();

        for (int xPos = 0; xPos < chars.length; xPos++){
            if(chars[xPos] == '*' || chars[xPos] == 'O'){
                lineCells.add(new Cell(xPos,yPos));
            }
        }

        return lineCells;
    }


}
