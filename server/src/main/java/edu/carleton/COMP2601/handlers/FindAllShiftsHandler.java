package edu.carleton.COMP2601.handlers;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.repository.Shift;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * Created by Pierre on 4/11/2017.
 */

public class FindAllShiftsHandler implements EventHandler {

    ShiftSwapRepository database = ShiftSwapRepository.getInstance();
    @Override
    public void handleEvent(Event event) {
        try {
            ArrayList<Shift> shifts = (ArrayList<Shift>) database.findAllShifts();
            for(Shift s: shifts){
                JSONObject jo = new JSONObject();
                jo.put(Fields.TYPE, Fields.MASTER_SCHEDULE_RESPONSE);
                jo.put(Fields.SOURCE, Fields.DATABASE);
                jo.put(Fields.DEST, ((JSONEvent) event).getSource());

                HashMap<String, Serializable> a = new HashMap();
                a.put(Fields.SHIFT, s);

                event.putEvent(new JSONEvent(jo,null,a));
            }
            event.putEvent(new Event(Fields.END_SHIFTS));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
