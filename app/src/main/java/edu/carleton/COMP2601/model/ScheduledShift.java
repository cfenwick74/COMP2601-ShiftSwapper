package edu.carleton.COMP2601.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carolyn on 2017-04-11.
 */
public class   ScheduledShift implements Serializable{
	Shift shift;
	List<Employee> scheduledEmployees =  new ArrayList<>();

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
