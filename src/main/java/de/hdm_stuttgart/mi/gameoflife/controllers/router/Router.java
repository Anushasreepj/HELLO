package de.hdm_stuttgart.mi.gameoflife.controllers.router;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;

public class Router {
    private Stage rootStage;
    private Double windowWidth;
    private Double windowHeight;
    private AbstractMap<String, Route> routes = new HashMap<>();

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

    public void navigate(String pathName) throws IOException {
        Route route = routes.get(pathName);
        loadView(route);
    }

    private void loadView(Route route) throws IOException {
        String viewSrc = route.getViewSrc();

        URL fxmlFileUrl = getClass().getClassLoader().getResource(viewSrc);

        Parent view = FXMLLoader.load(fxmlFileUrl);

        rootStage.setScene(new Scene(view, windowWidth, windowHeight));
        rootStage.show();
    }
}
