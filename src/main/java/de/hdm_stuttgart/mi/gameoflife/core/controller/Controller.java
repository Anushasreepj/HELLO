package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.*;
import de.hdm_stuttgart.mi.gameoflife.core.engine.FutureCellState;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineFactory;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import de.hdm_stuttgart.mi.gameoflife.core.presets.PresetLoader;
import de.hdm_stuttgart.mi.gameoflife.core.presets.StandardPreset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Controller implements IController {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    private IEngine engine;
    private IGrid gameGrid;
    private PlayState playState = PlayState.UNSET;
    private volatile int generationCount = 0; // Thread save
    private Editor editor = new Editor();
    private SimulationSettings settings = new SimulationSettings();
    private PresetLoader presetLoader = new PresetLoader();

    public Controller() { }

    public void init() throws EngineNotFoundException {
        logger.info("Initialize Controller");


        // Load a random preset from standardPreset factory
        String[] files = presetLoader.getAvailableFiles();
        String fileToLoad = files[new Random().nextInt(files.length)];

        try{
            editor.loadPresetOffset(presetLoader.loadPreset(fileToLoad),20,20);
            settings.setUninitialized(true);
        } catch (Exception e){
            logger.warn("Standard Preset " + fileToLoad +" couldn't be loaded.");
            e.printStackTrace();
        }

        // Get game grid
        gameGrid = editor.getGrid();

        // Initialize core engine with game grid
        engine = EngineFactory.loadByName("engine", gameGrid);
    }

    /**
     * Reset game state
     *
     * Todo
     *
     */
    public void reset() {
        engine.stopCalculation();

        // Reset generation count
        generationCount = 0;
    }

    /**
     * Start game
     */
    public void start() {
        engine.startCalculation(() -> {

            // Increment generation count
            generationCount++;
        }, settings);
    }

    /**
     * Pause game
     */
    public void pause() {
        engine.stopCalculation();
    }

    /**
     * Trigger next generation
     */
    public void nextStep() {
        engine.nextGeneration();

        // Increment generation count
        generationCount++;
    }

    /**
     * Update generation speed
     *
     * Todo
     *
     */
    public void setSpeed(int msPerTick) {
        pause();
        settings.setMsPerTick(msPerTick);
        start();
    }

    public FutureCellState[] getChangedCellStates() {
        return engine.getChanges();
    }

    public int getGenerationCount() {
        return generationCount;
    }
}
