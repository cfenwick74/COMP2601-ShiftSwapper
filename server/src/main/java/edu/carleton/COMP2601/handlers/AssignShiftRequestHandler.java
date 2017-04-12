package edu.carleton.COMP2601.handlers;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Handles a request for a shift assignment
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


