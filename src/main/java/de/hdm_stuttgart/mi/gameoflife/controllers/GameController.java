package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.*;
import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.controller.Controller;
import de.hdm_stuttgart.mi.gameoflife.core.controller.IController;
import de.hdm_stuttgart.mi.gameoflife.core.engine.factory.EngineNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
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

    @FXML
    private void startClicked(ActionEvent event) {}

    @FXML
    private void nextClicked(ActionEvent event) {
        controller.nextStep();

        this.updateGrid();
    }

    public void initialize() {
        grid = new UIGrid();
        scrollPane = new UIZoomableScrollPane(grid);
        wrapper.getChildren().add(scrollPane);

        scrollPane.setScaleValue(0.5);

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
