package edu.carleton.COMP2601.dummy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.carleton.COMP2601.model.Shift;

/**
 * Created by carolyn on 2017-04-10.
 */
public class DummyShiftContent {
	public static List<Shift> ITEMS = new ArrayList<>();

	static {
		ITEMS.add(new Shift(1, new Date(), new Date()));
		ITEMS.add(new Shift(2, new Date(), new Date()));
		ITEMS.add(new Shift(3, new Date(), new Date()));
		ITEMS.add(new Shift(4, new Date(), new Date()));
		ITEMS.add(new Shift(5, new Date(), new Date()));
		ITEMS.add(new Shift(6, new Date(), new Date()));
		ITEMS.add(new Shift(7, new Date(), new Date()));
		ITEMS.add(new Shift(8, new Date(), new Date()));
		ITEMS.add(new Shift(9, new Date(), new Date()));

	}
}
