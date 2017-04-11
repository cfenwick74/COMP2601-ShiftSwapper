package edu.carleton.COMP2601;
/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Server.java - Uses a Reactor pattern to dispatch messages for multi-client TTT game
 */

import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.communication.JSONEventSource;
import edu.carleton.COMP2601.communication.Reactor;
import edu.carleton.COMP2601.communication.ThreadWithReactor;
import edu.carleton.COMP2601.handlers.AssignShiftRequestHandler;
import edu.carleton.COMP2601.handlers.EmployeeScheduleRequestHandler;
import edu.carleton.COMP2601.handlers.FindAllEmployeesHandler;
import edu.carleton.COMP2601.handlers.MasterScheduleRequestHandler;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

public class Server {
    private ConcurrentHashMap<String, ThreadWithReactor> clients;
    private JSONEventSource source;
    private Reactor reactor;
    ThreadWithReactor twr;

    private static final int PORT = 1025;
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
        MasterScheduleRequestHandler masterScheduleRequestHandler = new MasterScheduleRequestHandler();
        AssignShiftRequestHandler assignShiftRequestHandler = new AssignShiftRequestHandler();
        FindAllEmployeesHandler findAllEmployeesHandler = new FindAllEmployeesHandler();
        reactor.register(Fields.CONNECT_REQUEST, connectRequestHandler);
        reactor.register(Fields.DISCONNECT_REQUEST, disconnectRequestHandler);
        reactor.register(Fields.SHIFT_SWAP_REQUEST, dispatchEventHandler);
        reactor.register(Fields.SHIFT, dispatchEventHandler);
        reactor.register(Fields.SHIFT_RELEASE_REQUEST, dispatchEventHandler);
        reactor.register(Fields.MASTER_SCHEDULE_REQUEST, masterScheduleRequestHandler);

        reactor.register(Fields.ASSIGN_SHIFT_REQUEST, assignShiftRequestHandler);
        reactor.register(Fields.UNASSIGN_SHIFT_REQUEST, assignShiftRequestHandler);

        reactor.register(Fields.FIND_ALL_EMPLOYEES_REQUEST, masterScheduleRequestHandler);

        reactor.register(Fields.EMP_SCHEDULE_REQUEST, new EmployeeScheduleRequestHandler());

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
        ShiftSwapRepository database = ShiftSwapRepository.getInstance();

        @Override
        public void handleEvent(Event event) {
            // TODO: add employee or change employee status to "connected"
            int answer = database.login(Integer.parseInt((String)event.get(Fields.ID)), (String)event.get(Fields.PASSWORD));
            System.out.println("in connect request handler. answer: " + answer);

            try {
                JSONObject jo = new JSONObject();
                jo.put(Fields.TYPE, Fields.CONNECTED_RESPONSE);
                jo.put(Fields.SOURCE, Fields.DATABASE);
                jo.put(Fields.DEST, ((JSONEvent) event).getSource());

                HashMap a = new HashMap();
                if(answer == Fields.USER_ISADMIN){
                    a.put(Fields.STATUS, Fields.TRUE);
                    a.put(Fields.IS_ADMIN, Fields.TRUE);
                } else if(answer == Fields.USER_ISEMPLOYEE){
                    a.put(Fields.STATUS, Fields.TRUE);
                    a.put(Fields.IS_ADMIN, Fields.FALSE);
                } else {
                    a.put(Fields.STATUS, Fields.FALSE);
                }


                Event e = new JSONEvent(jo,null,a);
                event.putEvent(e);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
