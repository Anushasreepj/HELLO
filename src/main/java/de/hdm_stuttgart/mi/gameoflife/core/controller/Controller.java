package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.*;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineFactory;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller implements IController {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    private IEngine engine;
    private IGrid gameGrid;
    private PlayState playState;
    private int generationCount;

    public Controller() { }

    public void init() throws EngineNotFoundException {
        logger.info("Initialize Controller");

        gameGrid = new Grid();
        engine = EngineFactory.loadByName("engine", gameGrid);
        playState = PlayState.UNSET;
        generationCount = 0;

        loadPreset(StandardPreset.getHive());
    }

    public void reset() {
        engine.stopCalculation();
    }

    public void start() {
        engine.startCalculation();
    }

    public void pause() {
        engine.stopCalculation();

    }

    public void nextStep() {
        engine.nextGeneration();
    }

    public void setZoom() {

    }

    public void setSpeed() {

    }

    public Cell[] getAliveCells() {
        return gameGrid.getAliveCells();
    }

    /**
     * Load `preset` into `gameGrid`
     *
     * @param preset
     */
    public void loadPreset(IPreset preset) {
        Cell[] cells = preset.getCells();

        for (Cell cell : cells) {
            gameGrid.setState(cell, true);
        }

        engine.loadGrid(gameGrid);
    }
}
