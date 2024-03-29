package edu.carleton.COMP2601.communication;

/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Class representing a JSON Event Source; based on EventStreamImpl provided by Professor White
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JSONEventSource implements EventStream {

	BufferedWriter writer;
	BufferedReader reader;
	Socket socket;

	/*
	 * Allows streams to be created: input followed by output
	 */
	public JSONEventSource(InputStream is, OutputStream os) throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(os));
		reader = new BufferedReader(new InputStreamReader(is));
	}

	/*
	 * Allows streams to be created: output followed by input
	 */
	public JSONEventSource(String host, int port) throws IOException {
		socket = new Socket(host, port);
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/*
	 * Designed for server-side usage when a socket has been accepted
	 */
	public JSONEventSource(Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream());
		this.socket = s;
	}

	@Override
	public Event getEvent() throws IOException, ClassNotFoundException {
		StringBuffer buf = new StringBuffer();
		String line;
		boolean done = false;
		while (!done) {
			line = reader.readLine();
			if (line == null || line.isEmpty())
				done = true;
			else
				buf.append(line);
		}
		JSONObject jo;
		JSONEvent evt = null;
		String a = buf.toString();
		if (!a.equals("")) {
			System.out.println(buf.toString());
			try {
				jo = new JSONObject(a);
				HashMap<String, Serializable> fields = (HashMap<String, Serializable>) toMap((JSONObject) jo.get(Fields.EVENT_CONTENT));
				evt = new JSONEvent(jo, this, fields);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return evt;
	}

	public static Map<String, Serializable> toMap(JSONObject object) throws JSONException {
		Map<String, Serializable> map = new HashMap<String, Serializable>();

		Iterator<String> keysItr = object.keys();
		while(keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, (Serializable) value);
		}
		return map;
	}

	public static List<Serializable> toList(JSONArray array) throws JSONException {
		List<Serializable> list = new ArrayList<Serializable>();
		for(int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if(value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if(value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add((Serializable) value);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public void putEvent(Event e) throws IOException {
		JSONObject jo = new JSONObject();
		jo.put(Fields.TYPE, ((JSONEvent) e).getType());
		jo.put(Fields.SOURCE, ((JSONEvent) e).getSource());
		jo.put(Fields.DEST, ((JSONEvent) e).getDest());

		JSONObject fields = new JSONObject();
		for(String key: e.getMap().keySet()){
			fields.put(key, e.get(key));
		}
		jo.put(Fields.EVENT_CONTENT, fields);
		writer.write(jo.toString());
		writer.newLine();
		writer.newLine();
		writer.flush();
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (writer != null)
				writer.close();
			if (reader != null)
				reader.close();
			socket = null;
			writer = null;
			reader = null;
		} catch (IOException e) {
			// Fail quietly
		}
	}
}
