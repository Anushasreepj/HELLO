package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.function.Consumer;

public class UIEditor extends VBox
{
    private Consumer<String> presetsSelectChangedSubject;

    private UIPresetsSelect presetsSelect = new UIPresetsSelect((value) -> presetsSelectChangedSubject.accept(value));

    public UIEditor() {
        this.getChildren().add(presetsSelect);
    }

    public void addPresets(Collection<String> presets) {
        presetsSelect.add(presets);
    }

    public void registerPresetsSelectChangedListener(Consumer<String> presetName) {
        presetsSelectChangedSubject = presetName;
    }



}
