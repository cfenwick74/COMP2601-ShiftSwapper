package edu.carleton.COMP2601;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.JSONEvent;

public class UserListActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter adapter;
    TextView gameInfo;
    private String myName;
    private ArrayList list = new ArrayList();
    MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        instance = MainActivity.getInstance();

        listView = (ListView) findViewById(R.id.listView);
        gameInfo = (TextView) findViewById(R.id.game_info);

        myName = instance.getSource();

//        instance.register(Fields.USERS_UPDATED, new ListUpdateHandler());
//        instance.register(Fields.PLAY_GAME_RESPONSE, new PlayGameResponseHandler());
//        instance.register(Fields.PLAY_GAME_REQUEST, new PlayGameRequestHandler());
        this.adapter = new ArrayAdapter<>(this, R.layout.list_row, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

	    /**
         * Behaviour for click on a user in the user list
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("clicked list item " + position);
                instance.setDest(((AppCompatTextView)view).getText().toString());
                Thread connectThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jo = new JSONObject();
//                            jo.put("Type", Fields.PLAY_GAME_REQUEST);
                            jo.put("Source", instance.getSource());
                            jo.put("Dest", instance.getDest());
                            jo.put("Message", "");
                            instance.sendToServer(new JSONEvent(jo, null, new HashMap<String, Serializable>()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                connectThread.start();
            }
        });
    }

	/**
     * Handler for play game request
     * Launches dialog on user's UI when someone asks to play a game with them
     * If yes - launch Game activity, send response to requestor
     * If no - close dialog, update text field on requestor's side saying user doesn't want to play
     */
    private class PlayGameRequestHandler implements EventHandler {
        @Override
        public void handleEvent(final Event event) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog dialog = new AlertDialog.Builder(UserListActivity.this).create();
                    dialog.setTitle(((JSONEvent) event).getSource() + getString(R.string.wants_to_play));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.accept),
                            new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            instance.setDest(((JSONEvent) event).getSource());
                            try {
                                JSONObject jo = new JSONObject();
//                                jo.put("Type", Fields.PLAY_GAME_RESPONSE);
                                jo.put("Source", instance.getSource());
                                jo.put("Dest", ((JSONEvent) event).getSource());
                                jo.put("Message", true);
                                instance.sendToServer(new JSONEvent(jo, null, new HashMap<String, Serializable>()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(UserListActivity.this, GameActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.reject),
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        JSONObject jo = new JSONObject();
//                                        jo.put("Type", Fields.PLAY_GAME_RESPONSE);
                                        jo.put("Source", instance.getSource());
                                        jo.put("Dest", ((JSONEvent) event).getSource());
                                        jo.put("Message", false);
                                        instance.sendToServer(new JSONEvent(jo, null, new HashMap<String, Serializable>()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    dialog.show();
               }
            });
        }
    }

	/**
     * Handler for play game response
     * If accepted, launches Game activity
     * If rejected, updates text view with rejection message
     */
    private class PlayGameResponseHandler implements EventHandler {
        @Override
        public void handleEvent(final Event event) {
//            boolean playStatus = Boolean.valueOf(((JSONEvent)event).getMessage());
//            if (playStatus){
//                Intent intent = new Intent(UserListActivity.this, GameActivity.class);
//                startActivity(intent);
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        gameInfo.setText(((JSONEvent) event).getSource() + getString(R.string.reject_game));
//                    }
//                });
//            }
        }
    }

	/**
     * Updates user list view when a user connects/disconnects
     */
    private class ListUpdateHandler implements EventHandler {
        @Override
        public void handleEvent(Event event) {
//            String e = ((JSONEvent) event).getMessage();
//            list.clear();
//            try {
//                JSONArray jUsers = new JSONArray(e);
//                if (jUsers != null) {
//                    for (int i = 0; i < jUsers.length(); i++) {
//                        if (!myName.equals(jUsers.getString(i)))
//                            list.add(jUsers.getString(i));
//                    }
//                }
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    adapter.notifyDataSetChanged();
//                }
//            });
        }
    }


}
