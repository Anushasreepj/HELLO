package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import de.hdm_stuttgart.mi.gameoflife.core.engine.FutureCellState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class UIGrid extends Canvas {

    // How the cells will get rendered.
    final private int spaceBetween = 2;
    final private int cellSize = 10;

    /**
     *
     * @return the total size a cell will occupy.
     */
    public int getTotalCellSize(){
        return cellSize + spaceBetween;
    }


    public UIGrid() {
        // Setup canvas and create an empty grid
        createEmptyGrid(2000);
    }

    /**
     * Apply changes to canvas grid. Remove killed cells and revive new cells.
     *
     * @param changedCellStates
     */
    public void update(FutureCellState[] changedCellStates) {

        GraphicsContext cc = this.getGraphicsContext2D();

        for(FutureCellState changedCellState: changedCellStates){
            updateCell(cc, changedCellState);
        }
    }

    /**
     * Initialize an empty canvas grid
     *
     * @param gridSize width = height
     */
    private void createEmptyGrid(final int gridSize) {



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
                updateCell(cc, new FutureCellState(new Cell(x,y), false, true));
            }
        }

    }

    /**
     * Update a single cell in canvas grid
     * @param cc
     * @param cellState
     */
    private void updateCell(GraphicsContext cc, FutureCellState cellState) {
        if(cellState.isAlive()) cc.setFill(Color.PURPLE);
        else cc.setFill(Color.DARKGRAY);

        final int cellSpace = cellSize + spaceBetween;
        final int xCellStart = cellState.getCell().getX() * cellSpace;
        final int yCellStart = cellState.getCell().getY() * cellSpace;
        cc.fillRect(xCellStart, yCellStart, cellSize, cellSize);
    }
}
