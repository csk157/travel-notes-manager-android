package com.ces.travelnotesmanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public static final String COLUMN_ID = "_id";

	private static final String DATABASE_NAME = "travelnotesmanager.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table products (_id integer primary key autoincrement,"
				+ "title text not null,"
				+ "address text not null,"
				+ "description integer not null,"
				+ "date date not null,"
				+ "visit_again text not null)");

		db.execSQL("create table items (_id integer primary key autoincrement,"
				+ "product_id long not null," + "amount integer not null)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
