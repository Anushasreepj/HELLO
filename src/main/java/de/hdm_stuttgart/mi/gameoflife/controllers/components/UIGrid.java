package de.hdm_stuttgart.mi.gameoflife.controllers.components;

import de.hdm_stuttgart.mi.gameoflife.core.Cell;
import javafx.scene.layout.GridPane;

public class UIGrid extends GridPane {
    final private int size = 200;

    private Cell[] _aliveCells;

    public UIGrid() {
        this.setHgap(1);
        this.setVgap(1);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                this.add(new UICell(false), i, j);
            }
        }
    }

    /**
     * Update Grid UI
     *
     * Todo: Optimize, only apply changes
     *
     * @param aliveCells
     */
    public void update(Cell[] aliveCells) {

        // Kill old cells
        if (_aliveCells != null) {
            for (Cell cell : _aliveCells) {
                this.add(new UICell(false), cell.getX(), cell.getY());
            }
        }

        // Revive new cells
        for (Cell cell : aliveCells) {
            this.add(new UICell(true), cell.getX(), cell.getY());
        }

        _aliveCells = aliveCells;
    }
}
