package edu.carleton.COMP2601.communication;

/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Class representing a JSON Event Source; based on EventStreamImpl provided by Professor White
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
				evt = new JSONEvent(jo, this);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return evt;
	}

	@SuppressWarnings("unchecked")
	public void putEvent(Event e) throws IOException {
		JSONObject jo = new JSONObject();
		jo.put(JSONEvent.TYPE, ((JSONEvent) e).getType());
		jo.put(JSONEvent.SOURCE, ((JSONEvent) e).getSource());
		jo.put(JSONEvent.DEST, ((JSONEvent) e).getDest());
		jo.put(JSONEvent.NEW_SHIFT, ((JSONEvent) e).getShift());
		jo.put(JSONEvent.NEW_SHIFT, ((JSONEvent) e).getNewShift());
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
