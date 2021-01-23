package de.hdm_stuttgart.mi.gameoflife.core;

public interface IEngine {

    /**
     * Starts automatic calculation.
     * TODO: Add Settings Object
     */
    void startCalculation(Runnable onSuccess);

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
}
