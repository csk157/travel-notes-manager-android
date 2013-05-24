package com.ces.travelnotesmanager;

import java.text.ParseException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.ces.travelnotesmanager.NotesFragment.OnFragmentInteractionListener;
import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

public class MainActivity extends FragmentActivity implements
		OnFragmentInteractionListener,
		ShowNoteInfoFragment.OnFragmentInteractionListener {

	private NotesFragment nFrag;
	private ShowNoteInfoFragment showNoteFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(getString(R.string.app_name));
		SharedPreferences settings = getSharedPreferences("TravelNotesPrefs", 0);

		// Create initial objects on first launch
		if (settings.getBoolean("FirstTime", true)) {
			try {
				Service.getInstance(this).createSomeObjects();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			settings.edit().putBoolean("FirstTime", false).commit();
		}

		// If fragment exists (activity recreated) assign it here
		nFrag = (NotesFragment) getSupportFragmentManager().findFragmentById(
				R.id.viewContainer);

		// If not found - create
		if (findViewById(R.id.viewContainer) != null && nFrag == null) {
			nFrag = new NotesFragment();
			if (getIntent().getExtras() != null)
				nFrag.setArguments(new Bundle(getIntent().getExtras()));

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.viewContainer, nFrag);
			ft.commit();
		}

		// Add show note fragment as well if there is one
		if (showNoteFrag == null) {
			showNoteFrag = (ShowNoteInfoFragment) getSupportFragmentManager()
					.findFragmentById(R.id.noteContainer);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// When orientation changed to portrait, and note is shown
		// open separate activity only showing the note
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
				&& showNoteFrag != null) {
			Intent i = new Intent(this, ShowNoteActivity.class);
			i.putExtra("note", showNoteFrag.getNote());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish(); // Finish this activity, so there are no unused instances
		} else
			recreate(); // Recreate this one, so new fragments are created,
						// shown

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 || requestCode == 2) { // If returning from
													// create(1) or edit(2)
			if (resultCode == RESULT_OK) { // Refresh fragments, select new
											// (edited) note
				Note n = (Note) data.getSerializableExtra("note");
				refresh();
				nFrag.setSelected(n);
			}
		}
	}

	@Override
	public void onNoteSelected(final Note n) {
		if (showNoteFrag == null) { // Not showing any note
			if (findViewById(R.id.noteContainer) == null) { // Portrait mode,
															// open new activity
				Intent i = new Intent(this, ShowNoteActivity.class);
				i.putExtra("note", n);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			} else { // Create a new fragment
				showNoteFrag = new ShowNoteInfoFragment();
				Bundle args = new Bundle();
				args.putSerializable("note", n);
				showNoteFrag.setArguments(args);

				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.replace(R.id.noteContainer, showNoteFrag);
				ft.commitAllowingStateLoss();
			}
		} else {
			showNoteFrag.updateNote(n);
		}
	}

	@Override
	public void onNoteDeleted() {
		hideCurrent();
		refresh();
	}

	@Override
	public void onFilterChanged() {
		hideCurrent();
	}
	
	private void refresh() {
		nFrag.refreshList();
	}

	/**
	 * Removes show note fragment
	 */
	private void hideCurrent() {
		if (showNoteFrag != null) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.remove(showNoteFrag);
			ft.commit();
			showNoteFrag = null;
		}
	}
}
