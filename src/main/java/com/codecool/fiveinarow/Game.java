package com.codecool.fiveinarow;

import java.util.Scanner;

public class Game implements GameInterface {
    private int[][] board;

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
        String input;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your next move! ");
        while (!isValid(input = scanner.nextLine().toUpperCase())) {
            System.out.print(input + " is not a valid input or is taken! Please enter a valid coordinate! ");
        }
        return convertToCoordinate(input);
    }

    int[] convertToCoordinate(String input) {
        return new int[]{
                Integer.parseInt(input.substring(1)) - 1,
                Character.toUpperCase(input.charAt(0)) - 'A'};
    }

    boolean isValid(String input) {
        int minLength = 2;
        int maxLength = String.valueOf(board[0].length).length() + 1;
        if (input == null) return false;
        if (input.equalsIgnoreCase("quit")) return true;
        if (input.length() < minLength || input.length() > maxLength) return false;
        String y = input.substring(0, 1);
        int yIntValue = y.charAt(0) - 'A';
        if (yIntValue < 0 || yIntValue > board.length - 1) return false;
        String x = input.substring(1);
        return x.chars().allMatch(Character::isDigit) &&
                Integer.parseInt(x) >= 1 &&
                Integer.parseInt(x) <= board[0].length &&
                !isTaken(yIntValue, Integer.parseInt(x) - 1);
    }

    private boolean isTaken(int y, int x) {
        return board[y][x] != 0;
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
