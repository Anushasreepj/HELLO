package de.hdm_stuttgart.mi.gameoflife.core;

public interface IEngine {
    //TODO: ADD SETTINGS CLASS

    /**
     *
     */
    void startCalculation();
    void stopCalculation();
    void nextGeneration();
}
