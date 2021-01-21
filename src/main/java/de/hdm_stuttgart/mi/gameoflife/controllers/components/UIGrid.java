package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class UIGrid extends GridPane {
    final private int size = 200;

    //How the cells will get rendered.
    final private int spaceBetween = 2;
    final private int cellSize = 10;

    private Cell[] _aliveCells;
    private Canvas canvas;

    public UIGrid() {
        this.setHgap(1);
        this.setVgap(1);

        int canvasSizeX = 640;
        int canvasSizeY = 360;

        canvas = new Canvas(canvasSizeX,canvasSizeY);
        GraphicsContext cc = canvas.getGraphicsContext2D();
        cc.setFill(Color.WHITE);
        cc.fill();


        //Draw the initial grid of dead cells so not everything is white.
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (int x = 0; x<canvasSizeX/(spaceBetween+cellSize);x++){
            for (int y = 0; y<canvasSizeY/(spaceBetween+cellSize);y++){
                cells.add(new Cell(x,y));
            }
        }
        _aliveCells = cells.toArray(new Cell[0]);

        update(new Cell[0]);

        this.add(canvas,0,0);
    }



    public void update(Cell[] aliveCells) {

        GraphicsContext cc = canvas.getGraphicsContext2D();

        cc.setFill(Color.DARKGRAY);


        // Kill old cells
        if (_aliveCells != null) {
            for (Cell cell : _aliveCells) {
                int xCellStart = cell.getX()*(spaceBetween+cellSize);
                int yCellStart = cell.getY()*(spaceBetween+cellSize);
                cc.fillRect(xCellStart, yCellStart, cellSize, cellSize);
            }
        }

        cc.setFill(Color.PURPLE);

        // Revive new cells
        for (Cell cell : aliveCells) {
            int xCellStart = cell.getX()*(spaceBetween+cellSize);
            int yCellStart = cell.getY()*(spaceBetween+cellSize);
            cc.fillRect(xCellStart, yCellStart, cellSize, cellSize);
        }

        _aliveCells = aliveCells;
    }
}
