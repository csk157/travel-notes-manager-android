package com.ces.travelnotesmanager.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ces.travelnotesmanager.TravelNotesContentProvider;
import com.ces.travelnotesmanager.dao.Dao;
import com.ces.travelnotesmanager.model.Note;

public class Service {
	private static Service instance = null;
	private static SimpleDateFormat dateFormat;
	private Context context;

	private Service(Context c) {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		context = c;
	}

	public static Service getInstance(Context c) {
		if (instance == null)
			instance = new Service(c);

		return instance;
	}

	public static Service getInstance() throws Exception {
		if (instance == null)
			throw new Exception("First time should be called with context");

		return instance;
	}

	public static Date convertDate(String d) throws ParseException {
		return dateFormat.parse(d);
	}

	public static String formatDate(Date d) {
		return dateFormat.format(d);
	}

	public void createSomeObjects() throws ParseException {
		addNote(new Note("Madrid", "Madrid, Spain", "Warm and sunny",
				convertDate("20/07/2011"), true));
		addNote(new Note("Alicante", "Alicante, Spain", "Sea, sun, litter",
				convertDate("22/07/2011"), false));
		addNote(new Note("Marseille", "Marseille, France", "Rocks, sea, warm",
				convertDate("15/10/2012"), true));
	}

	private Note buildNoteFromCursor(Cursor c) {
		long id = c.getLong(c.getColumnIndex("_id"));
		String title = c.getString(c.getColumnIndex("title"));
		String address = c.getString(c.getColumnIndex("address"));
		String description = c.getString(c.getColumnIndex("description"));
		Date date = null;
		try {
			date = convertDate(c.getString(c.getColumnIndex("date")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		boolean visit = c.getInt(c.getColumnIndex("visit_again")) == 1;

		Note n = new Note(title, address, description, date, visit);
		n.setId(id);
		return n;
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

		return notes;
	}

	private ContentValues buildValuesFromNote(Note n) {
		ContentValues values = new ContentValues();
		values.put("title", n.getTitle());
		values.put("address", n.getAddress());
		values.put("description", n.getDescription());
		values.put("date", formatDate(n.getDate()));
		values.put("visit_again", n.isVisitAgain() ? 1 : 0);

		return values;
	}

	public Note addNote(Note n) {
		ContentValues values = buildValuesFromNote(n);
		Uri u = context.getContentResolver().insert(TravelNotesContentProvider.CONTENT_URI, values);
		long nid = Long.parseLong(u.getLastPathSegment());
		//		long nid = Dao.getInstance(context).addNote(values);
		n.setId(nid);
		return n;
	}

	public Note updateNote(Note n) {
		ContentValues values = buildValuesFromNote(n);
		Uri u = Uri.parse(TravelNotesContentProvider.CONTENT_URI + "/" + n.getId());
		context.getContentResolver().update(u, values, null, null);
//		Dao.getInstance(context).updateNote(values, n.getId());
		return n;
	}

	public void removeNoteById(long id) {
//		Dao.getInstance(context).removeNoteById(id);
		Uri u = Uri.parse(TravelNotesContentProvider.CONTENT_URI + "/" + id);
		context.getContentResolver().delete(u, null, null);
	}

	public Note findNoteById(long id) {
		Uri u = Uri.parse(TravelNotesContentProvider.CONTENT_URI + "/" + id);
		Cursor c = context.getContentResolver()
				.query(u, null, null, null, null);
		// Cursor c = Dao.getInstance(context).findNoteById(id);
		c.moveToFirst();
		return buildNoteFromCursor(c);
	}

	public Note reloadNote(Note n) {
		n = findNoteById(n.getId());
		return n;
	}

	public Note removeNote(Note n) {
		removeNoteById(n.getId());
		return n;
	}

}
