package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.UICell;
import de.hdm_stuttgart.mi.gameoflife.controllers.components.UIZoomableScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
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
    private GridPane grid;

    @FXML
    private HBox wrapper;

    @FXML
    private UIZoomableScrollPane scrollPane;

    public void initialize() {
        int size = 200;

        grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid.add(new UICell(), i, j);
            }
        }

        scrollPane = new UIZoomableScrollPane(grid);
        wrapper.getChildren().add(scrollPane);
    }

}
