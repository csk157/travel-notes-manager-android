package com.ces.travelnotesmanager.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.ces.travelnotesmanager.TravelNotesContentProvider;
import com.ces.travelnotesmanager.model.Note;

public class Service {
	private static Service instance = null;
	private static SimpleDateFormat dateFormat;
	private Context context;
	private int showFilter = 0;

	@SuppressLint("SimpleDateFormat")
	private Service(Context c) {
		// Forcing date format, to be used in application
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		context = c;
	}

	public static Service getInstance(Context c) {
		if (instance == null)
			instance = new Service(c);

		return instance;
	}

	/**
	 * Getting instance without using context. Should have been called with
	 * context before.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Service getInstance() throws Exception {
		if (instance == null)
			throw new Exception("First time should be called with context");

		return instance;
	}

	/**
	 * Creates date object from string
	 * 
	 * @param String
	 *            d Date in format of dd/MM/yyyy
	 * @return
	 * @throws ParseException
	 */
	public static Date convertDate(String d) throws ParseException {
		return dateFormat.parse(d);
	}

	/**
	 * Creates string from date in format of dd/MM/yyyy
	 * 
	 * @param d
	 * @return
	 */
	public static String formatDate(Date d) {
		return dateFormat.format(d);
	}

	/**
	 * Creates some objects, made to be called when launching first time
	 * 
	 * @throws ParseException
	 */
	public void createSomeObjects() throws ParseException {
		addNote(new Note(
				"Sunny Madrid",
				"Madrid, Spain",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("20/07/2011"), true,
				"file:///android_asset/images/madrid.jpg"));
		addNote(new Note(
				"Dirty Alicante",
				"Alicante, Spain",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("22/07/2011"), false,
				"file:///android_asset/images/alicante.jpg"));
		addNote(new Note(
				"Rocky Marseille",
				"Marseille, France",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("15/10/2012"), true,
				"file:///android_asset/images/marseille.jpg"));
		addNote(new Note(
				"Shiny Paris",
				"Paris, France",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("19/10/2012"), false,
				"file:///android_asset/images/paris.jpg"));
		addNote(new Note(
				"Incredible Iceland",
				"Reykjavik, Iceland",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("10/03/2012"), true,
				"file:///android_asset/images/iceland.jpg"));
		addNote(new Note(
				"OK London",
				"London, UK",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("12/03/2013"), false));
		addNote(new Note(
				"Big Apple",
				"New York, US",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("12/03/2003"), true));
		addNote(new Note(
				"OK Minsk",
				"Minsk, Belarus",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("19/04/2005"), false));
		addNote(new Note(
				"Not so OK Palanga",
				"Palanga, Lithuania",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("19/07/2008"), false));
		addNote(new Note(
				"Great Torronto",
				"Torronto, Canada",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("25/08/2009"), true));
		addNote(new Note(
				"Average Berlin",
				"Berlin, Germany",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("25/09/2010"), false));
		addNote(new Note(
				"Nice Hamburg",
				"Hamburg, Germany",
				"Aliquam erat volutpat. Duis sodales lacus augue, id venenatis elit. Nulla tempus, turpis quis aliquet congue, odio massa congue odio, sit amet consectetur ligula ipsum ultricies tortor. Nulla ullamcorper vestibulum aliquet. Sed dui velit, accumsan at iaculis ut, iaculis in massa. Quisque lacus leo, ullamcorper sit amet lobortis eget, lobortis sed lectus. Nunc tristique bibendum volutpat.",
				convertDate("29/09/2010"), true));
	}

	public List<Note> getAllNotes() {
		Cursor c = context.getContentResolver().query(
				TravelNotesContentProvider.CONTENT_URI, null, null, null, null);
		ArrayList<Note> notes = new ArrayList<Note>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			notes.add(buildNoteFromCursor(c));
			c.moveToNext();
		}
		c.close();

		return notes;
	}

	public List<Note> getVisitNotes() {
		Cursor c = context.getContentResolver().query(
				TravelNotesContentProvider.CONTENT_URI, null,
				"visit_again = 1", null, null);
		ArrayList<Note> notes = new ArrayList<Note>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			notes.add(buildNoteFromCursor(c));
			c.moveToNext();
		}
		c.close();
		return notes;
	}

	public List<Note> getDontVisitNotes() {
		Cursor c = context.getContentResolver().query(
				TravelNotesContentProvider.CONTENT_URI, null,
				"visit_again = 0", null, null);
		ArrayList<Note> notes = new ArrayList<Note>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			notes.add(buildNoteFromCursor(c));
			c.moveToNext();
		}
		c.close();
		return notes;
	}

	public Note addNote(Note n) {
		ContentValues values = buildValuesFromNote(n);
		Uri u = context.getContentResolver().insert(
				TravelNotesContentProvider.CONTENT_URI, values);
		long nid = Long.parseLong(u.getLastPathSegment());
		n.setId(nid);
		return n;
	}

	public Note updateNote(Note n) {
		ContentValues values = buildValuesFromNote(n);
		Uri u = Uri.parse(TravelNotesContentProvider.CONTENT_URI + "/"
				+ n.getId());
		context.getContentResolver().update(u, values, null, null);
		return n;
	}

	public void removeNoteById(long id) {
		// Dao.getInstance(context).removeNoteById(id);
		Uri u = Uri.parse(TravelNotesContentProvider.CONTENT_URI + "/" + id);
		context.getContentResolver().delete(u, null, null);
	}

	public Note findNoteById(long id) {
		Uri u = Uri.parse(TravelNotesContentProvider.CONTENT_URI + "/" + id);
		Cursor c = context.getContentResolver()
				.query(u, null, null, null, null);
		c.moveToFirst();
		Note n = buildNoteFromCursor(c);
		c.close();
		return n;
	}

	public Note reloadNote(Note n) {
		n = findNoteById(n.getId());
		return n;
	}

	public Note removeNote(Note n) {
		removeNoteById(n.getId());
		return n;
	}

	public int getShowFilter() {
		return showFilter;
	}

	public void setShowFilter(int showFilter) {
		this.showFilter = showFilter;
	}

	/**
	 * Creates Bitmap object from path. Also reduces size, so it takes less
	 * memory
	 * 
	 * @param path
	 *            String containing path to asset or to image in file system
	 * @param maxWidth
	 *            Maximum width of an image (will be resized according to this)
	 * @param am
	 *            Asset Manager for getting image from asset folder
	 * @return Bitmap or null if failed
	 */
	public Bitmap decodeImage(String path, int maxWidth, AssetManager am) {
		InputStream istr;
		try {
			if (path.contains("file:///android_asset/images/")) {
				istr = am.open(path.replace("file:///android_asset/", ""));
			} else {
				istr = new BufferedInputStream(new FileInputStream(path));
			}

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(istr, null, o);


			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / (float) scale / 2 >= maxWidth)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap b = null;
			if (path.contains("file:///android_asset/images/"))
				b = BitmapFactory.decodeStream(istr, null, o2);
			else
				b = BitmapFactory.decodeFile(path, o2); // Decode stream does not
														// work well
			istr.close();

			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Building ContentValues from Note object
	 * 
	 * @param n
	 * @return
	 */
	private ContentValues buildValuesFromNote(Note n) {
		ContentValues values = new ContentValues();
		values.put("title", n.getTitle());
		values.put("address", n.getAddress());
		values.put("description", n.getDescription());
		values.put("date", formatDate(n.getDate()));
		values.put("visit_again", n.isVisitAgain() ? 1 : 0);
		values.put("image", n.getImage());

		return values;
	}

	/**
	 * Building note object from cursor
	 * 
	 * @param c
	 * @return
	 */
	private Note buildNoteFromCursor(Cursor c) {
		long id = c.getLong(c.getColumnIndex("_id"));
		String title = c.getString(c.getColumnIndex("title"));
		String address = c.getString(c.getColumnIndex("address"));
		String description = c.getString(c.getColumnIndex("description"));
		String image = c.getString(c.getColumnIndex("image"));
		Date date = null;
		try {
			date = convertDate(c.getString(c.getColumnIndex("date")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean visit = c.getInt(c.getColumnIndex("visit_again")) == 1;

		Note n = new Note(title, address, description, date, visit, image);
		n.setId(id);
		return n;
	}
}
