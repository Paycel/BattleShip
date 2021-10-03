package com.company;


import java.util.ArrayList;

public class Battle {
    private int playerShips = 10, computerShips = 10;
    private String[][] playerGrid, computerGrid;
    private int[][] missedGuesses;
    private Map playerMap, computerMap;
    private int numRows, numCols;

    public Battle(int size, boolean hide) {
    }

    public void turn() {
    }

    public void deployPlayerShips() {
    }

    public void deployShip() {
    }

    public int getRandomWay(boolean[] ways) {
        return 0;
    }

    public boolean isAvailable(int x, int y, ArrayList<String> ship, boolean isPlayer) {
        return true;
    }

    public void deployComputerShips() {
    }

    public void playerTurn(){
    }

    public boolean isSunk(int x, int y, String[][] grid) {
        return true;
    }

    public void fillSunkShip() {
    }

    public void computerTurn() {
    }

    public void gameOver() {
    }
}
