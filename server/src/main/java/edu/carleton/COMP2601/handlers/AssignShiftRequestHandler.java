package edu.carleton.COMP2601.handlers;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * Created by carolyn on 2017-04-11.
 */

public class AssignShiftRequestHandler implements EventHandler {
    ShiftSwapRepository database = ShiftSwapRepository.getInstance();

    @Override
    public void handleEvent(Event event) {
        System.out.println("In AssignShiftRequest handler");
        String action = event.getType();
        int shift_id = Integer.parseInt(event.get(Fields.SHIFT).toString());
        int employee_id = Integer.parseInt(event.get(Fields.EMPLOYEE_ID).toString());

        switch (action) {
            case Fields.ASSIGN_SHIFT_REQUEST:
                database.addToSchedule(employee_id, shift_id);
                break;
            case Fields.UNASSIGN_SHIFT_REQUEST:
                database.removeFromSchedule(employee_id, shift_id);
        }

    }
}


