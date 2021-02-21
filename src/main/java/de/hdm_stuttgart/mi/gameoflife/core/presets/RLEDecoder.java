package de.hdm_stuttgart.mi.gameoflife.core.presets;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.IPreset;

import java.util.ArrayList;
import java.util.List;

/**
 RLE Structure for our GOL:

 Just basic RLE, with the structure run_coun|ttag. The last item of the whole preset is followed by a !.
 A line end is indicated by a $.
 Example for basic glider:
 3o$2bo$bo!

 The basic structure is provided by # lines:
 #N ThisIsTheName
 #R 5 7 This will offset the structure by 5 in x- and 7 in y-Direction.
 All other # lines beginning with a # will just be handled as comments.

 */
public class RLEDecoder implements IPresetDecoder{

    public IPreset convertToPreset(String rawData) throws InvalidPresetFileException {


        //1. Process Lines
        String[] lines = rawData.split("\n");
        StringBuilder validTextBuilder = new StringBuilder();
        String name = "New Preset";
        int xOffset = 0;
        int yOffset = 0;

        for (String line: lines) {
            if(line.startsWith("#")){
                if(line.startsWith("#N")){
                    name = line.replace("#N","").strip();
                }

                if(line.startsWith("#R")){
                    String[] parts = line.replace("#R","").strip().split(" ");

                    if(parts.length>=2){
                        try{
                            xOffset = Integer.parseInt(parts[0]);
                            yOffset = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e){
                            //Reset is broken TODO: Log Error to console.
                            xOffset = 0;
                            yOffset = 0;
                        }
                    }

                }

            } else {
                validTextBuilder.append(line);
            }
        }

        return new StandardPreset(decode(validTextBuilder.toString(), xOffset, yOffset), name);
    }

    private Cell[] decode(String string, int xOffset, int yOffset){ //That's the good stuff!
        List<Cell> cells = new ArrayList<Cell>();
        int x = 0;

        String[] lines = string.split("\\$"); //Split into single lines.

        for (int y = 0; y < lines.length; y++){
            String line = lines[y];
            x = 0;

            int currentCount = 0;
            for (int i = 0; i < line.length(); i++){

                char c = line.charAt(i);

                if(Character.isDigit(c)){
                    currentCount = currentCount*10 + c - '0'; //Reads char by char so we need to shift it to the next multiply by ten.
                } else {
                    if(currentCount == 0){
                        //Just one.
                        if(c == 'o') cells.add(new Cell(x + xOffset, y + yOffset));
                        x++;
                    } else {

                        for (int k = 0; k < currentCount; k++){
                            if(c == 'o') cells.add(new Cell(x + xOffset, y + yOffset));
                            x++;
                        }

                        currentCount = 0;
                    }
                }
            }
        }

        return cells.toArray(new Cell[0]);

    }


}
