package com.ces.travelnotesmanager;

import com.ces.travelnotesmanager.model.Note;

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
		note = null;
		title = (EditText) findViewById(R.id.noteTitle);
		address = (EditText) findViewById(R.id.noteAddress);
		description = (EditText) findViewById(R.id.noteDescription);
		date = (EditText) findViewById(R.id.noteDate);
		visit = (CheckBox) findViewById(R.id.noteVisitAgain);
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
			
			return true;
		case R.id.cancel:
			cancel();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void save(){
		if(this.note == null){
			String title = this.title.getText().toString();
			String address = this.address.getText().toString();
		}
	}
	
	private void cancel(){
		NavUtils.navigateUpFromSameTask(this);
	}
}
