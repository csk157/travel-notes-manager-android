package com.ces.travelnotesmanager.dao;

import java.util.ArrayList;

import com.ces.travelnotesmanager.model.Note;


public class Dao {
//	private SQLiteDatabase database;
//	private DbHelper dbHelper;

	private static Dao instance;
	private ArrayList<Note> notes;
	
	private Dao(/*Context c*/) {
//		dbHelper = new DbHelper(c);
//		database = dbHelper.getWritableDatabase();
		notes = new ArrayList<Note>();
	}

	public static Dao getInstance(/*Context c*/) {
		if (instance == null)
			instance = new Dao(/*c*/);

		return instance;
	}
	
	public void addNote(Note n){
		notes.add(n);
	}
	
	public void removeNote(Note n){
		notes.remove(n);
	}
	
	public ArrayList<Note> getAllNotes(){
		return new ArrayList<Note>(notes);
	}
	
}
