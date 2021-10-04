package com.company;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Battle {
    private int playerShips = 10, computerShips = 10;
    private String[][] playerGrid, computerGrid;
    private int[][] missedGuesses;
    private Map playerMap, computerMap;
    private int numRows, numCols;

    public Battle(int size, boolean hide) {
        this.playerMap = new Map(size, false);
        this.computerMap = new Map(size, hide);
        this.numRows = size;
        this.numCols = size;
        this.playerGrid = this.playerMap.grid;
        this.computerGrid = this.computerMap.grid;
        this.missedGuesses = new int[numRows][numCols];
        int[] buff = new int[numCols];
        Arrays.fill(buff, 0);
        Arrays.fill(this.missedGuesses, buff.clone());
    }

    public void turn() {
        playerTurn();
        computerTurn();

        System.out.println("\nYour field:");
        playerMap.printOceanMap();
        System.out.println("\nComputer field:");
        computerMap.printOceanMap();

        System.out.println();
        System.out.println("Your ships: " + playerShips + " | Computer ships: " + computerShips);
        System.out.println();
    }

    public void deployPlayerShips() {
        System.out.println("\nDeploy your ships:");
        for (int i = 1; i <= playerShips; i++) {
            System.out.print("Enter X Y coordinate for your " + i + " ship ");
            if (i == 1) {
                System.out.println("(4 decks): ");
                deployShip(4, true);
            } else if (i <= 3) {
                System.out.println("(3 decks): ");
                deployShip(3, true);
            } else if (i <= 6) {
                System.out.println("(2 decks): ");
                deployShip(2, true);
            } else {
                System.out.print("(1 deck): ");
                deployShip(1, true);
            }
        }
        playerMap.printOceanMap();
    }

    public void deployShip(int size, boolean isPlayer) {
        String[][] grid = isPlayer ? playerGrid : computerGrid;
        if (isPlayer) {
            Scanner input = new Scanner(System.in);
            int way = -1, x = -1, y = -1;
            ArrayList<String> buff = new ArrayList<>();
            Boolean[] ways = new Boolean[4];
            Arrays.fill(ways, true);
            do {
                x = input.nextInt();
                y = input.nextInt();
                if (size > 1) {
                    // up
                    if (x - size + 1 >= 0) {
                        for (int i = x - 1; i >= x - size + 1; i--)
                            if (!isAvailable(i, y, buff, true))
                                ways[0] = false;
                    } else ways[0] = false;
                    // right
                    if (y + size - 1 < numCols) {
                        for (int i = y + 1; i <= y + size - 1; i++)
                            if (!isAvailable(x, i, buff, true))
                                ways[1] = false;
                    } else ways[1] = false;
                    // down
                    if (x + size - 1 < numRows) {
                        for (int i = x + 1; i <= x + size - 1; i++)
                            if (!isAvailable(i, y, buff, true))
                                ways[2] = false;
                    } else ways[2] = false;
                    // left
                    if (y - size + 1 >= 0) {
                        for (int i = y - 1; i >= y - size + 1; i--)
                            if (!isAvailable(x, i, buff, true))
                                ways[3] = false;
                    } else ways[3] = false;
                }
                boolean correct = Arrays.asList(ways).contains(true);
                if (isAvailable(x, y, buff, true) && correct) {
                    buff.add("" + x + y);
                    grid[x][y] = "@";
                } else
                    System.out.println("Enter coordinate correct");
            } while (buff.isEmpty());
            if (size > 1) {
                int x2 = -1, y2 = -1;
                boolean correct = false;
                do {
                    x2 = input.nextInt();
                    y2 = input.nextInt();
                    boolean up, down, right, left;
                    if (isAvailable(x2, y2, buff, true)) {
                        up = x2 < x;
                        down = x2 > x;
                        right = y2 > y;
                        left = y2 < y;
                        correct = up && ways[0] || right && ways[1] || down && ways[2] || left && ways[3];
                    }
                    if (!correct)
                        System.out.println("Enter coordinate correct");
                } while (!correct);
                buff.add("" + x2 + y2);
                grid[x2][y2] = "@";
                for (int i = 0; i < size - 2; ) {
                    int x_ = input.nextInt();
                    int y_ = input.nextInt();
                    if (isAvailable(x_, y_, buff, true)) {
                        buff.add("" + x_ + y_);
                        grid[x_][y_] = "@";
                        i++;
                    } else
                        System.out.println("Retype your coords");
                }
            }
        } else {
            int way = -1, start_x = -1, start_y = -1;
            do {
                ArrayList<String> buff = new ArrayList<>();
                do {
                    start_x = (int) (Math.random() * 10);
                    start_y = (int) (Math.random() * 10);
                    if (isAvailable(start_x, start_y, buff, false)) {
                        buff.add("" + start_x + start_y);
                        grid[start_x][start_y] = "@";
                    }
                } while (buff.isEmpty());
                start_x = Character.getNumericValue(buff.get(0).charAt(0));
                start_y = Character.getNumericValue(buff.get(0).charAt(1));
                boolean[] ways = new boolean[4];
                Arrays.fill(ways, true);
                if (size > 1) {
                    // up
                    if (start_x - size + 1 >= 0) {
                        for (int i = start_x - 1; i >= start_x - size + 1; i--)
                            if (!isAvailable(i, start_y, buff, false))
                                ways[0] = false;
                    } else ways[0] = false;
                    // right
                    if (start_y + size - 1 < numCols) {
                        for (int i = start_y + 1; i <= start_y + size - 1; i++)
                            if (!isAvailable(start_x, i, buff, false))
                                ways[1] = false;
                    } else ways[1] = false;
                    // down
                    if (start_x + size - 1 < numRows) {
                        for (int i = start_x + 1; i <= start_x + size - 1; i++)
                            if (!isAvailable(i, start_y, buff, false))
                                ways[2] = false;
                    } else ways[2] = false;
                    // left
                    if (start_y - size + 1 >= 0) {
                        for (int i = start_y - 1; i >= start_y - size + 1; i--)
                            if (!isAvailable(start_x, i, buff, false))
                                ways[3] = false;
                    } else ways[3] = false;
                }
                way = getRandomWay(ways);
                if (way == -1)
                    grid[start_x][start_y] = " ";
            } while (way == -1);
            for (int i = 1; i <= size - 1; i++)
                switch (way) {
                    case 0:
                        grid[start_x - i][start_y] = "@";
                        break;
                    case 1:
                        grid[start_x][start_y + i] = "@";
                        break;
                    case 2:
                        grid[start_x + i][start_y] = "@";
                        break;
                    case 3:
                        grid[start_x][start_y - i] = "@";
                        break;
                }
        }
    }

    public int getRandomWay(boolean[] ways) {
        int way = -1;
        boolean isExist = false;
        for (boolean a : ways)
            if (a) {
                isExist = true;
                break;
            }
        do {
            int rnd = new Random().nextInt(ways.length);
            if (ways[rnd]) way = rnd;
        } while (way == -1 && isExist);
        return way;
    }

    public boolean isAvailable(int x, int y, ArrayList<String> ship, boolean isPlayer) {
        String[][] grid = isPlayer ? playerGrid : computerGrid;
        int count = 0;
        if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (grid[x][y].equals(" "))) {
            for (int i = x - 1; i <= x + 1; i++)
                for (int j = y - 1; j <= y + 1; j++)
                    if (i >= 0 && i < numRows && j >= 0 && j < numCols)
                        if (grid[i][j].equals("@")) {
                            count++;
                            if (ship.isEmpty()) {
                                return false;
                            } else {
                                AtomicBoolean isValid = new AtomicBoolean(true);
                                AtomicBoolean horizontal = new AtomicBoolean(true);
                                AtomicBoolean vertical = new AtomicBoolean(true);
                                final AtomicInteger[] minX = {new AtomicInteger(11)};
                                final AtomicInteger[] minY = {new AtomicInteger(11)};
                                final int[] maxX = {-1};
                                final int[] maxY = {-1};
                                ship.forEach((pair) -> {
                                    int x_ = Character.getNumericValue(pair.charAt(0));
                                    int y_ = Character.getNumericValue(pair.charAt(1));
                                    if (x_ < minX[0].get()) minX[0].set(x_);
                                    if (x_ > maxX[0]) maxX[0] = x_;
                                    if (y_ < minY[0].get()) minY[0].set(y_);
                                    if (y_ > maxY[0]) maxY[0] = y_;
                                    if (x_ != x) horizontal.set(false);
                                    if (y_ != y) vertical.set(false);
                                });
                                if (horizontal.get() && !vertical.get()) {
                                    if (minY[0].get() - y != 1 && y - maxY[0] != 1)
                                        isValid.set(false);
                                } else if (!horizontal.get() && vertical.get()) {
                                    if (minX[0].get() - x != 1 && x - maxX[0] != 1)
                                        isValid.set(false);
                                } else if (!horizontal.get() && !vertical.get())
                                    isValid.set(false);
                                if (!isValid.get()) {
                                    return false;
                                }
                            }
                        }
        } else if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && grid[x][y].equals("@")) {
            return false;
        } else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols)) {
            if (isPlayer)
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
            return false;
        }
        boolean correct;
        if (isPlayer)
            correct = (ship.size() == 0 && count == 0) || (ship.size() > 0 && count == 1);
        else
            correct = count <= 1;
        return correct;
    }

    public void deployComputerShips() {
        System.out.println("\nComputer is deploying ships");
        for (int i = 1; i <= computerShips; i++) {
            if (i == 1) {
                deployShip(4, false);
            } else if (i <= 3) {
                deployShip(3, false);
            } else if (i <= 6) {
                deployShip(2, false);
            } else {
                deployShip(1, false);
            }
        }
        computerMap.printOceanMap();
    }

    public void playerTurn() {
        System.out.println("\nYOUR TURN");
        int x = -1, y = -1;
        do {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter X Y coordinate: ");
            x = input.nextInt();
            y = input.nextInt();

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) //valid guess
            {
                switch (computerGrid[x][y]) {
                    case "@":
                        System.out.println("Boom! You hit the ship!");
                        //todo fix
                        computerGrid[x][y] = "!"; //Hit mark
                        if (isSunk(x, y, computerGrid)) {
                            fillSunkShip(x, y, computerGrid);
                            --computerShips;
                        }
                        break;
                    case " ":
                        System.out.println("Sorry, you missed");
                        computerGrid[x][y] = "-";
                        break;
                }
            } else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols))  //invalid guess
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        } while ((x < 0 || x >= numRows) || (y < 0 || y >= numCols));  //keep re-prompting till valid guess
    }

    public boolean isSunk(int x, int y, String[][] grid) {
        boolean horizontal = false, vertical = false;
        if ((x - 1 > 0 && !grid[x - 1][y].equals(" ") && !grid[x - 1][y].equals("x")) || (x + 1 < numRows && !grid[x + 1][y].equals(" ") && !grid[x + 1][y].equals("x")))
            vertical = true;
        if ((y - 1 > 0 && !grid[x][y - 1].equals(" ") && !grid[x][y - 1].equals("x")) || (y + 1 < numCols && !grid[x][y + 1].equals(" ") && !grid[x][y + 1].equals("x")))
            horizontal = true;
        if (!vertical && !horizontal) return true;
        String[] buff = new String[7];
        int index = 0;
        Arrays.fill(buff, " ");
        if (vertical) {
            // down check
            for (int i = x; i <= x + 3; i++)
                if (i >= 0 && i < numRows && !grid[i][y].equals(" ") && !grid[i][y].equals("x"))
                    buff[index++] = grid[i][y];
                else break;
            // up check
            for (int i = x - 1; i >= x - 3; i--)
                if (i >= 0 && i < numRows && !grid[i][y].equals(" ") && !grid[i][y].equals("x"))
                    buff[index++] = grid[i][y];
                else break;
        } else if (horizontal) {
            // down check
            for (int i = y; i <= y + 3; i++)
                if (i >= 0 && i < numCols && !grid[x][i].equals(" ") && !grid[x][i].equals("x"))
                    buff[index++] = grid[x][i];
                else break;
            // up check
            for (int i = y - 1; i >= y - 3; i--)
                if (i >= 0 && i < numCols && !grid[x][i].equals(" ") && !grid[x][i].equals("x"))
                    buff[index++] = grid[x][i];
                else break;
        }
        return !Arrays.asList(buff).contains("@");
    }

    public void fillSunkShip(int x, int y, String[][] grid) {
        boolean horizontal = false, vertical = false;
        if ((x - 1 > 0 && !grid[x - 1][y].equals(" ") && !grid[x - 1][y].equals("x")) || (x + 1 < numRows && !grid[x + 1][y].equals(" ") && !grid[x + 1][y].equals("x")))
            vertical = true;
        if ((y - 1 > 0 && !grid[x][y - 1].equals(" ") && !grid[x][y - 1].equals("x")) || (y + 1 < numCols && !grid[x][y + 1].equals(" ") && !grid[x][y + 1].equals("x")))
            horizontal = true;
        String[] buff = new String[4];
        if (!vertical && !horizontal)
            buff[0] = "" + x + y;
        int index = 0;
        if (vertical) {
            // down check
            for (int i = x; i <= x + 3; i++)
                if (i >= 0 && i < numRows && !grid[i][y].equals(" ") && !grid[i][y].equals("x"))
                    buff[index++] = "" + i + y;
                else break;
            // up check
            for (int i = x - 1; i >= x - 3; i--)
                if (i >= 0 && i < numRows && !grid[i][y].equals(" ") && !grid[i][y].equals("x"))
                    buff[index++] = "" + i + y;
                else break;
        } else if (horizontal) {
            // down check
            for (int i = y; i <= y + 3; i++)
                if (i >= 0 && i < numCols && !grid[x][i].equals(" ") && !grid[x][i].equals("x"))
                    buff[index++] = "" + x + i;
                else break;
            // up check
            for (int i = y - 1; i >= y - 3; i--)
                if (i >= 0 && i < numCols && !grid[x][i].equals(" ") && !grid[x][i].equals("x"))
                    buff[index++] = "" + x + i;
                else break;
        }
        Arrays.stream(buff).forEach((coords) -> {
            if (coords != null) {
                int x_ = Character.getNumericValue(coords.charAt(0));
                int y_ = Character.getNumericValue(coords.charAt(1));
                for (int i = x_ - 1; i <= x_ + 1; i++)
                    for (int j = y_ - 1; j <= y_ + 1; j++)
                        if (i >= 0 && j >= 0 && i < numRows && j < numCols)
                            grid[i][j] = "x";
            }
        });
    }

    public void computerTurn() {
        System.out.println("\nCOMPUTER'S TURN");
        //Guess co-ordinates
        int x = -1, y = -1;
        do {
            x = (int) (Math.random() * 10);
            y = (int) (Math.random() * 10);

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) //valid guess
            {
                switch (playerGrid[x][y]) {
                    case "@":
                        //if player ship is already there; player loses ship
                        System.out.println("The Computer hit one of your ships!");
                        playerGrid[x][y] = "!";
                        if (isSunk(x, y, playerGrid)) {
                            fillSunkShip(x, y, playerGrid);
                            --playerShips;
                        }
                        break;
                    case " ":
                        System.out.println("Computer missed");
                        //Saving missed guesses for computer
                        if (missedGuesses[x][y] != 1) {
                            playerGrid[x][y] = "-";
                            missedGuesses[x][y] = 1;
                        }
                        break;
                }
            }
        } while ((x < 0 || x >= numRows) || (y < 0 || y >= numCols));  //keep re-prompting till valid guess
    }

    public void gameOver() {
        System.out.println("Your decks: " + playerShips + " | Computer decks: " + computerShips);
        if (playerShips > 0 && computerShips <= 0)
            System.out.println("Hooray! You won the battle :)");
        else
            System.out.println("Sorry, you lost the battle");
        System.out.println();
    }
}
