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
import edu.carleton.COMP2601.repository.Employee;
import edu.carleton.COMP2601.repository.ShiftSwapRepository;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Handles a request for a list of all employees
 */

public class FindAllEmployeesHandler implements EventHandler {

    ShiftSwapRepository database = ShiftSwapRepository.getInstance();

    @Override
    public void handleEvent(Event event) {
        try {
            ArrayList<Employee> employees = (ArrayList<Employee>) database.findAllEmployees();
            JSONObject jo = new JSONObject();

            jo.put(Fields.TYPE, Fields.FIND_ALL_EMPLOYEES_RESPONSE);
            jo.put(Fields.SOURCE, Fields.DATABASE);
            jo.put(Fields.DEST, "");
            HashMap<String, Serializable> a = new HashMap();
            a.put(Fields.EVENT_CONTENT, employees);
            event.putEvent(new JSONEvent(jo, null, a));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
