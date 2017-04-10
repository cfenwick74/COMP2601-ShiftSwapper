package edu.carleton.COMP2601.communication;

/**
 * Class provided by Professor White
 */

interface ReactorInterface {
	void register(String type, EventHandler event);
	void deregister(String type);
	void dispatch(Event event) throws NoEventHandler;
}
