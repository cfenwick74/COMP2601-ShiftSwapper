package edu.carleton.COMP2601.communication;

/**
 * Class provided by Professor White
 */

import java.io.IOException;

public interface EventInputStream {
	public Event getEvent() throws IOException, ClassNotFoundException;
}
