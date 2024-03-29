package edu.carleton.COMP2601;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.carleton.COMP2601.model.ScheduledShift;
import edu.carleton.COMP2601.model.Shift;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Shift} and makes a call to the
 * specified {@link ManageShiftsFragment.OnListFragmentInteractionListener}.
 *
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 */

public class MyShiftRecyclerViewAdapter extends RecyclerView.Adapter<MyShiftRecyclerViewAdapter.ViewHolder> {

	public final List<ScheduledShift> mValues;
	private final ManageShiftsFragment.OnListFragmentInteractionListener mListener;

	public MyShiftRecyclerViewAdapter(List<ScheduledShift> items, ManageShiftsFragment.OnListFragmentInteractionListener listener) {
		mValues = items;
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_manage_shift, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		ScheduledShift scheduledShift = mValues.get(position);
		holder.mItem = scheduledShift;
		holder.mContentView.setText(scheduledShift.getShift().getDescription() + "\nEmployees Assigned: " + scheduledShift.getScheduledEmployees().size());

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
					mListener.onListFragmentInteraction(holder.mItem);
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

		public final TextView mContentView;
		public ScheduledShift mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mContentView = (TextView) view.findViewById(R.id.content);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + mContentView.getText() + "'";
		}
	}
}
