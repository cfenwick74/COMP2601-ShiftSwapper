package edu.carleton.COMP2601.communication;

/**
 * Class provided by Professor White
 */

import java.io.IOException;

interface EventOutputStream {
	void putEvent(Event e) throws IOException, ClassNotFoundException;
}
