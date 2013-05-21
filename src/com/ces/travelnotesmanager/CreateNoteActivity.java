package com.ces.travelnotesmanager;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import com.ces.travelnotesmanager.dao.Dao;
import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class CreateNoteActivity extends Activity {
	private Note note;
	private EditText title, address, description, date;
	private CheckBox visit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		// Show the Up button in the action bar.
		setupActionBar();

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey("note")) {
			note = (Note) getIntent().getExtras().getSerializable("note");
		} else {
			note = null;
		}

		title = (EditText) findViewById(R.id.noteTitle);
		address = (EditText) findViewById(R.id.noteAddress);
		description = (EditText) findViewById(R.id.noteDescription);
		date = (EditText) findViewById(R.id.noteDate);
		visit = (CheckBox) findViewById(R.id.noteVisitAgain);

		fillIn();
	}

	private void fillIn() {
		if (note != null) {
			title.setText(note.getTitle());
			address.setText(note.getAddress());
			description.setText(note.getDescription());
			date.setText(Service.formatDate(note.getDate()));
			visit.setChecked(note.isVisitAgain());
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.save_note:
			save();
			return true;
		case R.id.cancel:
			cancel();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void save() {

		String title = this.title.getText().toString();
		String address = this.address.getText().toString();
		String description = this.description.getText().toString();
		Date date = null;
		try {
			date = Service.convertDate(this.date.getText().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		boolean visit = this.visit.isChecked();

		if (this.note == null) {
			Note n = new Note(title, address, description, date, visit);
			Service.getInstance(this).addNote(n);
		} else {
			note.setTitle(title);
			note.setAddress(address);
			note.setDescription(description);
			note.setDate(date);
			note.setVisitAgain(visit);
			
			Service.getInstance(this).updateNote(note);
		}
		this.setResult(RESULT_OK);
		finish();
	}

	private void cancel() {
		NavUtils.navigateUpFromSameTask(this);
	}
}
