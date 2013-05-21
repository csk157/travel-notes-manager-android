package com.ces.travelnotesmanager;

import java.text.ParseException;

import com.ces.travelnotesmanager.NotesFragment.OnFragmentInteractionListener;
import com.ces.travelnotesmanager.service.Service;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements
		OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Service.getInstance();
		if (findViewById(R.id.view_container) != null) {
			NotesFragment nFrag = new NotesFragment();
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
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, CreateNoteActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onFragmentInteraction(String id) {
		// TODO Auto-generated method stub

	}

}
