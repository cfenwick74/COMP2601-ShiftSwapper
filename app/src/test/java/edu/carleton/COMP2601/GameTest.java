package edu.carleton.COMP2601;

import org.junit.Before;
import org.junit.Test;

import static edu.carleton.COMP2601.Game.NOONE;
import static edu.carleton.COMP2601.Game.ONGOING;
import static edu.carleton.COMP2601.Game.PLAYER1;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Game.java
 * Carolyn Fenwick  100956658
 * Pierre Sequin
 */
public class GameTest {

    private Game game;
    private String[] testBoard;

    @Before
    public void setUp() {
        game = new Game();
        testBoard = game.getBoard();
        testBoard[0] = "x";
        testBoard[1] = "";
        testBoard[2] = "";
        testBoard[3] = "o";
        testBoard[4] = "o";
        testBoard[5] = "";
        testBoard[6] = "x";
        testBoard[7] = "";
        testBoard[8] = "";
    }

    @Test
    public void testResetBoard() throws Exception {
        game.reset();
        final String[] expected = new String[]{"", "", "", "", "", "", "", "", ""};
        assertArrayEquals(expected, testBoard);
    }

    @Test
    public void testGetBoard() throws Exception {
        final String[] expected = new String[]{"x", "", "", "o", "o", "", "x", "", ""};
        assertArrayEquals(expected, game.getBoard());
    }

    @Test
    public void testPlayer1Move() throws Exception {
        game.reset();
        game.playerMove(PLAYER1, 4);
        final String value = "x";
        final String[] expectedBoard = new String[]{"", "", "", "", "x", "", "", "", ""};
        assertEquals(value, testBoard[4]);
        assertArrayEquals(expectedBoard, game.getBoard());
    }

    @Test
    public void testPlayer2Move() throws Exception {
        game.reset();
        game.playerMove(game.PLAYER2, 7);
        final String value = "o";
        final String[] expectedBoard = new String[]{"", "", "", "", "", "", "", "o", ""};
        assertEquals(value, testBoard[7]);
        assertArrayEquals(expectedBoard, game.getBoard());
    }

    @Test
    public void testPlayerTwoPlay() throws Exception {
        String[] expected = game.getBoard().clone();
        assertArrayEquals(expected, game.getBoard());
    }

    @Test
    public void testCheckBoardWithWinnerOnTopRow() throws Exception {
        setUpWinningGameBoard("topRow");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithWinnerInMiddleRow() throws Exception {
        setUpWinningGameBoard("middleRow");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithWinnerInBottomRow() throws Exception {
        setUpWinningGameBoard("bottomRow");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithWinnerInLeftColumn() throws Exception {
        setUpWinningGameBoard("leftVert");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithWinnerInMiddleColumn() throws Exception {
        setUpWinningGameBoard("midVert");
        assertEquals(PLAYER1, game.checkForWinner());    }

    @Test
    public void testCheckBoardWithWinnerInRightColumn() throws Exception {
        setUpWinningGameBoard("rightVert");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithWinnerInLeftToRightDiagonal() throws Exception {
        setUpWinningGameBoard("ltrDiag");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithWinnerInRightToLeftDiagonal() throws Exception {
        setUpWinningGameBoard("rtlDiag");
        assertEquals(PLAYER1, game.checkForWinner());
    }

    @Test
    public void testCheckBoardWithNoWinner() throws Exception {
        setUpWinningGameBoard("noWinner");
        assertEquals(NOONE, game.checkForWinner());
    }

    @Test
    public void testCheckBoardInOngoingGame() throws Exception {
        setUpWinningGameBoard("");
        assertEquals(ONGOING, game.checkForWinner());
    }

    private void setUpWinningGameBoard(String location) {
        switch (location) {
            case "topRow":
                testBoard[0] = "x";
                testBoard[1] = "x";
                testBoard[2] = "x";
                testBoard[3] = "";
                testBoard[4] = "";
                testBoard[5] = "";
                testBoard[6] = "";
                testBoard[7] = "";
                testBoard[8] = "";
                break;
            case "middleRow":
                testBoard[0] = "";
                testBoard[1] = "";
                testBoard[2] = "";
                testBoard[3] = "x";
                testBoard[4] = "x";
                testBoard[5] = "x";
                testBoard[6] = "";
                testBoard[7] = "";
                testBoard[8] = "";
                break;
            case "bottomRow":
                testBoard[0] = "";
                testBoard[1] = "";
                testBoard[2] = "";
                testBoard[3] = "";
                testBoard[4] = "";
                testBoard[5] = "";
                testBoard[6] = "x";
                testBoard[7] = "x";
                testBoard[8] = "x";
                break;
            case "leftVert":
                testBoard[0] = "x";
                testBoard[1] = "";
                testBoard[2] = "";
                testBoard[3] = "x";
                testBoard[4] = "";
                testBoard[5] = "";
                testBoard[6] = "x";
                testBoard[7] = "";
                testBoard[8] = "";
                break;
            case "midVert":
                testBoard[0] = "";
                testBoard[1] = "x";
                testBoard[2] = "";
                testBoard[3] = "";
                testBoard[4] = "x";
                testBoard[5] = "";
                testBoard[6] = "";
                testBoard[7] = "x";
                testBoard[8] = "";
                break;
            case "rightVert":
                testBoard[0] = "";
                testBoard[1] = "";
                testBoard[2] = "x";
                testBoard[3] = "";
                testBoard[4] = "";
                testBoard[5] = "x";
                testBoard[6] = "";
                testBoard[7] = "";
                testBoard[8] = "x";
                break;
            case "ltrDiag":
                testBoard[0] = "x";
                testBoard[1] = "";
                testBoard[2] = "";
                testBoard[3] = "";
                testBoard[4] = "x";
                testBoard[5] = "";
                testBoard[6] = "";
                testBoard[7] = "";
                testBoard[8] = "x";
                break;
            case "rtlDiag":
                testBoard[0] = "";
                testBoard[1] = "";
                testBoard[2] = "x";
                testBoard[3] = "";
                testBoard[4] = "x";
                testBoard[5] = "";
                testBoard[6] = "x";
                testBoard[7] = "";
                testBoard[8] = "";
                break;
            case "noWinner":
                testBoard[0] = "x";
                testBoard[1] = "o";
                testBoard[2] = "x";
                testBoard[3] = "o";
                testBoard[4] = "o";
                testBoard[5] = "x";
                testBoard[6] = "x";
                testBoard[7] = "x";
                testBoard[8] = "o";
                break;
            default:    // game is ongoing
                testBoard[0] = "x";
                testBoard[1] = "";
                testBoard[2] = "";
                testBoard[3] = "";
                testBoard[4] = "o";
                testBoard[5] = "x";
                testBoard[6] = "";
                testBoard[7] = "";
                testBoard[8] = "";
        }
    }

}