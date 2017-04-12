/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 **/

package edu.carleton.COMP2601;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeProvider;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import edu.carleton.COMP2601.communication.AcceptorReactor;
import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.dummy.DummyContent;
import edu.carleton.COMP2601.model.ScheduledShift;
import edu.carleton.COMP2601.model.Shift;
import edu.carleton.COMP2601.model.ShiftChangeRequest;
import edu.carleton.COMP2601.model.ShiftDetailItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of Shifts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ShiftDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ShiftListActivity extends AppCompatActivity {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	Activity a;
	ArrayList<ShiftDetailItem> items = new ArrayList<>();
	private ManageShiftsFragment.OnListFragmentInteractionListener mListener;
	private String currentEmployee;
	SimpleItemRecyclerViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		a = this;
		AcceptorReactor ar = AcceptorReactor.getInstance();
		currentEmployee = getIntent().getStringExtra(Fields.SOURCE);
		JSONObject jo = new JSONObject();

		if(getIntent().hasExtra(ShiftDetailFragment.ARG_ARRAYLIST)) {
			items = (ArrayList<ShiftDetailItem>) getIntent().getSerializableExtra(ShiftDetailFragment.ARG_ARRAYLIST);
			adapter.notifyDataSetChanged();
		}
		try {
			ar.register(Fields.EMP_SCHEDULE_RESPONSE, new EmployeeShiftResponseHandler());
			jo.put(Fields.TYPE, Fields.EMP_SCHEDULE_REQUEST);
			jo.put(Fields.SOURCE, currentEmployee);
			jo.put(Fields.DEST, "Server");
			ar.put(new JSONEvent(jo,null,new HashMap<String, Serializable>()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		EmployeeShiftResponseHandler employeeShiftResponseHandler = new EmployeeShiftResponseHandler();
		setContentView(R.layout.activity_shift_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		View recyclerView = findViewById(R.id.shift_list);
		assert recyclerView != null;
		setupRecyclerView((RecyclerView) recyclerView);

		if (findViewById(R.id.shift_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
			//mListener = (ManageShiftsFragment.OnListFragmentInteractionListener) recyclerView;
			//adapter = new MyShiftRecyclerViewAdapter(new ArrayList<ScheduledShift>(), mListener);
			//recyclerView.setAdapter(adapter);
		adapter = new SimpleItemRecyclerViewAdapter(items);
		recyclerView.setAdapter(adapter);
	}

	public class SimpleItemRecyclerViewAdapter
			extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

		private final List<ShiftDetailItem> mValues;

		public SimpleItemRecyclerViewAdapter(List<ShiftDetailItem> items) {
			mValues = items;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.shift_list_content, parent, false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			holder.mItem = mValues.get(position);
			holder.mIdView.setText(""+mValues.get(position).getId());
			holder.mContentView.setText(mValues.get(position).getDescription());

			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mTwoPane) {
						Bundle arguments = new Bundle();
						//arguments.putInt(ShiftDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
						arguments.putString(ShiftDetailFragment.ARG_ITEM_CLASS, holder.mItem.getClass().toString());
						arguments.putSerializable(ShiftDetailFragment.ARG_ITEM_VALUE, (Serializable) holder.mItem);
						ShiftDetailFragment fragment = new ShiftDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.shift_detail_container, fragment)
								.commit();
					} else {
						Context context = v.getContext();
						Intent intent = new Intent(context, ShiftDetailActivity.class);
						intent.putExtra(ShiftDetailFragment.ARG_ITEM_VALUE, (Serializable) holder.mItem);
						//intent.putExtra(ShiftDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

						context.startActivity(intent);
					}
				}
			});
		}

		@Override
		public int getItemCount() {
			return mValues.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public final View mView;
			public final TextView mIdView;
			public final TextView mContentView;
			public ShiftDetailItem mItem;

			public ViewHolder(View view) {
				super(view);
				mView = view;
				mIdView = (TextView) view.findViewById(R.id.id);
				mContentView = (TextView) view.findViewById(R.id.content);
			}

			@Override
			public String toString() {
				return super.toString() + " '" + mContentView.getText() + "'";
			}
		}
	}

	private class EmployeeShiftResponseHandler implements EventHandler{


		@Override
		public void handleEvent(Event event) {
			items.clear();
			for(HashMap<String, Serializable> shift : (ArrayList<HashMap<String,Serializable>>)event.get(Fields.SHIFT)){
				items.add((new Shift(shift)));
			}
			for(HashMap<String, Serializable> shift : (ArrayList<HashMap<String,Serializable>>)event.get(Fields.REQUESTORS_SHIFT)){
				items.add(new ShiftChangeRequest(shift));
			}
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
		}
	}
}
