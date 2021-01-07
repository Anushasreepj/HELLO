package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import javafx.scene.layout.GridPane;

public class UIGrid extends GridPane {
    final int size = 200;

    public UIGrid() {
        this.setHgap(1);
        this.setVgap(1);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                this.add(new UICell(), i, j);
            }
        }
    }
}
