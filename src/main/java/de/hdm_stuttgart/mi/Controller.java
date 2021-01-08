package de.hdm_stuttgart.mi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @FXML
    private void sayHello(ActionEvent event) {
        logger.info("Hello!");
    }
}
