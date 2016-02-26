package com.example.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.main.calendar.CalendarPicker;
import com.example.main.configuration.TrainingConfiguration;
import com.example.main.configuration.TrainingConfigurationNew;
import com.example.main.graphs.Graphs;
import com.example.main.movies.MoviesList;
import com.example.main.settings.TrainingSettingsFragment;
import com.example.main.training.TrainingList;
import com.example.main.util.CustomDrawerAdapter;
import com.example.main.util.DrawerItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;

	List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initializing
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Add Drawer Item to dataList
		dataList.add(new DrawerItem(getString(R.string.menu_item1), R.drawable.ic_action_email));
		dataList.add(new DrawerItem(getString(R.string.menu_item2), R.drawable.ic_action_good));
		dataList.add(new DrawerItem(getString(R.string.menu_item3), R.drawable.ic_action_gamepad));
		dataList.add(new DrawerItem(getString(R.string.menu_item4), R.drawable.ic_action_cloud));
		dataList.add(new DrawerItem(getString(R.string.menu_item5), R.drawable.ic_action_camera));
		dataList.add(new DrawerItem(getString(R.string.menu_item6), R.drawable.ic_action_camera));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			SelectItem(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SelectItem(int possition) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (possition) {
			case 0:
				fragment = new TrainingList();
				break;
			case 1:
				fragment = new TrainingConfiguration();
				break;
			case 2:
				fragment = new CalendarPicker();
				break;
			case 3:
				fragment = new Graphs();
				break;
			case 4:
				fragment = new MoviesList();
				break;
			case 5:
				fragment = new TrainingConfigurationNew();
				break;
			default:
				break;
		}

		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();

		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {   // Settings
			case R.id.action_settings:
				Toast.makeText(this, "Hurraaa!", Toast.LENGTH_SHORT).show();
				Fragment fragment = new TrainingSettingsFragment();
				FragmentManager frgManager = getFragmentManager();
				frgManager.beginTransaction().replace(R.id.content_frame, fragment)
						.commit();

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

//		return false;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			SelectItem(position);

		}
	}

}