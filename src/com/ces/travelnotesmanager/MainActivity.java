package com.ces.travelnotesmanager;

import java.text.ParseException;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ces.travelnotesmanager.NotesFragment.OnFragmentInteractionListener;
import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

public class MainActivity extends Activity implements
		OnFragmentInteractionListener {

	NotesFragment nFrag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences settings = getSharedPreferences("TravelNotesPrefs", 0);
		
		if (settings.getBoolean("FirstTime", true)) {
			try {
				Service.getInstance(this).createSomeObjects();
			} catch (ParseException e) {
				e.printStackTrace();
			}

		    settings.edit().putBoolean("FirstTime", false).commit();
		    Log.d("Log", "First launch. Creating default objects");
		}
		else{
			Log.d("Log", "Not a first launch.");
		}
		
		
		if (findViewById(R.id.view_container) != null) {
			nFrag = new NotesFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.view_container, nFrag);
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.create_note:
			Intent intent = new Intent(this, CreateNoteActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, 1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		refresh();
	}

	@Override
	public void onFragmentInteraction(String id) {
		
	}

	public void refresh(){
		nFrag.refreshList();
	}

	@Override
	public void onNoteSelected(Note n) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ShowNoteActivity.class);
		intent.putExtra("note", n);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, 1);
	}
}
