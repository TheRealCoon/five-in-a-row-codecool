package com.codecool.fiveinarow;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringJoiner;

public class Game implements GameInterface {
    private int[][] board;
    private final int howMany;
    private final int MIN_HOW_MANY = 3;

    public Game(int n, int m) {
        if (n >= MIN_HOW_MANY && m >= MIN_HOW_MANY) {
            board = new int[n][m];
        } else throw new IllegalArgumentException("Board dimensions must be equal or greater than 3!");
        howMany = getHowManyValueFromUser();
    }

    public int getHowManyValueFromUser() {
        String input;
        int number = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many marks in a row are needed for win?");
        while (!isValidNumber(input = scanner.nextLine())) {
            System.out.println(input + " is not a valid number! Input must be a number and greater than " + MIN_HOW_MANY + "!");
            if (input.equalsIgnoreCase("quit")) quit();
        }
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Couldn't convert '" + input + "' into a number!");
        }
        scanner.close();
        return number;
    }

    private boolean isValidNumber(String text) {
        int number;
        if (text == null) {
            return false;
        }
        try {
            number = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return number >= MIN_HOW_MANY && number <= Math.max(board.length, board[0].length);
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
        scanner.close();
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

    public boolean hasWon(int player) {
        return hasWonHorizontally(player) || hasWonVertically(player) || hasWonDiagonallyUp(player) || hasWonDiagonallyDown(player);
    }

    private boolean hasWonDiagonallyDown(int player) {
        for (int v = 0; v < board.length - howMany + 1; v++) {
            for (int h = 0; h < board[0].length - howMany + 1; h++) {
                if (board[v][h] == player) {
                    int counter = 1;
                    int maxK = Math.min(board.length - v, board[0].length - h);
                    for (int k = 1; k < maxK; k++) {
                        if (board[v + k][h + k] == player) {
                            counter++;
                        } else {
                            break;
                        }
                    }
                    if (counter == howMany) return true;
                }
            }
        }
        return false;
    }

    private boolean hasWonDiagonallyUp(int player) {
        for (int v = board.length - 1; v >= howMany - 1; v--) {
            for (int h = 0; h < board[0].length - howMany + 1; h++) {
                if (board[v][h] == player) {
                    int counter = 1;
                    int maxK = Math.min(v + 1, board[0].length - h);
                    for (int k = 1; k < maxK; k++) {
                        if (board[v - k][h + k] == player) {
                            counter++;
                        } else {
                            break;
                        }
                    }
                    if (counter == howMany) return true;
                }
            }
        }
        return false;
    }

    private boolean hasWonHorizontally(int player) {
        for (int v = 0; v < board.length; v++) {
            for (int h = 0; h < board[0].length - howMany + 1; h++) {
                if (board[v][h] == player) {
                    int counter = 1;
                    int maxK = Math.min(h + howMany + 1, board[0].length);
                    for (int k = h + 1; k < maxK; k++) {
                        h = k;
                        if (board[v][k] == player) {
                            counter++;
                        } else {
                            break;
                        }
                    }
                    if (counter == howMany) return true;
                }
            }
        }
        return false;
    }

    private boolean hasWonVertically(int player) {
        for (int h = 0; h < board[0].length; h++) {
            for (int v = 0; v < board.length - howMany + 1; v++) {
                if (board[v][h] == player) {
                    int counter = 1;
                    int maxK = Math.min(v + howMany, board.length);
                    for (int k = v + 1; k < maxK; k++) {
                        v = k;
                        if (board[k][h] == player) {
                            counter++;
                        } else {
                            break;
                        }
                    }
                    if (counter == howMany) return true;
                }
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

    public void play() {

    }

    private void quit() {
        System.out.println("Quiting...");
        System.exit(0);
    }
}
