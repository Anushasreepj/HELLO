package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import javafx.scene.control.ComboBox;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.function.Consumer;

public class UIPresetsSelect extends ComboBox {
    public UIPresetsSelect(Consumer<String> onChange) {
        this.prefWidth(140);
        this.setPromptText("Select a preset");

        this.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            onChange.accept((String) newValue);
        });
    }

    public void add(Collection<String> options) {
        this.getItems().setAll(options);
    }
}
