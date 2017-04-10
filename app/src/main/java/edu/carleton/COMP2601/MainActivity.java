package edu.carleton.COMP2601;
/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * User List UI class
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.communication.JSONEventSource;
import edu.carleton.COMP2601.communication.Reactor;
import edu.carleton.COMP2601.communication.ThreadWithReactor;

public class MainActivity extends AppCompatActivity {
    EditText ip_text, port_text, name_text;
    Button connectBtn;
    Socket s;
    JSONEventSource e;
    ThreadWithReactor twr;

    static MainActivity instance;
    ProgressBar spinner;

    ArrayList<String> users = new ArrayList<>();

    String ipaddr = "192.168.1.110";
    int port = 1024;
    String name = "Pierre";
    String dest = "";

    protected Reactor r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        ip_text = (EditText) findViewById(R.id.edit_host);
        port_text = (EditText) findViewById(R.id.edit_port);
        name_text = (EditText) findViewById(R.id.edit_name);
        connectBtn = (Button) findViewById(R.id.connect_button);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        ip_text.setText(ipaddr);
        port_text.setText(String.valueOf(port));
    }

	/**
     * Connect button behaviour
     * @param connectBtn the button that was clicked
     */
    public void onClick(View connectBtn) {
        //TODO
        ipaddr = ip_text.getText().toString();
        port = Integer.parseInt(port_text.getText().toString());
        name = name_text.getText().toString();

        Thread connectThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    s = new Socket(ipaddr, port);
                    e = new JSONEventSource(s);
                    init();
                    twr = new ThreadWithReactor(e, r);

                    spinner.setIndeterminate(true);
                    JSONObject jo = new JSONObject();
                    jo.put("Type", Fields.CONNECT_REQUEST);
                    jo.put("Source", name);
                    jo.put("Dest", "");
                    jo.put("Message", "");
                    twr.start(new JSONEvent(jo, null));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        connectThread.start();
        spinner.setVisibility(View.VISIBLE);
    }

	/**
     * Initialize reactor and register connection handler
     */
    void init() {
        r = new Reactor();
        ConnectResponseHandler connectResponseHandler = new ConnectResponseHandler();
        r.register(Fields.CONNECTED_RESPONSE, connectResponseHandler);
    }

    void register(String type, EventHandler e) {
        twr.register(type, e);
    }

    public static MainActivity getInstance() {
        return instance;
    }

	/**
     * Sends an Event to the server
     * @param e the Event to send
     */
    void sendToServer(Event e) {
        if (twr != null) {
            try {
                ((JSONEventSource) twr.getEventSource()).putEvent(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

	/**
     *
     * @return  the name of the event sender
     */
    String getSource() {
        return name;
    }

	/**
     *
     * @return name of event recipient
     */
    String getDest() {
        return dest;
    }

    void setDest(String dest) {
        this.dest = dest;
    }

    @Override
    protected void onPause() {
        super.onPause();
        spinner.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinner.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            JSONObject jo = new JSONObject();
            jo.put("Type", Fields.DISCONNECT_REQUEST);
            jo.put("Source", name);
            jo.put("Dest", "");
            jo.put("Message", "");
            sendToServer(new JSONEvent(jo, null));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

	/**
     * Handler for connection requests
     * Launches the User List Activity
     */
    private class ConnectResponseHandler implements EventHandler {
        @Override
        public void handleEvent(Event event) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                }
            });

            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }

}
