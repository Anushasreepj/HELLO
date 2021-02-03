package de.hdm_stuttgart.mi.gameoflife.core;

import de.hdm_stuttgart.mi.gameoflife.core.engine.FutureCellState;

public interface IEngine {

    /**
     * Starts automatic calculation.
     * @param settings The settings object
     */
    void startCalculation(Runnable onSuccess, SimulationSettings settings);

    /**
     * Loads in the settings for the engine.
     * @param settings
     */
    void loadSettings(SimulationSettings settings);

    /**
     * Will stop automatic calculation.
     * Won't do anything if none is running at all.
     */
    void stopCalculation();

    /**
     * Calculates one single next generation.
     */
    void nextGeneration();

    /**
     * Used to stop calculations and replace the internal grid with another one.
     * @param grid The new grid to use
     */
    void loadGrid(IGrid grid);

    void scheduleCellFlip(Cell cell);

    boolean isRunning();

    FutureCellState[] getChanges();
}
