package de.hdm_stuttgart.mi.gameoflife.core.presets;

import de.hdm_stuttgart.mi.gameoflife.core.IPreset;

public interface IPresetDecoder {
    IPreset convertToPreset(String rawData) throws InvalidPresetFileException;
}
