package edu.carleton.COMP2601.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by carolyn on 2017-04-11.
 */
public class   ScheduledShift implements Serializable{
	Shift shift;
	List<Employee> scheduledEmployees =  new ArrayList<>();

	public ScheduledShift(HashMap<String, Serializable> shiftMap) {
		HashMap<String, Serializable> map = shiftMap;
		shift = new Shift((HashMap<String, Serializable>) map.get("shift"));
		for (Serializable item : (ArrayList<Serializable>)map.get("scheduledEmployees")) {
			scheduledEmployees.add(new Employee((HashMap<String, Serializable>)item));
		}
	}

	public ScheduledShift() {

	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	public List<Employee> getScheduledEmployees() {
		return scheduledEmployees;
	}

	public void setScheduledEmployees(List<Employee> scheduledEmployees) {
		this.scheduledEmployees = scheduledEmployees;
	}
}
