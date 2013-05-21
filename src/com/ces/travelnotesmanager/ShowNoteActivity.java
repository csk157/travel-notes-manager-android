package com.ces.travelnotesmanager;

import java.text.ParseException;
import java.util.Locale;

import com.ces.travelnotesmanager.NotesFragment.OnFragmentInteractionListener;
import com.ces.travelnotesmanager.R.id;
import com.ces.travelnotesmanager.dao.Dao;
import com.ces.travelnotesmanager.model.Note;
import com.ces.travelnotesmanager.service.Service;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowNoteActivity extends FragmentActivity implements
		ActionBar.TabListener,
		ShowNoteInfoFragment.OnFragmentInteractionListener,
		ShowNoteMapFragment.OnFragmentInteractionListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private final static int INFO_TAB = 0;
	private final static int MAP_TAB = 1;

	private Note note;
	private ShowNoteInfoFragment info;
	private ShowNoteMapFragment map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_note);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		Tab info = actionBar.newTab();
		info.setText(R.string.noteInfoTab);
		info.setTabListener(this);

		Tab map = actionBar.newTab();
		map.setText(R.string.noteMapTab);
		map.setTabListener(this);

		actionBar.addTab(info);
		actionBar.addTab(map);
		
		note = (Note) getIntent().getExtras().getSerializable("note");
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		note = Service.getInstance(this).reloadNote(note);
		info.updateNote(note);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.edit_note:
			Intent intent = new Intent(this, CreateNoteActivity.class);
			intent.putExtra("note", note);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, 1);
			return true;
		case R.id.delete_note:
			Service.getInstance(this).removeNote(note);
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fr;
			switch (position) {
			case INFO_TAB:
				info = new ShowNoteInfoFragment();
				fr = info;
				break;
			case MAP_TAB:
				map = new ShowNoteMapFragment();
				fr = map;
				break;
			default:
				return null;
			}

			Bundle args = new Bundle();
			args.putSerializable("note", note);
			fr.setArguments(args);

			return fr;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case INFO_TAB:
				return getString(R.string.noteInfoTab).toUpperCase(l);
			case MAP_TAB:
				return getString(R.string.noteMapTab).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub

	}
}
