package de.hdm_stuttgart.mi.gameoflife.core.controller;

import de.hdm_stuttgart.mi.gameoflife.core.*;
import de.hdm_stuttgart.mi.gameoflife.core.engine.FutureCellState;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineFactory;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller implements IController {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    private IEngine engine;
    private IGrid gameGrid;
    private PlayState playState = PlayState.UNSET;
    private volatile int generationCount = 0; // Thread save
    private Editor editor = new Editor();
    private SimulationSettings settings = new SimulationSettings();

    public Controller() { }

    public void init() throws EngineNotFoundException {
        logger.info("Initialize Controller");

        // Load a preset from standardPreset factory
        editor.loadPreset(StandardPreset.getBlinker());

        // Get game grid
        gameGrid = editor.getGrid();

        // Initialize core engine with game grid
        engine = EngineFactory.loadByName("engine", gameGrid);
    }

    /**
     * Reset game state
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

    public FutureCellState[] getChangedCellStates() {
        return engine.getChanges();
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
