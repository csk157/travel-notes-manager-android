package com.ces.travelnotesmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Dao {
	private SQLiteDatabase database;
	private DbHelper dbHelper;

	private static Dao instance;

	private Dao(Context c) {
		dbHelper = new DbHelper(c);
		database = dbHelper.getWritableDatabase();
	}

	public static Dao getInstance(Context c) {
		if (instance == null)
			instance = new Dao(c);

		return instance;
	}

	public Cursor getAllNotes() {
		Cursor cursor = database.query("notes", null, null, null, null, null,
				null);
		return cursor;
	}

	public long addNote(ContentValues values) {
		long id = database.insert("notes", null, values);
		return id;
	}

	public void removeNoteById(long id) {
		database.delete("notes", "_id = ?", new String[] { id + "" });
	}

	public void updateNote(ContentValues values, long id) {
		database.update("notes", values, "_id = ?", new String[] { id + "" });
	}

	public Cursor findNoteById(long id) {
		Cursor cursor = database.query("notes", null, "_id = ?",
				new String[] { id + "" }, null, null, null, 1 + "");
		return cursor;
	}
}
