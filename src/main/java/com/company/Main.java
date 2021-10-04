package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = 10;

        System.out.println("**** Welcome to Battle Ships game ****");
        System.out.println("Right now, sea is empty\n");
        String input = "";
        do {
            System.out.print("Do you want to hide enemy ships? (yes/no): ");
            input = scanner.next();
        } while (!input.equals("yes") && !input.equals("no"));
        Battle battle = new Battle(size, input.equals("yes"));

        battle.deployPlayerShips();

        battle.deployComputerShips();

        do {
            battle.turn();
        } while (battle.getPlayerShips() != 0 && battle.getComputerShips() != 0);

        battle.gameOver();
    }
}
