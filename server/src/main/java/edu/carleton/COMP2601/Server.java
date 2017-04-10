package edu.carleton.COMP2601;
/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Server.java - Uses a Reactor pattern to dispatch messages for multi-client TTT game
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.communication.JSONEventSource;
import edu.carleton.COMP2601.communication.Reactor;
import edu.carleton.COMP2601.communication.ThreadWithReactor;

import static edu.carleton.COMP2601.communication.JSONEvent.*;

public class Server {
    private ConcurrentHashMap<String, ThreadWithReactor> clients;
    private JSONEventSource source;
    private Reactor reactor;
    ThreadWithReactor twr;

    private static final int PORT = 1024;
    private static ServerSocket listener;

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        server.run();
    }

    /**
     * Creates a new {@link ThreadWithReactor} (client connection) for each incoming
     * {@link Socket} connection
     */
    private void run() {
        try {
            while (true) {
                Socket socket = listener.accept();
                source = new JSONEventSource(socket);
                twr = new ThreadWithReactor(source, reactor);
                twr.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs on server startup - create client list, open {@link ServerSocket}, and register event
     * handlers to {@link Reactor}
     */
    private void init() {
        clients = new ConcurrentHashMap<>();
        reactor = new Reactor();
        DispatchEventHandler dispatchEventHandler = new DispatchEventHandler();
        ConnectRequestHandler connectRequestHandler = new ConnectRequestHandler();
        DisconnectRequestHandler disconnectRequestHandler = new DisconnectRequestHandler();
        reactor.register(Fields.CONNECT_REQUEST, connectRequestHandler);
        reactor.register(Fields.DISCONNECT_REQUEST, disconnectRequestHandler);
        reactor.register(Fields.SHIFT_SWAP, dispatchEventHandler);
        reactor.register(Fields.SHIFT, dispatchEventHandler);
        reactor.register(Fields.SHIFT_RELEASE, dispatchEventHandler);

        try {
            listener = new ServerSocket(PORT);
            System.out.println("Listening...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Dispatches event from the source to the destination
     */
    private class DispatchEventHandler implements EventHandler {
        @Override
        public void handleEvent(Event event) {
            try {
                ((JSONEventSource) clients.get(((JSONEvent) event).getDest()).getEventSource()).putEvent(event);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Handler for server connection request event
     */
    private class ConnectRequestHandler implements EventHandler {
        @Override
        public void handleEvent(Event event) {
            // TODO: add employee or change employee status to "connected"
        }
    }

    /**
     * Handler for disconnect request event
     */
    private class DisconnectRequestHandler implements EventHandler {

        @Override
        public void handleEvent(Event event) {
          // TODO: change employee status to "not connected"
        }

    }
}
