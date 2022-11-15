package com.codecool.fiveinarow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class GameTest {

    private final String HOW_MANY_3 = "3";
    private final String HOW_MANY_5 = "5";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    Input mockInput = Mockito.mock(Input.class);

    {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_3);
    }

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);

    }


    @Test
    void isValid_emptyInput_returnsFalse() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        assertFalse(game1.isValid(""));
    }

    @Test
    void isValid_nullInput_returnsFalse() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        assertFalse(game1.isValid(null));
    }

    @Test
    void isValid_wrongFormatInput_returnsFalse() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
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
    void isValid_coordinateOutsideOfBoard_returnsFalse() throws IOException {
        Game game2 = new Game(5, 5, mockInput);
        assertFalse(game2.isValid("A0"));
        assertFalse(game2.isValid("A6"));
        assertFalse(game2.isValid("F1"));
    }

    @Test
    void isValid_coordinateIsTaken_returnsFalse() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        game1.setBoard(new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        assertFalse(game1.isValid("A1"));
    }

    @Test
    void isValid_quitInput_returnsTrue() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        assertTrue(game1.isValid("quit"));
        assertTrue(game1.isValid("Quit"));
        assertTrue(game1.isValid("QUIT"));
        assertTrue(game1.isValid("qUIT"));
    }

    @Test
    void isValid_validInput_returnsTrue() throws IOException {
        Game game2 = new Game(5, 5, mockInput);
        assertTrue(game2.isValid("A1"));
        assertTrue(game2.isValid("E5"));
    }

    @Test
    void convertToCoordinate_validInput_returnsExpectedCoordinate() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        Game game2 = new Game(5, 5, mockInput);

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
    void getMove_validInput_outputRightInput() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        when(mockInput.readInput("Player1, enter your next move:")).thenReturn("A1");
        int[] expected = new int[]{0, 0};
        int[] actual = game1.getMove(1);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }


    @Test
    void getMove_invalidInput_throwsException_outputIsRight() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        String input = "D1";
        when(mockInput.readInput("Player1, enter your next move:")).thenReturn(input);
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> game1.getMove(1));
        assertEquals(input + " is not a valid input or is taken! Please enter a valid coordinate!", e.getMessage());
    }

    @Test
    void mark_invalidInput_doesNothing() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        int[][] board = game1.getBoard();
        game1.mark(1, 2, 0);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                assertEquals(board[i][j], game1.getBoard()[i][j]);
            }
        }
    }

    @Test
    void mark_nullBoard_doesNothing() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        game1.setBoard(null);
        game1.mark(1, 0, 0);
        assertNull(game1.getBoard());
    }

    @Test
    void mark_validInput_changesBoard() throws IOException {
        Game game1 = new Game(3, 3, mockInput);
        game1.mark(1, 0, 0);
        assertEquals(1, game1.getBoard()[0][0]);

        Game game2 = new Game(5, 5, mockInput);
        game2.mark(2, 3, 3);
        assertEquals(2, game2.getBoard()[3][3]);
    }

    @Test
    void Constructor_oneInvalidDimension_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Game(2, 0, mockInput));
        assertThrows(IllegalArgumentException.class, () -> new Game(0, 2, mockInput));
        assertThrows(IllegalArgumentException.class, () -> new Game(-1, 2, mockInput));
        assertThrows(IllegalArgumentException.class, () -> new Game(0, -1, mockInput));
    }

    @Test
    void Constructor_twoInvalidDimensions_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Game(0, 0, mockInput));
        assertThrows(IllegalArgumentException.class, () -> new Game(-1, -1, mockInput));
    }

    @Test
    void Constructor_validDimensions_boardCreatedAndRightSize() throws IOException {
        Game game = new Game(3, 3, mockInput);
        assertEquals(3, game.getBoard().length);
        assertEquals(3, game.getBoard()[0].length);

        game = new Game(3, 8, mockInput);
        assertEquals(3, game.getBoard().length);
        assertEquals(8, game.getBoard()[0].length);
    }

    @Test
    void Constructor_validDimensions_noException() throws IOException {
        assertDoesNotThrow(() -> new Game(3, 3, mockInput));
    }

    @Test
    void hasWon_playerHasWonInFirstRow_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }


    @Test
    void hasWon_playerHasWonInMiddleRow_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWonInLastRow_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWonInFirstColumn_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWonInMiddleColumn_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWonInLastColumn_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWonDiagonallyDown_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_01_DiagonallyDownBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_02_DiagonallyDownBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_03_DiagonallyDownBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_04_DiagonallyDownBigBoard_returnsTrue() throws IOException {
        Game game = new Game(10, 8, mockInput);
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_05_DiagonallyDownBigBoard_returnsTrue() throws IOException {
        Game game = new Game(10, 8, mockInput);
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_06_DiagonallyDownBigBoard_returnsTrue() throws IOException {
        Game game = new Game(10, 8, mockInput);
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_01_DiagonallyUpBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_02_DiagonallyUpBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_03_DiagonallyUpBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_04_DiagonallyUpBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasWon_05_DiagonallyUpBigBoard_returnsTrue() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertTrue(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_01_HorizontallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_02_HorizontallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 1, 1}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_03_HorizontallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(10, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_04_HorizontallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_05_HorizontallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_06_HorizontallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_01_VerticallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_02_VerticallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_03_VerticallyBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_01_DiagonallyDownBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 12, mockInput);
        int[][] board = new int[][]{
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_02_DiagonallyDownBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 12, mockInput);
        int[][] board = new int[][]{
                {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_01_DiagonallyUpBigBoard_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(12, 8, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1},
                {0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0},
                {0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0},
                {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
    }

    @Test
    void hasWon_playerHasNotWon_returnsFalse() throws IOException {
        when(mockInput
                .readInput("How many marks in a row are needed for win?"))
                .thenReturn(HOW_MANY_5);
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 2, 1},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        game.setBoard(board);
        assertFalse(game.hasWon(1));
        assertFalse(game.hasWon(2));
    }


    @Test
    void isFull_boardIsNotFull_returnsFalse() throws IOException {
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {1, 2, 2, 2, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 2, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}};
        game.setBoard(board);
        assertFalse(game.isFull());
    }

    @Test
    void isFull_boardIsFull_returnsTrue() throws IOException {
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {1, 2, 2, 2, 1},
                {1, 2, 2, 2, 1},
                {1, 1, 1, 2, 1},
                {1, 2, 2, 2, 1},
                {1, 1, 1, 1, 1}};
        game.setBoard(board);
        assertTrue(game.isFull());
    }

    @Test
    void printBoard_boardIsFull_printsBoardAsExpected() throws IOException {
        Game game = new Game(5, 5, mockInput);
        int[][] board = new int[][]{
                {1, 2, 2, 2, 1},
                {1, 2, 2, 2, 1},
                {1, 1, 1, 2, 1},
                {1, 2, 2, 2, 1},
                {1, 1, 1, 1, 1}};
        game.setBoard(board);
        game.printBoard();
        assertEquals(
//                "How many marks in a row are needed for win?" + System.lineSeparator() +
                        "     1  2  3  4  5" + System.lineSeparator() +
                        "  A  X  O  O  O  X" + System.lineSeparator() +
                        "  B  X  O  O  O  X" + System.lineSeparator() +
                        "  C  X  X  X  O  X" + System.lineSeparator() +
                        "  D  X  O  O  O  X" + System.lineSeparator() +
                        "  E  X  X  X  X  X" + System.lineSeparator() +
                        System.lineSeparator(), outContent.toString());
    }

    @Test
    void printBoard_boardIsEmpty_printsDots() throws IOException {
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        Game game = new Game(5, 5, mockInput);
        game.setBoard(board);
        game.printBoard();
        assertEquals(
//                "How many marks in a row are needed for win?" + System.lineSeparator() +
                        "     1  2  3  4  5" + System.lineSeparator() +
                        "  A  .  .  .  .  ." + System.lineSeparator() +
                        "  B  .  .  .  .  ." + System.lineSeparator() +
                        "  C  .  .  .  .  ." + System.lineSeparator() +
                        "  D  .  .  .  .  ." + System.lineSeparator() +
                        "  E  .  .  .  .  ." + System.lineSeparator() +
                        System.lineSeparator(), outContent.toString());
    }
}
