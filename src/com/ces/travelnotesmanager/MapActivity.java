package com.ces.travelnotesmanager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class MapActivity extends Activity {
	private WebView map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String address = getIntent().getExtras().getString("address");
		setTitle("Map - " + address);

		map = (WebView) findViewById(R.id.mapView);
		WebSettings webSettings = map.getSettings();
		webSettings.setJavaScriptEnabled(true); // Enabling javascript, Lint
												// does not like it though

		// Loading file in assets folder, and adding parameters to url
		map.loadUrl("file:///android_asset/map/index.html?address=" + address);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
