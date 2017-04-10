package edu.carleton.COMP2601.communication;

/**
 * Class provided by Professor White
 */

import java.io.IOException;

interface EventInputStream {
	Event getEvent() throws IOException, ClassNotFoundException;
}
