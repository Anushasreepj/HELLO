package de.hdm_stuttgart.mi.gameoflife.controllers.router;

public class Route {
    private String pathName;
    private String viewSrc;

    public Route(String pathName, String viewSrc) {
        this.pathName = pathName;
        this.viewSrc = viewSrc;
    }

    public String getViewSrc() {
        return viewSrc;
    }
}
