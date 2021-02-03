package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.*;
import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.controller.Controller;
import de.hdm_stuttgart.mi.gameoflife.core.controller.IController;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
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

    @FXML
    private Text generationCount;

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
        controller.reset();
        resetGenerationCount();
    }

    /**
     * Pause button clicked
     *
     * @param event
     */
    @FXML
    private void pauseClicked(ActionEvent event) {
        logger.info("`Pause` clicked");

        controller.pause();

        //pauseFrameTickInterval();
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

        //startFrameTickInterval();
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

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Speed equals tps/fps.
            //Speed 0 will basically add pause functionality at that point...

            if(newValue.intValue() > 0){
                controller.setSpeed(1000/newValue.intValue());
            } else {
                //Can't set to 0 or lower. Just pause the simulation, it will be restarted automatically once this number is > 0 again.
                controller.pause();
            }
        });


        try {
            // Initialize engine controller here
            controller.init();

            // Update Grid
            this.updateGrid();
            logger.info("update grid");

        } catch (EngineNotFoundException e) {
            logger.error("Error while initializing engine");
        }

        grid.setOnMouseClicked(event -> {
            int cellX = (int) Math.ceil(event.getX()/grid.getTotalCellSize()) - 1 + controller.getTopLeftBound().getX();
            int cellY = (int) Math.ceil(event.getY()/grid.getTotalCellSize()) - 1 + controller.getTopLeftBound().getY();

            logger.info("Cell clicked: " + cellX + "|" + cellY);


            controller.scheduleCellStateFlip(new Cell(cellX, cellY));
        });

        // Create frame tick interval and update grid on each frame
        createFrameTickInterval(60, () -> {
            updateGrid();
            updateGenerationCount();
        });

        startFrameTickInterval();

    }

    /**
     * Get alive cells from engine controller and update UI grid
     */
    private void updateGrid() {
        grid.update(controller.getChangedCellStates());
    }

    /**
     * Get generationCount and update UI
     */
    private void updateGenerationCount() {
        generationCount.setText(String.valueOf(controller.getGenerationCount()));
    }

    /**
     * Get generationCount and update UI
     */
    private void resetGenerationCount() {
        generationCount.setText("0");
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
