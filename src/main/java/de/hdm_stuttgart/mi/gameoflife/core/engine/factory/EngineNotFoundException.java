package de.hdm_stuttgart.mi.gameoflife.core.engine.factory;

public class EngineNotFoundException extends Exception{
    public EngineNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
