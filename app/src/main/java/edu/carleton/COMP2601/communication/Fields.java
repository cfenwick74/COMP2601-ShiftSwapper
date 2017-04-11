package edu.carleton.COMP2601.communication;

/**
 * COMP 2601 - W2017 - Assignment 2
 * Pierre Seguin    -   100859121
 * Carolyn Fenwick  -   100956658
 * String constants
 */

public class Fields {
	public static final String SHIFT = "SHIFT";
	public static final String SHIFT_SWAP_REQUEST = "SHIFT_SWAP_REQUEST";
	public static final String SHIFT_SWAP_RESPONSE = "SHIFT_SWAP_RESPONSE";

	public static final String SHIFT_RELEASE_REQUEST = "SHIFT_RELEASE_REQUEST";
	public static final String SHIFT_RELEASE_RESPONSE = "SHIFT_RELEASE_RESPONSE";

	public static final String ADD_SHIFT_REQUEST = "ADD_SHIFT_REQUEST";
	public static final String ADD_SHIFT_RESPONSE = "ADD_SHIFT_RESPONSE";

	public static final String ASSIGN_SHIFT_REQUEST = "ASSIGN_SHIFT_REQUEST";
	public static final String ASSIGN_SHIFT_RESPONSE = "ASSIGN_SHIFT_RESPONSE";

	public static final String CONNECT_REQUEST = "CONNECT";
	public static final String CONNECTED_RESPONSE = "CONNECTED";

	public static final String DISCONNECT_REQUEST = "DISCONNECT";

	public static final String TYPE = "Type";
	public static final String SOURCE = "Source";
	public static final String DEST = "Dest";
	public static final String EVENT_CONTENT = "FIELDS";
	public static final String NEW_SHIFT = "NewShift";
	public static final String IS_ADMIN = "isAdmin";
	public static final String ID = "ID";
	public static final String PASSWORD = "PASSWORD";
	public static final String DATABASE = "DB";
	public static final int INVALID_USER_ENTRY = -1;
	public static final int USER_ISADMIN = 1;
	public static final int USER_ISEMPLOYEE = 0;


	public static final String STATUS = "Status";
	public static final String TRUE = "True";
	public static final String FALSE = "False";
	public static final String DATE_IN = "Date_In";
	public static final String DATE_OUT = "Date_Out";
	public static final String REQUESTORS_SHIFT = "REQUESTORS_SHIFT";
	public static final String REQUESTEES_SHIFT = "REQUESTEES_SHIFT";


	public static final String SHIFT_LIST_RESPONSE = "SHIFT_LIST_RESPONSE";
	public static final String SHIFT_LIST_REQUEST = "SHIFT_LIST_REQUEST";
	public static final String MASTER_SCHEDULE_REQUEST = "MASTER_SCHEDULE_REQUEST";
	public static final String MASTER_SCHEDULE_RESPONSE = "MASTER_SCHEDULE_RESPONSE";
	public static final String MASTER_SCHEDULE = "MASTER_SCHEDULE";
	public static final String MASTER_SCHEDULE_ITEM = "MASTER_SCHEDULE_ITEM";
	public static final String END_SHIFTS = "END_SHIFTS";
}
