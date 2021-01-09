package de.hdm_stuttgart.mi.gameoflife.core;

public interface IEngine {
    void startCalculation();
    void stopCalculation();
    void nextGeneration();
    void loadGrid(IGrid grid);
}
