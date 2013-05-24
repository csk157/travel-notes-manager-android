package com.ces.travelnotesmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

public class NotesFragment extends Fragment implements
		AbsListView.OnItemClickListener, OnItemSelectedListener {

	private static final int ALL = 0;
	private static final int VISIT = 1;
	private static final int DONT_VISIT = 2;

	private OnFragmentInteractionListener mListener;
	private AbsListView mListView;
	private ListAdapter mAdapter;
	private Spinner filter;
	private int currentFilter = ALL;
	private Note selected;

	public NotesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		// Saving current filter in service, so we can access it later
		currentFilter = Service.getInstance(getActivity()).getShowFilter();
		refreshList();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.frag_notes_list, menu);
		MenuItem m = menu.findItem(R.id.menuFilter);

		// Spinner for changing filter
		filter = (Spinner) m.getActionView().findViewById(R.id.filterNotes);
		filter.setSelection(currentFilter);
		filter.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.createNote:
			Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			getActivity().startActivityForResult(intent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notes, container, false);

		mListView = (AbsListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

		mListView.setOnItemClickListener(this);

		// if note is attached - select it
		Bundle args = getArguments();
		if (args != null) {
			if (args.containsKey("note"))
				setSelected((Note) args.getSerializable("note"));
		}

		return view;
	}

	/**
	 * Makes sure activity implements our listener
	 */
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (null != mListener) {
			Note n = (Note) mListView.getItemAtPosition(position);
			selected = n;
			mListener.onNoteSelected(n);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adapter, View arg1, int pos,
			long arg3) {
		if (adapter instanceof Spinner) {
			if (currentFilter != pos) {
				currentFilter = pos;
				Service.getInstance(getActivity()).setShowFilter(currentFilter);
				if (mListener != null)
					mListener.onFilterChanged();
				refreshList();
			}

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/**
	 * Refreshes current list
	 */
	public void refreshList() {
		List<Note> notes;
		switch (currentFilter) {
		case ALL:
			notes = Service.getInstance(getActivity()).getAllNotes();
			break;
		case VISIT:
			notes = Service.getInstance(getActivity()).getVisitNotes();
			break;
		case DONT_VISIT:
			notes = Service.getInstance(getActivity()).getDontVisitNotes();
			break;
		default:
			notes = new ArrayList<Note>();
		}

		mAdapter = new ArrayAdapter<Object>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, notes.toArray());

		if (mListView != null)
			mListView.setAdapter(mAdapter);
	}

	public Note getSelected() {
		return selected;
	}

	/**
	 * Sets currently selected note, and simulates click
	 * 
	 * @param n
	 */
	public void setSelected(Note n) {
		selected = n;
		if (currentFilter == VISIT && !n.isVisitAgain()
				|| currentFilter == DONT_VISIT && n.isVisitAgain())
			return;
		int pos = 0;

		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (mAdapter.getItem(i).equals(n)) {
				pos = i;
				break;
			}
		}

		View v = mListView.getAdapter().getView(pos, null, null);
		if (v != null)
			mListView.performItemClick(v, pos, pos);
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}

	/**
	 * Interface for informing activity on actions performed in this fragment
	 */
	public interface OnFragmentInteractionListener {
		public void onNoteSelected(Note n);
		public void onFilterChanged();
	}

}
