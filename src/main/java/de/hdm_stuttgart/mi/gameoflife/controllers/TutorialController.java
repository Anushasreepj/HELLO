package de.hdm_stuttgart.mi.gameoflife.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TutorialController extends PageBaseController {
    private static final Logger logger = LogManager.getLogger(TutorialController.class);

    @FXML
    private void navigateToMenu(ActionEvent event) {
        super.getRouter().navigate("menu");
    }
}
