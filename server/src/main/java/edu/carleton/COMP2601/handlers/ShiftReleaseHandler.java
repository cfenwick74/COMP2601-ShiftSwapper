package edu.carleton.COMP2601.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.repository.Employee;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * Created by Pierre on 4/10/2017.
 */

public class ShiftReleaseHandler implements EventHandler {

    ShiftSwapRepository database = ShiftSwapRepository.getInstance();
    @Override
    public void handleEvent(Event event) {
        Date date = new Date(Long.parseLong(event.get(Fields.DATE_IN).toString()));
        int e_id = Integer.parseInt(((JSONEvent)event).getSource());
        int shift_id = Integer.parseInt(event.get(Fields.DATE_IN).toString());
        ArrayList<Employee> result = (ArrayList<Employee>) database.findEledgibleEmployeesForShift(e_id, date);
        database.addToRequestShiftChange(e_id, result, shift_id);
    }
}
