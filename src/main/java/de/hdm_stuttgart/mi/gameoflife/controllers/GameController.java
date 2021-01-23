package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.*;
import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.controller.Controller;
import de.hdm_stuttgart.mi.gameoflife.core.controller.IController;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameController extends PageBaseController {
    private static final Logger logger = LogManager.getLogger(GameController.class);

    private IController controller = new Controller();

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
    private void navigateToMenu(ActionEvent event) {
        super.getRouter().navigate("menu");
    }

    @FXML
    private void resetClicked(ActionEvent event) {}

    @FXML
    private void pauseClicked(ActionEvent event) {}

    /**
     * TODO: This is only a test, bad performance
     *
     * @param event
     */
    @FXML
    private void startClicked(ActionEvent event) {
        logger.info("`Start` clicked");

        Timeline tick = new Timeline(
            new KeyFrame(Duration.millis(10),
            new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                long startTime = System.nanoTime();

                controller.nextStep();
                updateGrid();

                long elapsedTime = System.nanoTime() - startTime;
                logger.info("tick, " + elapsedTime/1000000f + "ms.");

            }
        }));

        tick.setCycleCount(Timeline.INDEFINITE);
        tick.play();
    }

    @FXML
    private void nextClicked(ActionEvent event) {
        logger.info("`Next` clicked");
        controller.nextStep();

        updateGrid();
    }

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
    }

    /**
     * Get alive cells from engine controller and update UI grid
     */
    private void updateGrid() {
        grid.update(controller.getAliveCells());
    }
}
