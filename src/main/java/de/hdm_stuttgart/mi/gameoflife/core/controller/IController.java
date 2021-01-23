package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.IPreset;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;

public interface IController {
    void init() throws EngineNotFoundException;

    void reset();

    void start();

    void pause();

    void nextStep();

    void setSpeed();

    Cell[] getAliveCells();
}
