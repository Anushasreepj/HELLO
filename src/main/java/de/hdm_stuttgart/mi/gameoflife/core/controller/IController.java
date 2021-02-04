package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.engine.FutureCellState;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import de.hdm_stuttgart.mi.gameoflife.core.presets.PresetLoader;

public interface IController {
    void init() throws EngineNotFoundException;

    void loadPreset(String presetName, int offsetX, int offsetY);

    PresetLoader getPresetLoader();

    void reset();

    void start();

    void pause();

    void nextStep();

    void setSpeed(int msPerTick);

    FutureCellState[] getChangedCellStates();

    Cell[] getAliveCells();

    int getGenerationCount();

    Cell getBottomRightBound();

    Cell getTopLeftBound();

    void scheduleCellStateFlip(Cell cell);
}
