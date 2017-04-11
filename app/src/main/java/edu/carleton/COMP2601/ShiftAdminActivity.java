package edu.carleton.COMP2601;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import edu.carleton.COMP2601.model.Employee;
import edu.carleton.COMP2601.model.ScheduledShift;

public class ShiftAdminActivity extends AppCompatActivity {


	AutoCompleteTextView emp1, emp2, emp3, emp4;
	ScheduledShift shift;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shift_admin);

		Intent intent = getIntent();

		shift = (ScheduledShift) intent.getSerializableExtra("scheduledShift");

		ArrayList<AutoCompleteTextView> textViews = new ArrayList<>();

		textViews.add((AutoCompleteTextView) findViewById(R.id.emp1));
		textViews.add((AutoCompleteTextView) findViewById(R.id.emp2));
		textViews.add((AutoCompleteTextView) findViewById(R.id.emp3));
		textViews.add((AutoCompleteTextView) findViewById(R.id.emp4));

		List<Employee> employees = shift.getScheduledEmployees();

		for (int i = 0; i < employees.size(); i++) {
			if (employees.get(i) != null) {
				textViews.get(i).setText(employees.get(i).getName());
			}
		}
	}


}
