package de.hdm_stuttgart.mi.gameoflife.core;

public class SimulationSettings {

    //Basic optimization Setting
    //Settings for the area the engine should provide updates for, since anything not rendered doesn't need to get processed by the UI.
    private Cell bottomLeftViewCorner = new Cell(-100, -100);
    private Cell topRightViewCorner = new Cell(100, 100);

    public void setViewCorners(Cell corner1, Cell corner2){
        int x1 = corner1.getX();
        int x2 = corner2.getX();
        int y1 = corner1.getY();
        int y2 = corner2.getY();

        bottomLeftViewCorner = new Cell(Math.min(x1,x2), Math.min(y1,y2));
        topRightViewCorner = new Cell(Math.max(x1,x2), Math.max(y1,y2));
    }

    public Cell getBottomLeftViewCorner(){
        return bottomLeftViewCorner;
    }

    public Cell getTopRightViewCorner(){
        return topRightViewCorner;
    }


    private int msPerTick = 50; // = 20tps, should be pretty feasible for all modern hardware for normal sized simulations

    public void setMsPerTick(int newMsPerTick){
        msPerTick = newMsPerTick;
    }

    public int getMsPerTick(){
        return msPerTick;
    }


    //Tells the engine whether it should calculate synchronized or in parallel.
    private boolean parallelCalculations = false;

    public void setParallelCalculations(boolean parallelCalculations){
        this.parallelCalculations = parallelCalculations;
    }

    public boolean getParallelCalculations(){
        return parallelCalculations;
    }

}
