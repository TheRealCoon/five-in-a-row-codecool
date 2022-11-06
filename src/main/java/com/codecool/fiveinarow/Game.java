package com.codecool.fiveinarow;

import java.util.Scanner;

public class Game implements GameInterface {
    private int[][] board;
    private final String pattern = "^[a-zA-Z]\\d+$";

    public Game(int n, int m) {
        board = new int[n][m];
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[] getMove(int player) {
        int[] coordinates = new int[2];
        String input;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Please enter your next move! ");
            while (!scanner.hasNext(pattern)) {
                System.out.println("That's not a valid Input!");
                scanner.next(pattern);
            }
            input = scanner.nextLine();
        } while (isValid(input));

        return convertToCoordinate(input);
    }

    public int[] getAiMove(int player) {
        return null;
    }

    public void mark(int player, int row, int col) {
    }

    public boolean hasWon(int player, int howMany) {
        return false;
    }

    public boolean isFull() {
        return false;
    }

    public void printBoard() {
    }

    public void printResult(int player) {
    }

    public void enableAi(int player) {
    }

    public void play(int howMany) {
    }

}
