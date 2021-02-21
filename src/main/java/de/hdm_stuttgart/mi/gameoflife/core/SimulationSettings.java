package de.hdm_stuttgart.mi.gameoflife.core;

public class SimulationSettings {

    //Basic Setting targeted towards optimization
    //Settings for the area the engine should provide updates for, since anything not rendered doesn't need to get processed by the UI.
    private Cell bottomRightBound = new Cell(500, 500);
    private Cell topLeftBound = new Cell(0, -0);

    /**
     * Sets the corners for the area the engine shall provide updates for. The corners can be any two non-adjacent corners.
     * @param corner1 First Corner
     * @param corner2 Second Corner (Non-Adjacent to corner1)
     */
    public void setViewCorners(Cell corner1, Cell corner2){ //Ensures right corners even if top left and botton right where given.
        int x1 = corner1.getX();
        int x2 = corner2.getX();
        int y1 = corner1.getY();
        int y2 = corner2.getY();

        bottomRightBound = new Cell(Math.max(x1,x2), Math.max(y1,y2));
        topLeftBound = new Cell(Math.min(x1,x2), Math.min(y1,y2));
    }

    /**
     * Returns whether a cell is inside the bounds or not.
     * @param cell Cell to check
     * @return Is Cell in Bounds?
     */
    public boolean isInBounds(Cell cell){
        return (cell.getX() >= topLeftBound.getX() && cell.getY() >= topLeftBound.getY()) &&
                (cell.getX() <= bottomRightBound.getX() && cell.getY() <= bottomRightBound.getY());
    }

    /**
     *
     * @return The Bottom Left Corner of the area the engine shall provide updates for
     */
    public Cell getBottomRightBound(){
        return bottomRightBound;
    }

    /**
     *
     * @return The Top Right Corner of the area the engine shall provide updates for
     */
    public Cell getTopLeftBound(){
        return topLeftBound;
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

    /**
     * Sets whether the Engine should calculate in parallel or synchronized
     * @param parallelCalculations
     */
    public void setParallelCalculations(boolean parallelCalculations){
        this.parallelCalculations = parallelCalculations;
    }

    /**
     *
     * @return Should the Engine calculate in parallel or synchronized?
     */
    public boolean getParallelCalculations(){
        return parallelCalculations;
    }

    private boolean uninitialized = false;

    /**
     * Sets whether the engine should save all current cell states as changes. Good if UI needs a full update
     * @param uninitialized
     */
    public void setUninitialized(boolean uninitialized){
        this.uninitialized = uninitialized;
    }

    /**
     *
     * @return Is Uninitialized?
     */
    public boolean isUninitialized(){
        return uninitialized;
    }

}
