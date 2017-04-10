package edu.carleton.COMP2601;
/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Tic-tac-toe game UI class
 */

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;


public class GameActivity extends AppCompatActivity {
    Button gameToggleButton;
    ImageButton[] tttButtons = new ImageButton[9];
    TextView textEdit;
    Drawable xImage;
    Drawable oImage;
    Game game;
    MainActivity instance;
    int PLAYER;
    int OPPONENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        xImage = ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_x);
        oImage = ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_o);
        gameToggleButton = (Button) findViewById(R.id.start_button);

        tttButtons[0] = (ImageButton) findViewById(R.id.tttButton1);
        tttButtons[1] = (ImageButton) findViewById(R.id.tttButton2);
        tttButtons[2] = (ImageButton) findViewById(R.id.tttButton3);
        tttButtons[3] = (ImageButton) findViewById(R.id.tttButton4);
        tttButtons[4] = (ImageButton) findViewById(R.id.tttButton5);
        tttButtons[5] = (ImageButton) findViewById(R.id.tttButton6);
        tttButtons[6] = (ImageButton) findViewById(R.id.tttButton7);
        tttButtons[7] = (ImageButton) findViewById(R.id.tttButton8);
        tttButtons[8] = (ImageButton) findViewById(R.id.tttButton9);
        setTTTButtons(false);

        textEdit = (TextView) findViewById(R.id.editText);
        textEdit.setText("");

        game = new Game();

        instance = MainActivity.getInstance();
        instance.register(Fields.MOVE_MESSAGE, new MoveReceived());
        instance.register(Fields.GAME_ON, new StartGameEvent());
        instance.register(Fields.GAME_OVER, new GameOverEvent());

        /**
         * Define behaviour for game toggle button. If there is no game in progress,
         * launches a new game, if there is an ongoing game, that game is terminated.
         */
        gameToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!game.gameInProgress()) {
                    resetBoard();
                    setTTTButtons(true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameToggleButton.setText(R.string.stop_text);
                        }
                    });

                    PLAYER = Game.PLAYER1;
                    OPPONENT = Game.PLAYER2;
                    game.setTurn(PLAYER);
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put("Type", Fields.GAME_ON);
                        jo.put("Source", instance.getSource());
                        jo.put("Dest", instance.getDest());
                        jo.put("Message", "");
                        instance.sendToServer(new JSONEvent(jo, null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                // game in progress; terminate current game
                else {
                    setTTTButtons(false);
                    game.endGame();
                    gameToggleButton.setText(R.string.start_text);
                    textEdit.setText(R.string.user_ended_text);

                    try {
                        JSONObject jo = new JSONObject();
                        jo.put("Type", Fields.GAME_OVER);
                        jo.put("Source", instance.getSource());
                        jo.put("Dest", instance.getDest());
                        jo.put("Message", instance.getSource() + getString(R.string.opponent_ended_text));
                        instance.sendToServer(new JSONEvent(jo, null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        //   Set click listener for tic-tac-toe board buttons
        for (int i = 0; i < tttButtons.length; i++) {
            final int finalI = i;
            tttButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tttClick(finalI);
                }
            });
        }
    }

    // Player click behaviour
    void tttClick(int button) {
        if (game.getTurn() == PLAYER) {
            game.playerMove(PLAYER, button);
            try {
                JSONObject jo = new JSONObject();
                jo.put("Type", Fields.MOVE_MESSAGE);
                jo.put("Source", instance.getSource());
                jo.put("Dest", instance.getDest());
                jo.put("Message", Integer.toString(button));
                instance.sendToServer(new JSONEvent(jo, null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateUi(PLAYER, button, Game.ONGOING);
        }
    }

    /**
     * @param enabled If true, enables all unoccupied buttons and disables occupied ones.
     *                If false, disables all buttons.
     */
    void setTTTButtons(final boolean enabled) {
        if (enabled) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] board = game.getBoard();
                    for (int i = 0; i < tttButtons.length; i++) {
                        if (board[i].equals(""))
                            tttButtons[i].setEnabled(true);
                        else
                            tttButtons[i].setEnabled(false);
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < tttButtons.length; i++) {
                        tttButtons[i].setEnabled(false);
                    }
                }
            });
        }
    }


    /**
     * Updates game board UI with last move played, and updates if there is a winner
     *
     * @param player the current player
     * @param move   the position of the button that was pressed
     * @param winner the current winner, if there is one
     */
    public void updateUi(final int player, final int move, final int winner) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textEdit.setText(getString(R.string.button_text) + move + getString(R.string.pressed_text) + ((player == OPPONENT) ? instance.getDest() : instance.getSource()));
                String[] board = game.getBoard();
                for (int i = 0; i < tttButtons.length; i++) {
                    if (!board[i].equals(""))
                        tttButtons[i].setImageDrawable(board[i].equals("o") ? oImage : xImage);
                }
                if (!(winner == Game.ONGOING)) {
                    updateWinner(winner);
                }
            }
        });
    }

    /**
     * Updates the game info field at the end of the game with result of the game (Player who won, or
     * draw if no winner)
     *
     * @param winner the game winner
     */
    void updateWinner(int winner) {
        setTTTButtons(false);
        try {
            JSONObject jo = new JSONObject();
            jo.put("Type", Fields.GAME_OVER);
            jo.put("Source", instance.getSource());
            jo.put("Dest", instance.getDest());
            if (winner == OPPONENT) {
                textEdit.setText(instance.getDest() + getString(R.string.opponent_winner));
                jo.put("Message", getString(R.string.player_winner));
            } else if (winner == PLAYER) {
                textEdit.setText(R.string.player_winner);
                jo.put("Message", instance.getSource() + getString(R.string.opponent_winner));
            } else if (winner == Game.NOONE) {
                textEdit.setText(getString(R.string.no_winner));
                jo.put("Message", getString(R.string.no_winner));
            }
            instance.sendToServer(new JSONEvent(jo, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameToggleButton.setText(R.string.start_text);
    }

    /**
     * Resets the game board tiles to start a new game
     */
    void resetBoard() {
        game.reset();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameToggleButton.setText(R.string.start_text);
                for (ImageButton tttButton : tttButtons) {
                    tttButton.setImageResource(0);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            JSONObject jo = new JSONObject();
            jo.put("Type", Fields.GAME_OVER);
            jo.put("Source", instance.getSource());
            jo.put("Dest", instance.getDest());
            jo.put("Message", instance.getSource() + getString(R.string.opponent_ended_text));
            instance.sendToServer(new JSONEvent(jo, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Move handler
     */
    private class MoveReceived implements EventHandler {
        @Override
        public void handleEvent(Event event) {
            if (game.getTurn() == OPPONENT) {
                int move = Integer.parseInt(((JSONEvent) event).getMessage());
                game.playerMove(OPPONENT, move);
                setTTTButtons(true);
                updateUi(OPPONENT, move, game.checkForWinner());
            }
        }
    }

    /**
     * Start game handler
     */
    private class StartGameEvent implements EventHandler {

        @Override
        public void handleEvent(Event event) {
            game.reset();
            resetBoard();
            PLAYER = Game.PLAYER2;
            OPPONENT = Game.PLAYER1;
            game.setTurn(OPPONENT);
            setTTTButtons(false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameToggleButton.setText(R.string.stop_text);
                    textEdit.setText(instance.getDest() + getString(R.string.game_started));
                }
            });
        }
    }

    /**
     * Game over handler
     */
    private class GameOverEvent implements EventHandler {

        @Override
        public void handleEvent(final Event event) {
            setTTTButtons(false);
            game.endGame();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameToggleButton.setText(R.string.start_text);
                    textEdit.setText(((JSONEvent) event).getMessage());
                }
            });
        }
    }
}
