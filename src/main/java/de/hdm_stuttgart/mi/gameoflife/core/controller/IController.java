package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.engine.FutureCellState;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;

public interface IController {
    void init() throws EngineNotFoundException;

    void loadPreset(String presetName, int offsetX, int offsetY);

    void reset();

    void start();

    void pause();

    void nextStep();

    void setSpeed(int msPerTick);

    FutureCellState[] getChangedCellStates();

    int getGenerationCount();
}
