package com.ces.travelnotesmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

public class ShowNoteInfoFragment extends Fragment {
	private Note note;
	private TextView title, address, date, description, visit;
	private ImageView img;
	private ImageButton btn;
	private LinearLayout layout;

	private final static int IMAGE_ID = 6666;

	private OnFragmentInteractionListener mListener;

	public ShowNoteInfoFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (getArguments() != null)
			note = (Note) getArguments().getSerializable("note");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.frag_show_note_info, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_note:
			Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
			intent.putExtra("note", note);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			getActivity().startActivityForResult(intent, 2);
			return true;
		case R.id.share: // Opens chooser for apps that can share(SEND)
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, getShareText());
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
			startActivity(Intent.createChooser(shareIntent, "Share using:"));
			return true;
		case R.id.delete_note:
			Service.getInstance(getActivity()).removeNote(note);
			if (mListener != null)
				mListener.onNoteDeleted();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show_note_info, container,
				false);
	}

	/**
	 * Assign variables after view is created
	 */
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState) {
		super.onViewCreated(v, savedInstanceState);
		title = (TextView) v.findViewById(R.id.noteTitleLbl);
		address = (TextView) v.findViewById(R.id.noteAddressLbl);
		date = (TextView) v.findViewById(R.id.noteDateLbl);
		description = (TextView) v.findViewById(R.id.noteDescriptionLbl);
		visit = (TextView) v.findViewById(R.id.noteVisitLbl);
		layout = (LinearLayout) v.findViewById(R.id.showNoteLinearLayout);
		img = (ImageView) v.findViewById(IMAGE_ID);

		btn = (ImageButton) v.findViewById(R.id.showMap);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onShowMapPressed(v);
			}
		});

		updateText();
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

	public Note getNote() {
		return note;
	}

	/**
	 * Updates fields
	 */
	public void updateText() {
		if (isAdded() && getView() != null) {
			if (note.getId() != 0) {
				btn.setVisibility(View.VISIBLE);
				title.setText(note.getTitle());
				address.setText(note.getAddress());
				date.setText(Service.formatDate(note.getDate()));
				description.setText(note.getDescription());

				if (note.isVisitAgain()) {
					visit.setText(getString(R.string.visitAgain));
					visit.setBackgroundColor(getResources().getColor(
							android.R.color.holo_green_light));
				} else {
					visit.setText(getString(R.string.dontVisitAgain));
					visit.setBackgroundColor(getResources().getColor(
							android.R.color.holo_red_light));
				}

				/**
				 * Attaching new task (thread) to be executed after layout is
				 * loaded. It is in a thread because task could take some time
				 * (depending on image size)
				 */
				layout.post(new Runnable() {
					@Override
					public void run() {
						if (getActivity() == null)
							return;
						img = (ImageView) layout.findViewById(IMAGE_ID);
						if (img != null) { // Remove old image
							layout.removeView(img);
							img = null;
						}

						if (note.getImage() != null
								&& note.getImage().length() != 0) {
							AssetManager am = getActivity().getAssets();
							Bitmap image = null;

							image = Service.getInstance(getActivity())
									.decodeImage(note.getImage(),
											layout.getWidth(), am);
							if (image != null) {
								float w = image.getWidth();
								float h = image.getHeight();

								float scWidth = layout.getWidth();

								img = new ImageView(getActivity());
								img.setId(IMAGE_ID);
								img.setImageBitmap(image);

								// Scaling image to fit perfectly, and not to
								// leave unwanted space
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										(int) scWidth,
										(int) (scWidth * (h / w)));
								lp.setMargins(0, 10, 0, 10); // Adding margin,
																// to look nicer
								img.setLayoutParams(lp);
								img.setScaleType(ScaleType.FIT_CENTER);
								layout.addView(img, 2); // Adds below title and
														// [address, date]
							}
						}
					}
				});
			} else
				btn.setVisibility(View.GONE);
		}
	}

	public void updateNote(Note n) {
		note = n;
		if (getActivity() != null)
			updateText();
	}

	public void onShowMapPressed(View v) {
		Intent intent = new Intent(getActivity(), MapActivity.class);
		intent.putExtra("address", note.getAddress());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getActivity().startActivityForResult(intent, 4);
	}


	/**
	 * Text for sharing
	 * 
	 * @return
	 */
	private String getShareText() {
		String s = "Hey! I've been to " + note.getAddress() + ". ";
		if (note.isVisitAgain())
			s += "I totally recommend you to visit this awesome place!";
		else
			s += "I would not recommend it to anyone though.";

		return s;
	}

	/**
	 * Interface for interacting with activity
	 */
	public interface OnFragmentInteractionListener {
		public void onNoteDeleted();
	}

}
