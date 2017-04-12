package edu.carleton.COMP2601.handlers;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.repository.ScheduledShift;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Handles a request for the master shift schedule
 */

public class MasterScheduleRequestHandler implements EventHandler {
	ShiftSwapRepository database = ShiftSwapRepository.getInstance();
	@Override
	public void handleEvent(Event event) {
		System.out.println("In Schedule request handler");

		ArrayList<ScheduledShift> scheduledShifts = database.getMasterSchedule();

		try {
			JSONObject jo = new JSONObject();
			jo.put(Fields.TYPE, Fields.MASTER_SCHEDULE_RESPONSE);
			jo.put(Fields.SOURCE, Fields.DATABASE);
			jo.put(Fields.DEST, ((JSONEvent) event).getSource());

			HashMap<String, Serializable> a = new HashMap();
			a.put(Fields.MASTER_SCHEDULE, scheduledShifts);

			Event e = new JSONEvent(jo,null,a);
			event.putEvent(e);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Event e = new Event(Fields.MASTER_SCHEDULE_RESPONSE);

	}
}


