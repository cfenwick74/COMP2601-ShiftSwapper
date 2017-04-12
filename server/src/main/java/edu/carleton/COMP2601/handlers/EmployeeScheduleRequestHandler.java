package edu.carleton.COMP2601.handlers;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.repository.Shift;
import edu.carleton.COMP2601.repository.ShiftChangeRequest;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Handles a request for an Employee's schedule
 */

public class EmployeeScheduleRequestHandler implements EventHandler {
    ShiftSwapRepository database = ShiftSwapRepository.getInstance();
    @Override
    public void handleEvent(Event event) {
        ArrayList<Shift> shifts = (ArrayList<Shift>) database.findShiftsForEmployee(Integer.parseInt(((JSONEvent)event).getSource()));
        ArrayList<ShiftChangeRequest> requests = (ArrayList<ShiftChangeRequest>) database.findRequestedShiftChangesForEmployee(Integer.parseInt(((JSONEvent)event).getSource()));
        try {
            JSONObject jo = new JSONObject();
            jo.put(Fields.TYPE, Fields.EMP_SCHEDULE_RESPONSE);
            jo.put(Fields.SOURCE, Fields.DATABASE);
            jo.put(Fields.DEST, ((JSONEvent) event).getSource());

            HashMap<String, Serializable> a = new HashMap();
            a.put(Fields.SHIFT, shifts);
            a.put(Fields.REQUESTORS_SHIFT, requests);
            event.putEvent(new JSONEvent(jo,null,a));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
