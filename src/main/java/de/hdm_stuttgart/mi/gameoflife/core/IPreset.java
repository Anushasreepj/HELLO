package de.hdm_stuttgart.mi.gameoflife.core;

/**
 * The Interface for Presets.
 * Using a data structure between preset file and the process loading the cells onto the grid takes up more storage
 * and makes loading gigantic structures like Gemini (https://conwaylife.com/wiki/Gemini) impossible,
 * but running those is a bit out of scope for this project anyways.
 */
public interface IPreset {

    /**
     *
     * @return Name of the Preset
     */
    String getName();

    /**
     *
     * @return All Cells that form this preset
     */
    Cell[] getCells();
}
