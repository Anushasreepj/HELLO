package de.hdm_stuttgart.mi.gameoflife.core.presets;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.IPreset;

import java.util.ArrayList;
import java.util.List;

public class StandardPreset implements IPreset {

    //TODO: Implement proper Factory for Presets instead of this mess.

    public static StandardPreset getLightWeightSpaceShip() {
        return new StandardPreset(new Cell[] {new Cell(0, 2), new Cell(0, 5), new Cell(1, 1), new Cell(2, 1), new Cell(2, 5), new Cell(3, 1), new Cell(3, 2), new Cell(3, 3), new Cell(3, 4)},"Light Weight Space Ship");
    }

    public static StandardPreset getBlinker() {
        return new StandardPreset(new Cell[]{new Cell(1,1),new Cell(1,2),new Cell(1,3)},"Blinker");
    }

    public static StandardPreset getSquare(){
        return new StandardPreset(new Cell[]{new Cell(0,0), new Cell(0,1),new Cell(1,0),new Cell(1,1)},"Square");
    }

    public static StandardPreset getShortTub(){
        return new StandardPreset(new Cell[]{new Cell(0,1),new Cell(1,0),new Cell(1,2),new Cell(2,1),new Cell(2,3),new Cell(3,2)},"Small Tub");
    }

    public static StandardPreset getHive(){
        return new StandardPreset(new Cell[]{new Cell(0,1),new Cell(0,2),new Cell(1,0),new Cell(1,3),new Cell(2,1),new Cell(2,2)},"Hive");
    }

    public static StandardPreset getPresetFromString(String string){
        List<Cell> cells = new ArrayList<>();
        String[] cellParts = string.split("/");
        for(String cellPart:cellParts){
            String[] xAndY = cellPart.split(",");
            cells.add(new Cell(Integer.parseInt(xAndY[0]),Integer.parseInt(xAndY[1])));
        }

        return new StandardPreset(cells.toArray(new Cell[0]),"New Preset");
    }


    private final Cell[] cells;
    private String name;

    public StandardPreset(Cell[] cells, String name) {
        this.cells = cells;
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }

    public Cell[] getCells() {
        return cells;
    }
}
