package de.hdm_stuttgart.mi.gameoflife.core;

import de.hdm_stuttgart.mi.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Editor {
    private static final Logger logger = LogManager.getLogger(Editor.class);

    private IGrid grid = new Grid();

    /**
     * Returns the grid
     * @return The editors grid
     */
    public IGrid getGrid(){
        return grid;
    }

    public void loadPreset(final IPreset preset){
        Arrays.stream(preset.getCells()).parallel().forEach(
                cell -> grid.setState(cell,true)
        );
        logger.info("Loaded preset " + preset.getName());
    }

    public void loadPresetOffset(final IPreset preset, final int offsetX, final int offsetY){

        Arrays.stream(preset.getCells()).parallel().forEach(
                cell -> grid.setState(new Cell(cell.getX()+offsetX, cell.getY()+offsetY),true)
        );

        logger.info("Loaded preset " + preset.getName() + " offset by " + offsetX + "x, " + offsetY + "y");
    }

    public void clear(){
        logger.info("Cleared grid");
        grid = new Grid();
    }

}
