package com.company;

import lombok.Getter;

@Getter
public class Map {
    private int numRows;
    private int numCols;
    private boolean hide;
    public String[][] grid;

    public Map(int size, boolean hide) {
        this.hide = hide;
        this.numRows = size;
        this.numCols = size;
        this.grid = new String[size][size];
        createOceanMap();
    }

    private void createOceanMap() {
        //First section of Ocean Map
        System.out.print("\t");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + "\t");
        System.out.println();

        //Middle section of Ocean Map
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = " ";
                if (j == 0)
                    System.out.print(i + "|\t" + grid[i][j] + "\t");
                else if (j == grid[i].length - 1)
                    System.out.print(grid[i][j] + "\t|" + i);
                else
                    System.out.print(grid[i][j] + "\t");
            }
            System.out.println();
        }

        //Last section of Ocean Map
        System.out.print("\t");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + "\t");
        System.out.println();
    }

    public void printOceanMap() {
        System.out.println();
        //First section of Ocean Map
        System.out.print("\t");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + "\t");
        System.out.println();

        //Middle section of Ocean Map
        for (int x = 0; x < grid.length; x++) {
            System.out.print(x + "|\t");

            for (int y = 0; y < grid[x].length; y++) {
                System.out.print((grid[x][y].equals("@") && hide ? "" : grid[x][y]) + "\t");
            }

            System.out.println("|" + x);
        }

        //Last section of Ocean Map
        System.out.print("\t");
        for (int i = 0; i < numCols; i++)
            System.out.print(i + "\t");
        System.out.println();
    }
}
