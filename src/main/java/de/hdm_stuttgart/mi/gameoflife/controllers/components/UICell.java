package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UICell extends Rectangle {
    private final int x;
    private final int y;

    public UICell(final boolean isAlive, final int x, final int y) {
        this.setWidth(20);
        this.setHeight(20);
        this.setFill(isAlive ? Color.PURPLE : Color.DARKGRAY);

        this.x = x;
        this.y = y;
    }

    public int _getX() {
        return x;
    }

    public int _getY() {
        return y;
    }
}
