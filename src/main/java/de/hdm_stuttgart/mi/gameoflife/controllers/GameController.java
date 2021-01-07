package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.UIGrid;
import de.hdm_stuttgart.mi.gameoflife.controllers.components.UIZoomableScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameController extends PageBaseController {
    private static final Logger logger = LogManager.getLogger(GameController.class);

    @FXML
    private void navigateToMenu(ActionEvent event) {
        super.getRouter().navigate("menu");
    }

    @FXML
    private UIGrid grid;

    @FXML
    private HBox wrapper;

    @FXML
    private UIZoomableScrollPane scrollPane;

    public void initialize() {
        grid = new UIGrid();
        scrollPane = new UIZoomableScrollPane(grid);
        wrapper.getChildren().add(scrollPane);
    }

}
