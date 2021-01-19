package de.hdm_stuttgart.mi.gameoflife.core.controller;

public interface IController {
    void init();

    void reset();

    void start();

    void pause();

    void nextStep();

    void setZoom();

    void setSpeed();

    void loadPreset();
}
