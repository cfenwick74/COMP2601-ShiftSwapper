package edu.carleton.COMP2601.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Pierre on 4/11/2017.
 */

public class ShiftChangeRequest implements Serializable, ShiftDetailItem {
    int requestor_shift_id;
    int requestee_shift_id;
    Shift requestor_shift;
    Shift requestee_shift;
    int changeId;

    public ShiftChangeRequest(int reqr_id, Shift reqr_shift, int reqee_id, Shift reqee_shift){
        requestor_shift_id = reqr_id;
        requestee_shift_id = reqee_id;
        requestee_shift = reqee_shift;
        requestor_shift = reqr_shift;
    }

    public ShiftChangeRequest(HashMap<String, Serializable> request) {
        requestor_shift_id = (Integer)request.get("requestor_shift_id");
        requestee_shift_id = (Integer)request.get("requestee_shift_id");
        requestee_shift = new Shift((HashMap<String, Serializable>) request.get("requestee_shift"));
        requestor_shift = new Shift((HashMap<String, Serializable>)request.get("requestor_shift"));
        changeId = (Integer)request.get("changeId");
    }

    @Override
    public int getId() {
        return changeId;
    }

    @Override
    public String getDescription() {
        return "Shift swap offered:\n\n" + requestee_shift.getDescription() + "\n--------- to swap for shift --------- \n" + requestor_shift.getDescription();
    }

    public int getRequestor_shift_id() {
        return requestor_shift_id;
    }

    public void setRequestor_shift_id(int requestor_shift_id) {
        this.requestor_shift_id = requestor_shift_id;
    }

    public int getRequestee_shift_id() {
        return requestee_shift_id;
    }

    public void setRequestee_shift_id(int requestee_shift_id) {
        this.requestee_shift_id = requestee_shift_id;
    }

    public Shift getRequestor_shift() {
        return requestor_shift;
    }

    public void setRequestor_shift(Shift requestor_shift) {
        this.requestor_shift = requestor_shift;
    }

    public Shift getRequestee_shift() {
        return requestee_shift;
    }

    public void setRequestee_shift(Shift requestee_shift) {
        this.requestee_shift = requestee_shift;
    }

    public int getChangeId() {
        return changeId;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }
}
