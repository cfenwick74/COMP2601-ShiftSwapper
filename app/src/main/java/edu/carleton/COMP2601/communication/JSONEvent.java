package edu.carleton.COMP2601.communication;

/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Class representing a JSON Event
 */

import org.json.JSONException;
import org.json.JSONObject;

public class JSONEvent extends Event {
	private final String source;
	private final String dest;
	private final String message;

	public JSONEvent(JSONObject jo, EventStream s) throws JSONException {
		super(jo.getString("Type"),s);
		this.source = jo.getString("Source");
		this.dest = jo.getString("Dest");
		this.message = jo.getString("Message");
	}
	
	public String getSource() {
		return source;
	}
	public String getDest() { return dest; }
    public String getType() { return type; }
	public String getMessage() { return message; }
}
