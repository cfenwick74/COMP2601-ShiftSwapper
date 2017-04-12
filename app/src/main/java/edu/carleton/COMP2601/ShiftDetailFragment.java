/**
		* COMP2601 Final project: ShiftSwapper
		* Carolyn Fenwick - 100956658
		* Pierre Seguin - 100859121
		* April 12, 2017
**/
package edu.carleton.COMP2601;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.carleton.COMP2601.dummy.DummyContent;
import edu.carleton.COMP2601.model.Shift;
import edu.carleton.COMP2601.model.ShiftChangeRequest;
import edu.carleton.COMP2601.model.ShiftDetailItem;

/**
 * A fragment representing a single Shift detail screen.
 * This fragment is either contained in a {@link ShiftListActivity}
 * in two-pane mode (on tablets) or a {@link ShiftDetailActivity}
 * on handsets.
 */
public class ShiftDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_ITEM_CLASS = "item_class";
	public static final String ARG_ITEM_VALUE = "item_value";
	public static final String ARG_ARRAYLIST = "arraylist";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private ShiftDetailItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ShiftDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mItem = (Shift)getArguments().getSerializable(ARG_ITEM_VALUE);
		Activity activity = this.getActivity();
		CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.shift_detail, container, false);
		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.shift_detail)).setText(mItem.getDescription());
		}

		return rootView;
	}
}
