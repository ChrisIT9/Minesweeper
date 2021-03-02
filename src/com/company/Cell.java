package com.company;

public class Cell {
    private boolean containsBomb;
    private boolean isUncovered = false;
    private int adjacentBombs = 0;
    private Cell[] adjacentCells = new Cell[8];
    private int nOfAdjacentCells;
    private boolean isFlagged;

    public Cell(boolean containsBomb) {
        this.containsBomb = containsBomb;
        for (Cell cell : this.adjacentCells) cell = null;
        this.nOfAdjacentCells = 0;
        this.isFlagged = false;
    }

    public void addAdjacentCell(Cell newCell) {
        this.adjacentCells[nOfAdjacentCells++] = newCell;
    }

    public int getNOfAdjacentCells() {
        return this.nOfAdjacentCells;
    }

    public void calculateAdjacentBombs() {
        for (Cell cell : this.adjacentCells)
            if (cell != null && cell.containsBomb()) this.adjacentBombs++;
    }

    public boolean containsBomb() {
        return this.containsBomb;
    }

    public void uncover() {
        if (!isUncovered) this.isUncovered = true;
    }

    public boolean isUncovered() {
        return this.isUncovered;
    }

    public int getAdjacentBombs() {
        return this.adjacentBombs;
    }

    public Cell[] getAdjacentCells() {
        return this.adjacentCells;
    }

    public void flag() {
        if (!isUncovered) this.isFlagged = !this.isFlagged;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }
}
