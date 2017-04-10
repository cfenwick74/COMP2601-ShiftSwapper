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
    public static final String TYPE = "Type";
    public static final String SOURCE = "Source";
    public static final String DEST = "Dest";
    public static final String NEW_SHIFT = "NewShift";
    public static final String SHIFT = "Shift";

    private final String source;
    private final String dest;
    private final String shift;
    private final String newShift;


    /**
     * Create a new {@link JSONEvent} from a {@link JSONObject}
     *
     * @param jo the {@link JSONObject} from which to create the event
     * @param s  the object's {@link EventStream}
     * @throws JSONException
     */
    public JSONEvent(JSONObject jo, EventStream s) throws JSONException {
        super(jo.getString(TYPE), s);
        this.source = jo.getString(SOURCE);
        this.dest = jo.getString(DEST);
        this.shift = jo.getString(SHIFT);
        this.newShift = jo.getString(NEW_SHIFT);
    }

    public String getSource() {
        return source;
    }

    public String getDest() {
        return dest;
    }

    public String getType() {
        return type;
    }

    public String getShift() {
        return shift;
    }

    public String getNewShift() {
        return newShift;
    }

}
