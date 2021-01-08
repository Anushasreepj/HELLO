package de.hdm_stuttgart.mi.gameoflife.controllers.router;

import de.hdm_stuttgart.mi.gameoflife.controllers.PageBaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;

public class Router {
    private Stage rootStage;
    private Double windowWidth;
    private Double windowHeight;
    private AbstractMap<String, Route> routes = new HashMap<>();

    private static final Logger logger = LogManager.getLogger(Router.class);

    public void setRoot(Stage stage, String title, double width, double height) {
        if (rootStage == null) {
            rootStage = stage;
        }

        windowWidth = width;
        windowHeight = height;

        rootStage.setTitle(title);
    }

    public void addPath(String pathName, String viewSrc) {
        Route route = new Route(pathName, viewSrc);
        routes.put(pathName, route);
    }

    public void navigate(String pathName) {
        Route route = routes.get(pathName);

        try {
            loadView(route);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void loadView(Route route) throws IOException {
        String viewSrc = route.getViewSrc();

        URL fxmlFileUrl = getClass().getClassLoader().getResource(viewSrc);

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFileUrl);
        Parent view = fxmlLoader.load();

        // Get view controller
        // Every view controller extends the `PageBaseController`
        PageBaseController controller = fxmlLoader.getController();
        // Set router for controller
        controller.setRouter(this);


        rootStage.setScene(new Scene(view, windowWidth, windowHeight));
        rootStage.show();
    }
}
