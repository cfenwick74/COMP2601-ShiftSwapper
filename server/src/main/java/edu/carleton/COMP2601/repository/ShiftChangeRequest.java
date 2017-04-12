package edu.carleton.COMP2601.repository;

import java.io.Serializable;

/**
 * Created by Pierre on 4/11/2017.
 */

public class ShiftChangeRequest implements Serializable {
    private int requestor_shift_id;
    private int requestee_shift_id;
    private Shift requestor_shift;
    private Shift requestee_shift;

    ShiftChangeRequest(int reqr_id, Shift reqr_shift, int reqee_id, Shift reqee_shift){
        requestor_shift_id = reqr_id;
        requestee_shift_id = reqee_id;
        requestee_shift = reqee_shift;
        requestor_shift = reqr_shift;
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
}
