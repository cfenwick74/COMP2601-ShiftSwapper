package edu.carleton.COMP2601.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by carolyn on 2017-04-09.
 */
public class Shift implements Serializable {
	public int id;
	private Date start;
	private Date end;

	public Shift(int id, Date start, Date end) {
		this(start, end);
		this.id = id;
	}
	public Shift(Date start, Date end) {
		this.start = start;
		this.end = end;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Shift shift = (Shift) o;

		if (id != shift.id) return false;
		if (start != null ? !start.equals(shift.start) : shift.start != null) return false;
		return end != null ? end.equals(shift.end) : shift.end == null;

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (start != null ? start.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		return result;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public int getId() {
		return id;
	}
}
