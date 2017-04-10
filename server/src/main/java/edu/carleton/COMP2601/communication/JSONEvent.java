package edu.carleton.COMP2601.communication;

/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * Class representing a JSON Event
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class JSONEvent extends Event {

    private final String source;
    private final String dest;


    /**
     * Create a new {@link JSONEvent} from a {@link JSONObject}
     *
     * @param jo the {@link JSONObject} from which to create the event
     * @param s  the object's {@link EventStream}
     * @throws JSONException
     */
    public JSONEvent(JSONObject jo, EventStream s, HashMap<String,Serializable> fields) throws JSONException {
        super(jo.getString(Fields.TYPE), s, fields);
        this.source = jo.getString(Fields.SOURCE);
        this.dest = jo.getString(Fields.DEST);
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



}
