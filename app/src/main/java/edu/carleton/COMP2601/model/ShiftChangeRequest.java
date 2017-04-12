package edu.carleton.COMP2601.model;

import java.io.Serializable;

/**
 * Created by Pierre on 4/11/2017.
 */

public class ShiftChangeRequest implements Serializable {
    int requestor_shift_id;
    int requestee_shift_id;
    Shift requestor_shift;
    Shift requestee_shift;

    ShiftChangeRequest(int reqr_id, Shift reqr_shift, int reqee_id, Shift reqee_shift){
        requestor_shift_id = reqr_id;
        requestee_shift_id = reqee_id;
        requestee_shift = reqee_shift;
        requestor_shift = reqr_shift;
    }
}
