package edu.carleton.COMP2601;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.carleton.COMP2601.communication.AcceptorReactor;
import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.model.Employee;
import edu.carleton.COMP2601.model.ScheduledShift;

public class ShiftAdminActivity extends AppCompatActivity {

	AutoCompleteTextView emp1, emp2, emp3, emp4;
	ScheduledShift shift;
	private AcceptorReactor ar;
	private String currentEmployee;
	private ArrayList<AutoCompleteTextView> textViews;
	private ArrayAdapter<String> names;
	private HashMap<String, Employee> employeesById = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shift_admin);
		textViews = new ArrayList<>();

		textViews.add((AutoCompleteTextView) findViewById(R.id.emp1));
		textViews.add((AutoCompleteTextView) findViewById(R.id.emp2));
		textViews.add((AutoCompleteTextView) findViewById(R.id.emp3));
		textViews.add((AutoCompleteTextView) findViewById(R.id.emp4));
		Button saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(saveOnClickListener());

		ar = AcceptorReactor.getInstance();
		FindAllEmployeesResponseHandler findAllEmployeesResponseHandler = new FindAllEmployeesResponseHandler();
		ar.register(Fields.FIND_ALL_EMPLOYEES_RESPONSE, findAllEmployeesResponseHandler);

		Intent intent = getIntent();

		shift = (ScheduledShift) intent.getSerializableExtra("scheduledShift");
		currentEmployee = intent.getStringExtra(Fields.CURRENT_EMPLOYEE);


		List<Employee> employees = shift.getScheduledEmployees();


		for (int i = 0; i < 4 && i < employees.size(); i++) {
			if (employees.get(i) != null) {
				textViews.get(i).setText(employees.get(i).getName());
			}
		}

		HashMap m = new HashMap();


		try {
			JSONObject jo = new JSONObject();
			jo.put(Fields.TYPE, Fields.FIND_ALL_EMPLOYEES_REQUEST);
			jo.put(Fields.SOURCE, currentEmployee);
			jo.put(Fields.DEST, Fields.DATABASE);
			ar.put(new JSONEvent(jo, null, m));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	private View.OnClickListener saveOnClickListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < textViews.size(); i++) {
					Employee newAssignee = employeesById.get(textViews.get(i).getText().toString());
					Employee oldAssignee = i < shift.getScheduledEmployees().size()? shift.getScheduledEmployees().get(i): null;
					if (oldAssignee != null && !oldAssignee.getName().equals(textViews.get(i).getText().toString())) {
						sendAssignmentRequest(Fields.UNASSIGN_SHIFT_REQUEST, oldAssignee.getID(), shift.getShift().getId());
						if (newAssignee != null) {
							sendAssignmentRequest(Fields.ASSIGN_SHIFT_REQUEST, newAssignee.getID(), shift.getShift().getId());
						}
					} else if (oldAssignee == null && newAssignee != null) {
							sendAssignmentRequest(Fields.ASSIGN_SHIFT_REQUEST, newAssignee.getID(), shift.getShift().getId());
						}
					}
				Toast.makeText(getBaseContext(), "Changes saved", Toast.LENGTH_LONG).show();
				onBackPressed();

			}

		};

	}

	private void sendAssignmentRequest(String requestType, Integer employeeID, int shift_id) {
		HashMap m = new HashMap();
		try {
			JSONObject jo = new JSONObject();
			jo.put(Fields.TYPE, requestType);
			jo.put(Fields.SOURCE, currentEmployee);
			m.put(Fields.EMPLOYEE_ID, employeeID);
			m.put(Fields.SHIFT, shift_id);
			jo.put(Fields.DEST, Fields.DATABASE);
			ar.put(new JSONEvent(jo, null, m));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}


	public class FindAllEmployeesResponseHandler implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			System.out.println("In find all emps response handler");
			employeesById = new HashMap<>();

			for ( Serializable item:(List<Serializable>)event.getMap().get(Fields.EVENT_CONTENT)) {
				Employee emp = new Employee((HashMap<String, Serializable>) item);
				employeesById.put(emp.getName(), emp);
			}
			((Activity)textViews.get(0).getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					for (AutoCompleteTextView view: textViews) {
						view.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, employeesById.keySet().toArray(new String[employeesById.size()])));
					}

				}
			});

		}
	}



}
