package edu.carleton.COMP2601.communication;

/**
 * Class provided by Professor White
 */

interface EventStream extends EventInputStream, EventOutputStream {
	void close();
}
