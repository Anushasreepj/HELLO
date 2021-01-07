package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UICell extends Rectangle {
    public UICell() {
        this.setWidth(20);
        this.setHeight(20);
        this.setFill(Color.DARKGRAY);
    }
}
