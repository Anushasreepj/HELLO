package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.engine.CellStateChange;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class UIGrid extends Canvas {

    // How the cells will get rendered.
    final private int spaceBetween = 2;
    final private int cellSize = 10;
    final private int gridSize = 2000;

    /**
     *
     * @return the total size a cell will occupy.
     */
    public int getTotalCellSize(){
        return cellSize + spaceBetween;
    }


    public UIGrid() {
        // Setup canvas and create an empty grid
        createEmptyGrid();
    }

    /**
     * Apply changes to canvas grid. Remove killed cells and revive new cells.
     *
     * @param changedCellStates
     */
    public void update(CellStateChange[] changedCellStates) {

        GraphicsContext cc = this.getGraphicsContext2D();

        for(CellStateChange changedCellState: changedCellStates){
            updateCell(cc, changedCellState);
        }
    }

    public void loadAliveCells(Cell[] aliveCells)  {
        GraphicsContext cc = this.getGraphicsContext2D();

        for(Cell cell: aliveCells){
            updateCell(cc, new CellStateChange(cell, true));
        }
    }

    /**
     * Initialize an empty canvas grid
     */
    public void createEmptyGrid() {

        // Set grid width and height
        this.setWidth(gridSize);
        this.setHeight(gridSize);

        GraphicsContext cc = this.getGraphicsContext2D();

        // Style grid
        cc.setFill(Color.WHITE);
        cc.fill();

        // Draw the initial grid of dead cells so not everything is plain white.

        final int gridLength = gridSize / (cellSize + spaceBetween);
        for (int x = 0; x < gridLength; x++){
            for (int y = 0; y < gridLength; y++){
                updateCell(cc, new CellStateChange(new Cell(x,y), false, true));
            }
        }

    }

    /**
     * Update a single cell in canvas grid
     * @param cc
     * @param cellState
     */
    private void updateCell(GraphicsContext cc, CellStateChange cellState) {
        if(cellState.isAlive()) cc.setFill(Color.PURPLE);
        else cc.setFill(Color.DARKGRAY);

        final int cellSpace = cellSize + spaceBetween;
        final int xCellStart = cellState.getCell().getX() * cellSpace;
        final int yCellStart = cellState.getCell().getY() * cellSpace;
        cc.fillRect(xCellStart, yCellStart, cellSize, cellSize);
    }
}
