package com.ces.travelnotesmanager;

import com.ces.travelnotesmanager.model.Note;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ShowNoteInfoFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link ShowNoteInfoFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class ShowNoteInfoFragment extends Fragment {
	// TODO: Rename and change types of parameters
	private Note note;
	private TextView title, address, date, description, visit;

	private OnFragmentInteractionListener mListener;

	public static ShowNoteInfoFragment newInstance(Note note) {
		ShowNoteInfoFragment fragment = new ShowNoteInfoFragment();
		Bundle args = new Bundle();
		args.putSerializable("Note", note);
		fragment.setArguments(args);
		return fragment;
	}

	public ShowNoteInfoFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			note = (Note) getArguments().getSerializable("note");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_show_note_info, container,
				false);

		title = (TextView) v.findViewById(R.id.noteTitleLbl);
		address = (TextView) v.findViewById(R.id.noteAddressLbl);
		date = (TextView) v.findViewById(R.id.noteDateLbl);
		description = (TextView) v.findViewById(R.id.noteDescriptionLbl);
		visit = (TextView) v.findViewById(R.id.noteVisitLbl);

		updateText();
		return v;
	}

	public void updateText() {
		title.setText(note.getTitle());
		address.setText(note.getAddress());
		date.setText(note.getDate().toString());
		description.setText(note.getDescription());
		if (note.isVisitAgain())
			visit.setText("Should visit again");
		else
			visit.setText("Don't visit again");
	}
	
	public void updateNote(Note n){
		note = n;
		updateText();
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
