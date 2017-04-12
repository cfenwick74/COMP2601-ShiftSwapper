package edu.carleton.COMP2601.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * ScheduledShift.java - represents a shift that has one or more Employees assigned to it
 */
public class ScheduledShift implements Serializable{
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
