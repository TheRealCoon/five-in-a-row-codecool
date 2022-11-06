package com.codecool.fiveinarow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {
    Game game1 = new Game(1,1);
    Game game2 = new Game(5,5);
    Game game3 = new Game(1,1);
    Game game4 = new Game(1,1);

    @Test
    void isValid_emptyInput_returnsFalse(){
        assertFalse(game1.isValid(""));
    }
    @Test
    void isValid_nullInput_returnsFalse(){
        assertFalse(game1.isValid(null));
    }
    @Test
    void isValid_wrongFormatInput_returnsFalse(){
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
    void isValid_coordinateOutsideOfBoard_returnsFalse(){
        assertFalse(game1.isValid("A0"));
        assertFalse(game1.isValid("A2"));
        assertFalse(game1.isValid("B1"));
        assertFalse(game2.isValid("A0"));
        assertFalse(game2.isValid("A6"));
        assertFalse(game2.isValid("F1"));
    }

    @Test
    void isValid_coordinateIsTaken_returnsFalse(){
        game1.setBoard(new int[][]{{1}});
        assertFalse(game1.isValid("A1"));
    }
    @Test
    void isValid_quitInput_returnsTrue(){
        assertTrue(game1.isValid("quit"));
        assertTrue(game1.isValid("Quit"));
        assertTrue(game1.isValid("QUIT"));
        assertTrue(game1.isValid("qUIT"));
    }



}
