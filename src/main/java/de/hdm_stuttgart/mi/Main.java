package de.hdm_stuttgart.mi;

import de.hdm_stuttgart.mi.gameoflife.controllers.router.Router;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        initRouting(primaryStage);
    }

    public static void main(String[] args) {
        logger.info("Starting App");
        launch(args);
    }

    /**
     * Initialize Routing
     *
     * Steps to create a new route:
     * 1. Create a <name>.fxml file in resources/views
     * 2. Create a new controller class that extends the `PageBaseController`
     * 3. Register the route with `router.addPath(pathName, viewSrc)`
     *
     * @param primaryStage
     */
    private void initRouting(Stage primaryStage) {
        Router router = new Router();

        router.setRoot(primaryStage, "Conway's Game Of Life", 1200, 776);

        // Setup routes here
        router.addPath("menu", "views/menu.fxml");
        router.addPath("game", "views/game.fxml");
        router.addPath("tutorial", "views/tutorial.fxml");

        router.navigate("game");
    }
}
