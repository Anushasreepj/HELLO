package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.*;
import de.hdm_stuttgart.mi.gameoflife.core.engine.CellStateChange;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineFactory;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import de.hdm_stuttgart.mi.gameoflife.core.presets.PresetLoader;
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
        loadPreset(fileToLoad, 20, 20);

        // Get game grid
        gameGrid = editor.getGrid();

        // Initialize core engine with game grid
        engine = EngineFactory.loadByName("engine", gameGrid);
    }

    /**
     * Load a preset from PresetLoader
     *
     * @param fileToLoad is a fileName
     * @param offsetX
     * @param offsetY
     */
    public void loadPreset(final String fileToLoad, final int offsetX, final int offsetY) {
        try {
            editor.loadPresetOffset(presetLoader.loadPreset(fileToLoad),offsetX,offsetY);
            settings.setUninitialized(true);
        } catch (Exception e){
            logger.warn("Standard Preset " + fileToLoad +" couldn't be loaded.");
            e.printStackTrace();
        }

        if (engine != null) {
            gameGrid = editor.getGrid();
            engine.loadGrid(gameGrid);
        }
    }

    /**
     * Get a PresetLoader instance
     *
     * @return PresetLoader
     */
    public PresetLoader getPresetLoader() {
        return presetLoader;
    }

    /**
     * Reset game state
     */
    public void reset() {
        engine.stopCalculation();
        editor.clear();
        // gameGrid = editor.getGrid();

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
        if(!engine.isRunning()){
            engine.nextGeneration();

            // Increment generation count
            generationCount++;
        }
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

    public CellStateChange[] getChangedCellStates() {
        return engine.getChanges();
    }

    /**
     * Get all current alive cells
     *
     * @return
     */
    public Cell[] getAliveCells() {
        return gameGrid.getAliveCells();
    }

    public int getGenerationCount() {
        return generationCount;
    }

    /**
     *
     * @return The Bottom Left Corner of the area the engine shall provide updates for
     */
    public Cell getBottomRightBound(){
        return settings.getBottomRightBound();
    }

    /**
     *
     * @return The Top Right Corner of the area the engine shall provide updates for
     */
    public Cell getTopLeftBound(){
        return settings.getTopLeftBound();
    }


    public void scheduleCellStateFlip(Cell cell) {
        engine.scheduleCellFlip(cell);
    }


}
