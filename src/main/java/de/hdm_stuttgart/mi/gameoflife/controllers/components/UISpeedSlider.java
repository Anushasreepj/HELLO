package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import javafx.scene.control.Slider;

public class UISpeedSlider extends Slider {
    public UISpeedSlider() {
        this.setMax(100);
        this.setMin(0);
        this.setValue(50);
        this.setShowTickMarks(true);
        this.setShowTickLabels(true);
        this.setMinorTickCount(5);
        this.setMajorTickUnit(50);
        this.setBlockIncrement(10);
        this.setPrefSize(150, 5);
    }
}
