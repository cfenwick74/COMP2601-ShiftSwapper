package edu.carleton.COMP2601.repository;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.communication.JSONEventSource;

/**
 * Created by Pierre on 4/10/2017.
 */

public class Handlers {

    /**
     * Handler for server connection request event
     */
    private class ConnectRequestHandler implements EventHandler {
        ShiftSwapRepository database = ShiftSwapRepository.getInstance();

        @Override
        public void handleEvent(Event event) {
            // TODO: add employee or change employee status to "connected"
//            int answer = database.login(Integer.parseInt((String)event.get(Fields.ID)), "1234");
            int answer = database.login(Integer.parseInt(((JSONEvent)event).getSource()), "1234");

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


}
