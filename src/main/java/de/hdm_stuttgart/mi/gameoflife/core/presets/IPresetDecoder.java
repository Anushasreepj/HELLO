package de.hdm_stuttgart.mi.gameoflife.core.presets;

public interface IPresetDecoder {
    StandardPreset convertToPreset(String rawData) throws InvalidPresetFileException;
}
