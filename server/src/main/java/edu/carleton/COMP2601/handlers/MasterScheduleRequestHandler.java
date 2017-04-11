package edu.carleton.COMP2601.handlers;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * Created by carolyn on 2017-04-11.
 */

public class MasterScheduleRequestHandler implements EventHandler {
	ShiftSwapRepository database = ShiftSwapRepository.getInstance();
	@Override
	public void handleEvent(Event event) {
		System.out.println(event);
		System.out.println("In Schedule request handler");

	}
}


