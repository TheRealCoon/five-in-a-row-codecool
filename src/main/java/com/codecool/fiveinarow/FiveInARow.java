package com.codecool.fiveinarow;

public class FiveInARow {
    private static int rows;
    private static int columns;
    private static final Input input = new Input();


    public static void main(String[] args) {
        displayTitle();
        MainMenu();
    }

    private static void MainMenu() {
        int menuIndex = -1;
        while (menuIndex != 0) {
            displayMainMenu();
            String userInput = input.readInput("");
            try {
                menuIndex = Integer.parseInt(userInput);
                loadMenuElement(menuIndex);
            } catch (NumberFormatException e) {
                System.out.println("'" + userInput + "' is not a number!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private static void loadMenuElement(int menuIndex) {
        switch (menuIndex) {
            case 0 -> {
                return;
            }
            case 1 -> startGame();
        }
    }

    private static void setUpGame() {
        System.out.println("Board Set Up:");
        rows = getBoardSizeFromUser("rows");
        System.out.print("");
        columns = getBoardSizeFromUser("columns");
    }

    private static int getBoardSizeFromUser(String text) {
        int number = 0;
        while (number < 3) {
            String userInput = null;
            try {
                userInput = input.readInput("How many " + text + " do you want? (at least 3)");
                number = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("'" + userInput + "' is not a number!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return number;
    }


    private static void startGame() {
        setUpGame();
        Game game = new Game(rows, columns);
        game.play(); //comment out if you want to show the "NoSuchElementException to mentors"
    }

    private static void displayMainMenu() {
        String[] menu = new String[]{
                "Exit",
                "New Game"
        };
        System.out.println("Main Menu:");
        for (int i = 1; i < menu.length; i++) {
            System.out.println(i + " - " + menu[i]);
        }
        System.out.println(0 + " - " + menu[0]);
    }

    private static void displayTitle() {
        System.out.println("*****************************");
        System.out.println("* F I V E - I N - A - R O W *");
        System.out.println("*****************************");
    }
}
