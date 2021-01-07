package de.hdm_stuttgart.mi.gameoflife.controllers;

import de.hdm_stuttgart.mi.gameoflife.controllers.components.UICell;
import de.hdm_stuttgart.mi.gameoflife.controllers.router.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
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

    public void initialize() {
        int size = 200;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid.add(new UICell(), i, j);
            }
        }
    }

}
