package edu.carleton.COMP2601.communication;

/**
 * Class provided by Professor White
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class Event implements EventStream, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String type;
	transient EventStream es;
	private HashMap<String, Serializable> map;

	public Event(String type) {
		this.type = type;
		this.es = null;
		this.map = new HashMap<>();
	}

	public Event(String type, EventStream es) {
		this.type = type;
		this.es = es;
		this.map = new HashMap<>();
	}
	
	public Event(String type, EventStream es, HashMap<String, Serializable> map) {
		this.type = type;
		this.es = es;
		this.map = map;
	}
	
	public void put(String key, Serializable value) {
		map.put(key, value);
	}
	
	public Serializable get(String key) {
		return map.get(key);
	}
	
	public void putEvent() throws ClassNotFoundException, IOException {
		putEvent(this);
	}
	
	public void putEvent(Event e) throws IOException, ClassNotFoundException {
		if (es != null)
			es.putEvent(e);
		else
			throw new IOException("No event stream defined");
	}
	
	public Event getEvent() throws ClassNotFoundException, IOException {
		if (es != null) {
			return es.getEvent();
		} else 
			throw new IOException("No event stream defined");
	}

	public void close() {
		if (es != null) {
			es.close();
		}
	}
}
