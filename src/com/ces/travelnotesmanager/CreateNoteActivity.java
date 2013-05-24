package com.ces.travelnotesmanager;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

public class CreateNoteActivity extends Activity {
	private Note note;
	private EditText title, address, description;
	private DatePicker date;
	private CheckBox visit;
	private String selectedImage;
	private final static int IMAGE_ID = 6666;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getString(R.string.createNoteTitle));

		// Checking if note is attached, if yes - edit, no - create
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey("note"))
			note = (Note) getIntent().getExtras().getSerializable("note");
		else
			note = null;

		title = (EditText) findViewById(R.id.noteTitle);
		address = (EditText) findViewById(R.id.noteAddress);
		description = (EditText) findViewById(R.id.noteDescription);
		date = (DatePicker) findViewById(R.id.noteDate);
		visit = (CheckBox) findViewById(R.id.noteVisitAgain);
		layout = (LinearLayout) findViewById(R.id.createNoteLayout);

		date.setMaxDate(System.currentTimeMillis()); // Do not allow future
														// dates
		fillIn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.setResult(RESULT_CANCELED);
			finish();
			return true;
		case R.id.saveNote:
			save();
			return true;
		case R.id.cancel:
			cancel();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Response from select image
		if (requestCode == 5 && data != null && data.getData() != null) {
			detachImage();
			Uri uri = data.getData();

			if (uri != null) { // Image is picked
				Cursor cursor = getContentResolver()
						.query(uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();

				selectedImage = cursor.getString(0);
				cursor.close();

				displayImage(selectedImage);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Shows chooser for selecting an image from other apps (gallery, dropbox,
	 * etc.)
	 * 
	 * @param v
	 */
	public void onImageSelectClicked(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				5);
	}

	/**
	 * Detaches image from note
	 * 
	 * @param v
	 */
	public void onImageDetachClicked(View v) {
		detachImage();
	}

	/**
	 * Fills in fields (when editing)
	 */
	private void fillIn() {
		if (note != null) {
			title.setText(note.getTitle());
			address.setText(note.getAddress());
			description.setText(note.getDescription());

			Calendar c = Calendar.getInstance();
			c.setTime(note.getDate());
			date.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH));
			visit.setChecked(note.isVisitAgain());
			findViewById(android.R.id.content).post(new Runnable() {
				@Override
				public void run() {
					displayImage(note.getImage());
				}
			});
		}
	}

	/**
	 * Checks if required fields are filled
	 */
	private boolean isValid() {
		boolean valid = true;
		if (title.getText().toString().length() == 0) {
			this.title.setError("Title is required!");
			valid = false;
		} else
			this.title.setError(null);

		if (address.getText().toString().length() == 0) {
			this.address.setError("Address is required!");
			valid = false;
		} else
			this.address.setError(null);

		return valid;
	}

	/**
	 * Saves note (edits or creates new one)
	 */
	private void save() {
		if (!isValid())
			return;
		String title = this.title.getText().toString();
		String address = this.address.getText().toString();
		String description = this.description.getText().toString();

		Date date = null;
		try {
			date = Service.convertDate(this.date.getDayOfMonth() + "/"
					+ (this.date.getMonth() + 1) + "/" + this.date.getYear());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		boolean visit = this.visit.isChecked();

		if (this.note == null) {
			this.note = new Note(title, address, description, date, visit,
					selectedImage);
			Service.getInstance(this).addNote(note);
		} else {
			note.setTitle(title);
			note.setAddress(address);
			note.setDescription(description);
			note.setDate(date);
			note.setVisitAgain(visit);
			note.setImage(selectedImage);

			Service.getInstance(this).updateNote(note);
		}

		Intent result = new Intent();
		result.putExtra("note", note);
		this.setResult(RESULT_OK, result);
		finish();
	}

	private void cancel() {
		this.setResult(RESULT_CANCELED);
		finish();
	}

	private void detachImage() {
		ImageView iv = (ImageView) findViewById(IMAGE_ID);
		if (iv != null) {
			layout.removeView(iv);
			selectedImage = null;
		}
	}

	/**
	 * Creates image view, finds exact size, adds to linear layout
	 * 
	 * @param path
	 *            Path to assets or to place in file system
	 */
	private void displayImage(String path) {
		selectedImage = path;
		if (path != null && path.length() != 0) {
			AssetManager am = getAssets();
			Bitmap image = null;

			image = Service.getInstance(this).decodeImage(path,
					layout.getWidth(), am);
			if (image != null) {
				float w = image.getWidth();
				float h = image.getHeight();

				float scWidth = layout.getWidth();

				ImageView img = new ImageView(this);
				img.setId(IMAGE_ID);
				img.setImageBitmap(image);

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						(int) scWidth, (int) (scWidth * (h / w)));
				lp.setMargins(0, 10, 0, 10);
				img.setLayoutParams(lp);
				img.setScaleType(ScaleType.FIT_CENTER);
				layout.addView(img);
			}
		}
	}

}
