package com.codecool.fiveinarow;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Game implements GameInterface {
    private int[][] board;

    public Game(int n, int m) {
        if (n > 0 && m > 0) {
            board = new int[n][m];
        } else throw new IllegalArgumentException("Board dimensions must be larger than 0!");
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[] getMove(int player) throws NoSuchElementException {
        String input;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player" + player + ", enter your next move:");
        while (!isValid(input = scanner.nextLine().toUpperCase())) {
            System.out.println(input + " is not a valid input or is taken! Please enter a valid coordinate!");
        }
        return convertToCoordinate(input);
    }

    int[] convertToCoordinate(String input) {
        return new int[]{
                input.charAt(0) - 'A',
                Integer.parseInt(input.substring(1)) - 1
        };
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
        if (board != null &&
                row >= 0 && col >= 0 &&
                row < board.length && col < board[0].length &&
                !isTaken(row, col)) {
            board[row][col] = player;
        }
    }

    public boolean hasWon(int player, int howMany) {
        for (int[] row : board) {
            int count = 0;
            for (int cell : row) {
                if (cell == player) count++;
                if (count >= howMany) return true;
            }
        }
        return false;
    }


    public boolean isFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
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
