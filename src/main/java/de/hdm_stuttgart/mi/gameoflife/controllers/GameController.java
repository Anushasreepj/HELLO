package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.*;
import de.hdm_stuttgart.mi.gameoflife.core.controller.Controller;
import de.hdm_stuttgart.mi.gameoflife.core.controller.IController;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameController extends PageBaseController {
    private static final Logger logger = LogManager.getLogger(GameController.class);

    private IController controller = new Controller();
    private Timeline frameTickInterval;

    @FXML
    private UIGrid grid;

    @FXML
    private HBox wrapper;

    @FXML
    private UIZoomableScrollPane scrollPane;

    @FXML
    private UIZoomSlider zoomSlider;

    @FXML
    private UISpeedSlider speedSlider;

    /**
     * Navigate back to menu button clicked
     *
     * @param event
     */
    @FXML
    private void navigateToMenu(ActionEvent event) {
        super.getRouter().navigate("menu");
    }

    /**
     * Reset button clicked
     *
     * @param event
     */
    @FXML
    private void resetClicked(ActionEvent event) {
        logger.info("`Reset` clicked");
        stopFrameTickInterval();
        controller.reset();
    }

    /**
     * Pause button clicked
     *
     * @param event
     */
    @FXML
    private void pauseClicked(ActionEvent event) {
        logger.info("`Pause` clicked");
        pauseFrameTickInterval();
        controller.pause();
    }

    /**
     * Start button clicked
     *
     * @param event
     */
    @FXML
    private void startClicked(ActionEvent event) {
        logger.info("`Start` clicked");

        controller.start();

        startFrameTickInterval();
    }

    /**
     * Next button blicked
     *
     * @param event
     */
    @FXML
    private void nextClicked(ActionEvent event) {
        logger.info("`Next` clicked");

        controller.nextStep();

        updateGrid();
    }

    /**
     * Initialize game controller
     *
     * - Create UIGrid
     * - Setup scroll and zoom pane
     * - Initialize engine controller
     * - Create frame tick interval
     *
     */
    public void initialize() {
        grid = new UIGrid();
        scrollPane = new UIZoomableScrollPane(grid);
        wrapper.getChildren().add(scrollPane);

        scrollPane.setScaleValue(1);

        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setScaleValue((double) newValue / 100);
        });

        try {
            // Initialize engine controller here
            controller.init();

            // Update Grid
            this.updateGrid();

        } catch (EngineNotFoundException e) {
            logger.error("Error while initializing engine");
        }

        // Create frame tick interval and update grid on each frame
        createFrameTickInterval(60, () -> updateGrid());
    }

    /**
     * Get alive cells from engine controller and update UI grid
     */
    private void updateGrid() {
        grid.update(controller.getAliveCells());
    }

    /**
     * Create frame tick interval
     * (Animate the UI)
     *
     * @param fps Frames per second
     * @param callback Handle interval event -> update grid
     */
    private void createFrameTickInterval(final int fps, Runnable callback) {
        frameTickInterval = new Timeline(
                new KeyFrame(Duration.millis(Math.floor(1000/fps)),
                        event -> {
                            // long startTime = System.nanoTime();

                            callback.run();

                            // long elapsedTime = System.nanoTime() - startTime;
                            // logger.info("tick, " + elapsedTime / 1000000f + "ms.");
                        }));

        frameTickInterval.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Start or resume frame tick interval
     */
    private void startFrameTickInterval() {
        switch (frameTickInterval.getStatus()) {
            case RUNNING:
                logger.info("Frame ticker already running");
                break;
            case PAUSED:
            case STOPPED:
            default:
                frameTickInterval.play();
                logger.info("Frame ticker started");
        }
    }

    /**
     * Pause the frame tick interval
     */
    private void pauseFrameTickInterval() {
        switch (frameTickInterval.getStatus()) {
            case PAUSED:
                logger.info("Frame ticker already paused");
                break;
            case STOPPED:
                logger.info("Frame ticker already stopped");
                break;
            case RUNNING:
            default:
                frameTickInterval.pause();
                logger.info("Frame ticker paused");
        }
    }

    /**
     * Stop the frame tick interval
     */
    private void stopFrameTickInterval() {
        switch (frameTickInterval.getStatus()) {
            case STOPPED:
                logger.info("Frame ticker already stopped");
                break;
            case PAUSED:
            case RUNNING:
            default:
                frameTickInterval.stop();
                logger.info("Frame ticker stopped");
        }
    }
}
