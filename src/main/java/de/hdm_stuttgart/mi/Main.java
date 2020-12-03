package de.hdm_stuttgart.mi;

import de.hdm_stuttgart.mi.gameoflife.controllers.router.Router;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private final Router router = new Router();

    @Override
    public void start(Stage primaryStage) throws Exception {
        router.setRoot(primaryStage, "Game Of Live", 960, 540);
        router.addPath("menu", "views/sample.fxml");
        router.navigate("menu");
    }

    public static void main(String[] args) {
        logger.info("Starting App");
        launch(args);
    }
}
