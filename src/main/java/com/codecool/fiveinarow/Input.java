package com.codecool.fiveinarow;

import java.util.Scanner;

public class Input {
    private Scanner scanner;

    public Input() {
        scanner = new Scanner(System.in);
    }

    public String readInput(String msg) {
        System.out.println(msg);
        return scanner.nextLine();
    }
}
