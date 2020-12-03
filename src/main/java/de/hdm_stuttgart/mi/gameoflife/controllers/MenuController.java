package de.hdm_stuttgart.mi.gameoflife.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuController extends PageBaseController {
    private static final Logger logger = LogManager.getLogger(MenuController.class);

    @FXML
    private void navigateToGame(ActionEvent event) {
        super.getRouter().navigate("game");
    }

    @FXML
    private void navigateToTutorial(ActionEvent event) {
        super.getRouter().navigate("tutorial");
    }
}
