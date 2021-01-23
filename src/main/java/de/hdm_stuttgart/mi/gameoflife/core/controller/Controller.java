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
    private PlayState playState = PlayState.UNSET;
    private int generationCount = 0;
    private Editor editor = new Editor();

    public Controller() { }

    public void init() throws EngineNotFoundException {
        logger.info("Initialize Controller");

        // Load a preset from standardPreset factory
        editor.loadPreset(StandardPreset.getBlinker());

        // Get game grid
        gameGrid = editor.getGrid();

        // Initialize core engine with game grid
        engine = EngineFactory.loadByName("streamengine", gameGrid);
    }

    /**
     * TODO
     */
    public void reset() {
        engine.stopCalculation();
    }

    /**
     * TODO
     */
    public void start() {
        engine.startCalculation();
    }

    /**
     * TODO
     */
    public void pause() {
        engine.stopCalculation();

    }

    public void nextStep() {
        engine.nextGeneration();
    }

    /**
     * TODO
     */
    public void setSpeed() {

    }

    public Cell[] getAliveCells() {
        return gameGrid.getAliveCells();
    }
}
