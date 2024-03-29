package edu.carleton.COMP2601.handlers;

import java.util.Date;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.repository.Shift;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Handles a request to add a shift (not yet implemented)
 */

public class AddShiftHandler implements EventHandler {

    ShiftSwapRepository database = ShiftSwapRepository.getInstance();
    @Override
    public void handleEvent(Event event) {
        Date in = new Date(Long.parseLong(event.get(Fields.DATE_IN).toString()));
        Date out = new Date(Long.parseLong(event.get(Fields.DATE_OUT).toString()));
        database.addShift(new Shift(in, out));
    }
}
