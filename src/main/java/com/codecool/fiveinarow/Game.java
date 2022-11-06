package com.codecool.fiveinarow;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringJoiner;

public class Game implements GameInterface {
    private int[][] board;

    public Game(int n, int m) {
        if (n > 0 && m > 0) {
            board = new int[n][m];
        } else throw new IllegalArgumentException("Board dimensions and  must be larger than 0!");
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
        if (input.equalsIgnoreCase("quit")) quit();
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
        StringBuilder sb = new StringBuilder();
        char[] verticalHeader = getVerticalHeader();
        sb.append(getHorizontalHeader());
        for (int i = 0; i < board.length; i++) {
            sb.append(System.lineSeparator()).append("  ").append(verticalHeader[i]);
            for (int j = 0; j < board[0].length; j++) {
                char c = '.';
                switch (board[i][j]) {
                    case 1 -> c = 'X';
                    case 2 -> c = 'O';
                }
                sb.append("  ").append(c);
            }
        }
        sb.append(System.lineSeparator());
        System.out.println(sb);
    }

    private char[] getVerticalHeader() {
        char[] verticalHeader = new char[board.length];
        for (int i = 0; i < board.length; i++) {
            verticalHeader[i] = (char) ('A' + i);
        }
        return verticalHeader;
    }

    private String getHorizontalHeader() {
        StringJoiner sj = new StringJoiner("  ", "     ", "");
        for (int i = 0; i < board[0].length; i++) {
            sj.add(String.valueOf(i + 1));
        }
        return sj.toString();
    }

    public void printResult(int player) {
        String msg;
        if (player == 1) {
            msg = "X won!";
        } else if (player == 2) {
            msg = "O won!";
        } else {
            msg = "It's a tie!";
        }
        System.out.println(msg);
    }

    public void enableAi(int player) {
    }

    public void play(int howMany) {
    }

    private void quit() {
        System.out.println("Quiting...");
        System.exit(0);
    }
}
