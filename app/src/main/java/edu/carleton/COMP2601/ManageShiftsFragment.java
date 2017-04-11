package edu.carleton.COMP2601;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import edu.carleton.COMP2601.communication.AcceptorReactor;
import edu.carleton.COMP2601.communication.Event;
import edu.carleton.COMP2601.communication.EventHandler;
import edu.carleton.COMP2601.communication.Fields;
import edu.carleton.COMP2601.communication.JSONEvent;
import edu.carleton.COMP2601.dummy.DummyShiftContent;
import edu.carleton.COMP2601.model.Shift;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ManageShiftsFragment extends Fragment {

	// TODO: Customize parameter argument names
	private static final String SOURCE  = "SOURCE";
	private static final String ARG_COLUMN_COUNT = "column-count";
	// TODO: Customize parameters
	private int mColumnCount = 1;
	private OnListFragmentInteractionListener mListener;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ManageShiftsFragment() {
	}

	// TODO: Customize parameter initialization
	@SuppressWarnings("unused")
	public static ManageShiftsFragment newInstance(int columnCount, String employee) {
		ManageShiftsFragment fragment = new ManageShiftsFragment();
		Bundle args = new Bundle();
		args.putString(SOURCE, employee);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AcceptorReactor ar = AcceptorReactor.getInstance();
		ShiftMasterListResponseHandler shiftMasterListResponseHandler = new ShiftMasterListResponseHandler();
		ar.register(Fields.SHIFT_LIST_RESPONSE, shiftMasterListResponseHandler);
		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(SOURCE);
		}



		try {
			JSONObject jo = new JSONObject();
			jo.put(Fields.TYPE, Fields.SHIFT_MASTER_LIST_REQUEST);
			jo.put(Fields.SOURCE, getArguments().get(SOURCE));
			jo.put(Fields.DEST, "");
			ar.put(new JSONEvent(jo,null, new HashMap<String, Serializable>()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_manage_shift_list, container, false);
		// Set the adapter
		if (view instanceof RecyclerView) {
			Context context = view.getContext();
			mListener = (OnListFragmentInteractionListener)context;

			RecyclerView recyclerView = (RecyclerView) view;
			if (mColumnCount <= 1) {
				recyclerView.setLayoutManager(new LinearLayoutManager(context));
			} else {
				recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
			}

			recyclerView.setAdapter(new MyShiftRecyclerViewAdapter(DummyShiftContent.ITEMS, mListener));
		}
		return view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnListFragmentInteractionListener) {
			mListener = (OnListFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnListFragmentInteractionListener {
		// TODO: Update argument type and name
		void onListFragmentInteraction(Shift item);
	}

	private class ShiftMasterListResponseHandler implements EventHandler {
		@Override
		public void handleEvent(Event event) {

		}
	}

}
