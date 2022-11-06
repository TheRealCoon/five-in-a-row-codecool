package com.codecool.fiveinarow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Game game1 = new Game(1, 1);
    Game game2 = new Game(5, 5);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    void isValid_emptyInput_returnsFalse() {
        assertFalse(game1.isValid(""));
    }

    @Test
    void isValid_nullInput_returnsFalse() {
        assertFalse(game1.isValid(null));
    }

    @Test
    void isValid_wrongFormatInput_returnsFalse() {
        assertFalse(game1.isValid("1A"));
        assertFalse(game1.isValid("1a"));
        assertFalse(game1.isValid("AA1"));
        assertFalse(game1.isValid("AA"));
        assertFalse(game1.isValid("11"));
        assertFalse(game1.isValid("¤¤"));
        assertFalse(game1.isValid("??"));
        assertFalse(game1.isValid("captainYolo"));
    }

    @Test
    void isValid_coordinateOutsideOfBoard_returnsFalse() {
        assertFalse(game1.isValid("A0"));
        assertFalse(game1.isValid("A2"));
        assertFalse(game1.isValid("B1"));
        assertFalse(game2.isValid("A0"));
        assertFalse(game2.isValid("A6"));
        assertFalse(game2.isValid("F1"));
    }

    @Test
    void isValid_coordinateIsTaken_returnsFalse() {
        game1.setBoard(new int[][]{{1}});
        assertFalse(game1.isValid("A1"));
    }

    @Test
    void isValid_quitInput_returnsTrue() {
        assertTrue(game1.isValid("quit"));
        assertTrue(game1.isValid("Quit"));
        assertTrue(game1.isValid("QUIT"));
        assertTrue(game1.isValid("qUIT"));
    }

    @Test
    void isValid_validInput_returnsTrue() {
        assertTrue(game1.isValid("A1"));
        assertTrue(game2.isValid("A1"));
        assertTrue(game2.isValid("E5"));
    }

    @Test
    void convertToCoordinate_validInput_returnsExpectedCoordinate() {
        assertEquals(0, game1.convertToCoordinate("A1")[0]);
        assertEquals(0, game1.convertToCoordinate("A1")[1]);

        assertEquals(2, game2.convertToCoordinate("C3")[0]);
        assertEquals(2, game2.convertToCoordinate("C3")[1]);

        assertEquals(4, game2.convertToCoordinate("E5")[0]);
        assertEquals(4, game2.convertToCoordinate("E5")[1]);

        assertEquals(0, game2.convertToCoordinate("A5")[0]);
        assertEquals(4, game2.convertToCoordinate("A5")[1]);
    }

    @Test
    void getMove_validInput_outputRightInput() {
        String input = "A1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        int[] expected = new int[]{0, 0};
        int[] actual = game1.getMove(1);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
        assertEquals("Player1, enter your next move:", outContent.toString().trim());
    }

    @Test
    void getMove_invalidInput_throwsException_outputIsRight() {
        String input = "B1";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertThrows(NoSuchElementException.class, () -> game1.getMove(1));
        assertEquals("Player1, enter your next move:" + System.lineSeparator() +
                input + " is not a valid input or is taken! Please enter a valid coordinate!", outContent.toString().trim());
    }

    @Test
    void mark_invalidInput_doesNothing() {
        int[][] board = game1.getBoard();
        game1.mark(1, 2, 0);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                assertEquals(board[i][j], game1.getBoard()[i][j]);
            }
        }
    }

    @Test
    void mark_nullBoard_doesNothing() {
        game1.setBoard(null);
        game1.mark(1, 0, 0);
        assertNull(game1.getBoard());
    }

    @Test
    void mark_validInput_changesBoard() {
        game1.mark(1, 0, 0);
        assertEquals(1, game1.getBoard()[0][0]);

        game2.mark(2, 3, 3);
        assertEquals(2, game2.getBoard()[3][3]);
    }

    @Test
    void Constructor_oneInvalidDimension_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Game(2, 0));
        assertThrows(IllegalArgumentException.class, () -> new Game(0, 2));
        assertThrows(IllegalArgumentException.class, () -> new Game(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> new Game(0, -1));
    }

    @Test
    void Constructor_twoInvalidDimensions_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Game(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Game(-1, -1));
    }

    @Test
    void Constructor_validDimensions_boardCreatedAndRightSize() {
        Game game = new Game(1, 1);
        assertEquals(1, game.getBoard().length);
        assertEquals(1, game.getBoard()[0].length);
        game = new Game(3, 8);
        assertEquals(3, game.getBoard().length);
        assertEquals(8, game.getBoard()[0].length);
    }

    @Test
    void Constructor_validDimensions_noException() {
        assertDoesNotThrow(() -> new Game(1, 1));
    }

    @Test
    void hasWon_playerHasWonInFirstRow_returnsTrue() {
        int[][] board = new int[][]{
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        Game game = new Game(5, 5);
        game.setBoard(board);
        assertTrue(game.hasWon(1, 5));
        assertTrue(game.hasWon(1, 4));
        assertTrue(game.hasWon(1, 3));
        assertTrue(game.hasWon(1, 2));
        assertTrue(game.hasWon(1, 1));
    }

    @Test
    void hasWon_playerHasWonInMiddleRow_returnsTrue() {
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        Game game = new Game(5, 5);
        game.setBoard(board);
        assertTrue(game.hasWon(1, 5));
        assertTrue(game.hasWon(1, 4));
        assertTrue(game.hasWon(1, 3));
        assertTrue(game.hasWon(1, 2));
        assertTrue(game.hasWon(1, 1));
    }

    @Test
    void hasWon_playerHasWonInLastRow_returnsTrue() {
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1}};
        Game game = new Game(5, 5);
        game.setBoard(board);
        assertTrue(game.hasWon(1, 5));
        assertTrue(game.hasWon(1, 4));
        assertTrue(game.hasWon(1, 3));
        assertTrue(game.hasWon(1, 2));
        assertTrue(game.hasWon(1, 1));
    }

    @Test
    void hasWon_playerHasNotWon_returnsFalse() {
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 2, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        Game game = new Game(5, 5);
        game.setBoard(board);
        assertFalse(game.hasWon(1, 4));
        assertFalse(game.hasWon(1, 5));
        assertFalse(game.hasWon(2, 2));
    }
}
