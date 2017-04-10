package edu.carleton.COMP2601.communication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import edu.carleton.COMP2601.MainActivity;

/**
 * Created by Pierre on 4/10/2017.
 */

public class AcceptorReactor {
    protected Reactor r;
    Socket s;
    JSONEventSource e;
    ThreadWithReactor twr;
    static AcceptorReactor instance = new AcceptorReactor();
    String addr;
    int port;
    public static AcceptorReactor getInstance(){return instance;}

    private AcceptorReactor(){
        r = new Reactor();
    }

    public void init(int port, String addr){
        this.port = port;
        this.addr = addr;
    }

    public void register(String type, EventHandler e) {
        r.register(type, e);
    }

    public void start(final Event event){
        Thread connectThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    s = new Socket(addr, port);
                    e = new JSONEventSource(s);
                    twr = new ThreadWithReactor(e, r);
                    twr.start(event);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        connectThread.start();
    }

    /**
     * Sends an Event to the server
     * @param e the Event to send
     */
    public void put(Event e) {
        if (twr != null) {
            try {
                ((JSONEventSource) twr.getEventSource()).putEvent(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
