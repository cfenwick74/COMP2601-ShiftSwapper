package edu.carleton.COMP2601;

/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Tic-tac-toe game logic  class
 */

class Game {
    static final int PLAYER1 = 0;
    static final int PLAYER2 = 1;
    static final int NOONE = 10;
    static final int ONGOING = -1;

    private String[] board = new String[9];
    private int turn = PLAYER1;
    private boolean gameStarted;

    Game() {
    }

    String[] getBoard() {
        return board;
    }

    /**
     * Reset game board to start a new {@link Game}
     */
    void reset() {
        gameStarted = true;
        for (int i = 0; i < board.length; i++) {
            board[i] = "";
        }
    }

	/**
	 * Places an 'x' or an 'o' in the square clicked by the player
	 *
	 * @param player the player who clicked the square
	 * @param i      the square that was clicked
	 */
	void playerMove(int player, int i) {
        if (player == PLAYER1) {
            board[i] = "x";
            turn = PLAYER2;
        } else if (player == PLAYER2) {
            board[i] = "o";
            turn = PLAYER1;
        }
    }

    /**
     * Check to see if square is occupied (i.e. not ""). If it is, check the squares that would
     * form a winning line; return the winner if the squares match
     *
     * @return The winning player ID (0=x, 1=0), -10 if the board is full and there is no winner,
     * -1 for if game is ongoing
     */
    int checkForWinner() {
        if (!board[0].equals("")) {
            if ((board[0].equals(board[1]) && board[1].equals(board[2])) ||     // top horizontal
                    board[0].equals(board[3]) && board[3].equals(board[6]) ||   // left vertical line
                    board[0].equals(board[4]) && board[4].equals(board[8]))     // LTR diagonal
                return ((board[0].equals("x")) ? PLAYER1 : PLAYER2);
        }

        // center vertical line
        if (!board[1].equals("") && board[1].equals(board[4]) && board[4].equals(board[7]))
            return ((board[1].equals("x")) ? PLAYER1 : PLAYER2);


        if (!board[2].equals("")) {
            if ((board[2].equals(board[5]) && board[5].equals(board[8])) ||    // right vertical line
                    board[2].equals(board[4]) && board[4].equals(board[6]))     // RTL diagonal
                return ((board[2].equals("x")) ? PLAYER1 : PLAYER2);
        }

        // centre horizontal line
        if (!board[3].equals("") && board[3].equals(board[4]) && board[4].equals(board[5]))
            return ((board[3].equals("x")) ? PLAYER1 : PLAYER2);

        // bottom horizontal line
        if (!board[6].equals("") && board[6].equals(board[7]) && board[7].equals(board[8]))
            return ((board[6].equals("x")) ? PLAYER1 : PLAYER2);

        // return empty string if game is ongoing
        for (String aBoard : board) {
            if (aBoard.equals(""))
                return ONGOING;
        }
        return NOONE;
    }

    /**
     * Checks if there is an ongoing game
     *
     * @return true if ongoing game; false otherwise
     */
    boolean gameInProgress() {
        return gameStarted && checkForWinner() == ONGOING;
    }


    /**
     * Gets the current player
     *
     * @return 0 if 'x', 1 if 'o'
     */
    int getTurn() {
        return turn;
    }

    /**
     * Sets the current player
     *
     * @param turn player whose turn it is
     */
    void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Ends a game when the game toggle button is pressed during an ongoing game
     */
    void endGame() {
        gameStarted = false;
    }
}
