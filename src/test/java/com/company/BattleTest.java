package com.company;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class BattleTest {
    private static int size;
    private static boolean hide;

    @BeforeAll
    static void setup(){
        size = 10;
        hide = true;
    }

    @Test
    void getRandomWay() {
        Battle battle = new Battle(size, hide);

        boolean[] ways_1 = new boolean[4];
        boolean[] ways_2 = new boolean[4];
        boolean[] ways_3 = new boolean[4];

        Arrays.fill(ways_1, false);
        Arrays.fill(ways_2, false);
        Arrays.fill(ways_3, false);

        ways_2[2] = true;
        ways_3[0] = true;

        Assertions.assertEquals(-1, battle.getRandomWay(ways_1));
        Assertions.assertEquals(2, battle.getRandomWay(ways_2));
        Assertions.assertEquals(0, battle.getRandomWay(ways_3));
    }

    @Test
    void isAvailable() {
        Battle battle = new Battle(size, hide);
        String[][] grid = battle.getComputerGrid();
        ArrayList<String> ship = new ArrayList<>();

        grid[0][0] = "@";
        ship.add("00");
        battle.setComputerGrid(grid);

        Assertions.assertFalse(battle.isAvailable(0, 0, ship, false));
        Assertions.assertTrue(battle.isAvailable(0, 1, ship, false));
        Assertions.assertFalse(battle.isAvailable(1, 1, ship, false));
        Assertions.assertTrue(battle.isAvailable(1, 0, ship, false));
    }

    @Test
    void isSunk() {
        Battle battle = new Battle(size, hide);
        String[][] grid = battle.getComputerGrid();

        grid[0][2] = "!";
        grid[3][3] = "@";
        grid[3][4] = "!";
        grid[5][0] = "!";
        grid[5][1] = "!";
        grid[5][2] = "!";
        battle.setComputerGrid(grid);

        Assertions.assertTrue(battle.isSunk(0, 2, grid));
        Assertions.assertFalse(battle.isSunk(3, 3, grid));
        Assertions.assertTrue(battle.isSunk(5, 0, grid));
    }
}