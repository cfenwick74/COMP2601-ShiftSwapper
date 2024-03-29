package edu.carleton.COMP2601.repository;

import java.io.Serializable;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * ShiftChangeRequest.java - represents a shift change request
 */

public class ShiftChangeRequest implements Serializable {
    private final int changeId;
    private int requestor_shift_id;
    private int requestee_shift_id;
    private Shift requestor_shift;
    private Shift requestee_shift;

    ShiftChangeRequest(int reqr_id, Shift reqr_shift, int reqee_id, Shift reqee_shift, int id){
        requestor_shift_id = reqr_id;
        requestee_shift_id = reqee_id;
        requestee_shift = reqee_shift;
        requestor_shift = reqr_shift;
        changeId = id;
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
}
