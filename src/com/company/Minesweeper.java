package com.company;

import java.util.Random;

/*
Se adjacentCells = 0, scopro e richiamo su tutte le celle adiacenti
se adjacentCells > 0 scopro e mi fermo
se la cella è una bomba ritorno, caso base
*/

public class Minesweeper {
    private Cell[][] grid;
    private GameState gameState;
    private GameResult gameResult;
    private int nOfBombs = 0;
    private int cellsToUncover;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";


    private void initializeGrid() {
        Random random = new Random();

        for (int row = 0; row < this.grid.length; row++) {
            for (int column = 0; column < this.grid[row].length; column++) {
                int chance = random.nextInt(8);

                if (chance == 0) {
                    this.grid[row][column] = new Cell(true);
                    this.nOfBombs++;
                }
                else this.grid[row][column] = new Cell(false);
            }
        }
    }

    private void calculateAdjacentCells() {
        for (int row = 0; row < this.grid.length; row++) {
            for (int column = 0; column < this.grid[row].length; column++) {

                if (row - 1 >= 0) { // riga superiore
                    this.grid[row][column].addAdjacentCell(this.grid[row - 1][column]);
                    if (column - 1 >= 0)
                        this.grid[row][column].addAdjacentCell(this.grid[row - 1][column - 1]);
                    if (column + 1 < this.grid[row].length)
                        this.grid[row][column].addAdjacentCell(this.grid[row - 1][column + 1]);
                }

                if (column - 1 >= 0)
                    this.grid[row][column].addAdjacentCell(this.grid[row][column - 1]); // casella sinistra
                if (column + 1 < this.grid[row].length)
                    this.grid[row][column].addAdjacentCell(this.grid[row][column + 1]); // casella destra

                if (row + 1 < this.grid.length) { // riga inferiore
                    this.grid[row][column].addAdjacentCell(this.grid[row + 1][column]);
                    if (column - 1 >= 0)
                        this.grid[row][column].addAdjacentCell(this.grid[row + 1][column - 1]);
                    if (column + 1 < this.grid[row].length)
                        this.grid[row][column].addAdjacentCell(this.grid[row + 1][column + 1]);
                }

            }
        }
    }

    private void calculateAdjacentBombs() {
        for (int row = 0; row < this.grid.length; row++)
            for (int column = 0; column < this.grid[row].length; column++)
                this.grid[row][column].calculateAdjacentBombs();
    }

    private void floodFill(Cell cell) {
        if (cell.containsBomb()) return;

        if (!cell.isUncovered()) { // edge-case quando la casella da cui parte la ricorsione è una casella con 0 bombe adiacenti, in questo modo evito di scoprirla due volte
            cell.uncover();
            this.cellsToUncover--;
        }

        if (cell.getAdjacentBombs() > 0) return;

        Cell[] adjacentCells = cell.getAdjacentCells();

        for (int i = 0; i < cell.getNOfAdjacentCells(); i++)
            if (!adjacentCells[i].isUncovered())
                floodFill(adjacentCells[i]);

    }


    public Minesweeper(int rows, int columns) {
        this.grid = new Cell[rows][columns];
        initializeGrid();
        calculateAdjacentCells();
        calculateAdjacentBombs();
        this.gameState = GameState.IN_PROGRESS;
        this.gameResult = GameResult.NOT_AVAILABLE;
        this.cellsToUncover = (this.grid[0].length * this.grid.length) - this.nOfBombs;
    }

    public void play(int row, int column) {
        if (this.gameState == GameState.OVER || row < 0 || row >= this.grid.length || column < 0 || column >= this.grid[0].length || this.grid[row][column].isUncovered())
            return;

        this.grid[row][column].uncover();
        this.cellsToUncover--;

        if (this.grid[row][column].containsBomb()) {
            this.gameState = GameState.OVER;
            this.gameResult = GameResult.HIT_BOMB;
        }

        if (this.gameState != GameState.OVER) {
            floodFill(this.grid[row][column]);
        }

        if (this.cellsToUncover == 0) {
            this.gameState = GameState.OVER;
            this.gameResult = GameResult.WON;
        }

    }

    public void flag(int row, int column) {
        if (this.gameState == GameState.OVER || row < 0 || row >= this.grid.length || column < 0 || column >= this.grid[0].length || this.grid[row][column].isUncovered()) return;

        this.grid[row][column].flag();
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public GameResult getGameResult() {
        return this.gameResult;
    }

    public String toString() {
        String s = "";
        for (int row = 0; row < this.grid.length; row++) {
            s += row + " [ ";
            for (int column = 0; column < this.grid[row].length; column++) {

                if (this.grid[row][column].containsBomb() && this.gameState == GameState.OVER) s += ANSI_YELLOW + "X" + ANSI_RESET; // partita finita, stampo le bombe
                else if (this.grid[row][column].isFlagged()) s += ANSI_RED + "o" + ANSI_RESET; // casella contrassegnata
                else if (!this.grid[row][column].isUncovered()) s += ANSI_BLUE + "o" + ANSI_RESET; // casella coperta
                else if (this.grid[row][column].getAdjacentBombs() == 0 && this.grid[row][column].isUncovered()) s += " "; // casella scoperta con 0 bombe adiacenti
                else s += ANSI_GREEN + this.grid[row][column].getAdjacentBombs() + ANSI_RESET; // casella scoperta con > 0 bombe adiacenti


                if (column < this.grid[row].length - 1) s += " ";
            }
            s += " ]\n";
        }

        s += "    ";
        for (int i = 0; i < this.grid[0].length; i++) s += i + " ";

        return s;
    }
}
