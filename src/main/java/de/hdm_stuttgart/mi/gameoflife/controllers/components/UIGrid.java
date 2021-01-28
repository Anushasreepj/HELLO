package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class UIGrid extends Canvas {

    // How the cells will get rendered.
    final private int spaceBetween = 2;
    final private int cellSize = 10;

    /**
     * Holds grid state (all alive cells)
     */
    private Cell[] _aliveCells;

    public UIGrid() {
        // Setup canvas and create an empty grid
        createEmptyGrid(2000);
    }

    /**
     * Apply changes to canvas grid. Remove killed cells and revive new cells.
     *
     * @param aliveCells
     */
    public void update(Cell[] aliveCells) {

        GraphicsContext cc = this.getGraphicsContext2D();

        // Kill old cells
        cc.setFill(Color.DARKGRAY);
        if (_aliveCells != null) {
            for (Cell cell : _aliveCells) {
                updateCell(cc, cell);
            }
        }

        // Revive new cells
        cc.setFill(Color.PURPLE);
        for (Cell cell : aliveCells) {
            updateCell(cc, cell);
        }

        _aliveCells = aliveCells;
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

        // Draw the initial grid of dead cells so not everything is white.
        ArrayList<Cell> cells = new ArrayList<Cell>();
        final int gridLength = gridSize / (cellSize + spaceBetween);
        for (int x = 0; x < gridLength; x++){
            for (int y = 0; y < gridLength; y++){
                cells.add(new Cell(x,y));
            }
        }

        _aliveCells = cells.toArray(new Cell[0]);
    }

    /**
     * Update a single cell in canvas grid
     * @param cc
     * @param cell
     */
    private void updateCell(GraphicsContext cc, Cell cell) {
        final int cellSpace = cellSize + spaceBetween;
        final int xCellStart = cell.getX() * cellSpace;
        final int yCellStart = cell.getY() * cellSpace;
        cc.fillRect(xCellStart, yCellStart, cellSize, cellSize);
    }
}
