package com.ces.travelnotesmanager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

public class ShowNoteActivity extends FragmentActivity implements
		ShowNoteInfoFragment.OnFragmentInteractionListener {

	private Note note;
	private ShowNoteInfoFragment info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_note);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		note = (Note) getIntent().getExtras().getSerializable("note");
		setTitle("Note - " + note.getTitle());

		// Creating fragment where the note will be shown
		info = getFragment();
		if (info == null) {
			Fragment fr = new ShowNoteInfoFragment();
			Bundle args = new Bundle();
			args.putSerializable("note", note);
			fr.setArguments(args);

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.showNoteContainer, fr);
			ft.commit();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// If orientation becomes landscape - switch to main activity
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Intent i = new Intent(this, MainActivity.class);
			i.putExtra("note", note);
			startActivity(i);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// only getting results from edit, so we refresh note
		note = Service.getInstance(this).reloadNote(note);
		getFragment().updateNote(note);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			goBack();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onNoteDeleted() {
		goBack();
	}

	/**
	 * Starts Main activity
	 */
	private void goBack() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

	/**
	 * Gets show note fragment from container. If there is any
	 * 
	 * @return
	 */
	private ShowNoteInfoFragment getFragment() {
		return (ShowNoteInfoFragment) getSupportFragmentManager()
				.findFragmentById(R.id.showNoteContainer);
	}

}
