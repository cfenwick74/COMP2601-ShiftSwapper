package edu.carleton.COMP2601.handlers;

import java.util.ArrayList;
import java.util.Date;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Handles a shift swap event
 */

public class SwapShiftHandler implements EventHandler {

    ShiftSwapRepository database = ShiftSwapRepository.getInstance();
    @Override
    public void handleEvent(Event event) {
        int requestor_e_id = Integer.parseInt(((JSONEvent)event).getSource());
        int requestor_shift_id = Integer.parseInt(event.get(Fields.REQUESTORS_SHIFT).toString());
        int requestee_e_id = Integer.parseInt(((JSONEvent)event).getDest());
        int requestee_shift_id = Integer.parseInt(event.get(Fields.REQUESTEES_SHIFT).toString());
        database.addToRequestShiftChange(requestor_e_id, requestor_shift_id, requestee_e_id, requestee_shift_id);
    }
}
