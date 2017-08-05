package com.softaai.mpga.screens.common.controllers;
import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.softaai.mpga.*;
import com.softaai.mpga.adapter.*;
import com.softaai.mpga.model.*;
import com.softaai.mpga.retrofit.api.*;
import com.softaai.mpga.screens.common.controllers.*;
import com.softaai.mpga.utils.*;
import java.util.*;
import retrofit2.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class MainActivity1 extends AppCompatActivity
{
	private DrawerLayout mDrawerLayout;
	
	private ListView listView;
    private View parentView;

    private ArrayList<Contact> contactList;
    private MyContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		/**
         * Array List for Binding Data from JSON to this List
         */
        contactList = new ArrayList<>();

        parentView = findViewById(R.id.coordinator);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) findViewById(R.id.listView);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					menuItem.setChecked(true);
					mDrawerLayout.closeDrawers();
					Toast.makeText(MainActivity1.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
					return true;
				}
			});

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View parentView) {

					/**
					 * Checking Internet Connection
					 */
					if (InternetConnection.checkConnection(getApplicationContext())) {
						final ProgressDialog dialog;
						/**
						 * Progress Dialog for User Interaction
						 */
						dialog = new ProgressDialog(MainActivity1.this);
						dialog.setTitle(getString(R.string.string_getting_json_title));
						dialog.setMessage(getString(R.string.string_getting_json_message));
						dialog.show();

						//Creating an object of our api interface
						ApiService api = RetroClient.getApiService();

						/**
						 * Calling JSON
						 */
						Call<ContactList> call = api.getMyJSON();

						/**
						 * Enqueue Callback will be call when get response...
						 */
						call.enqueue(new Callback<ContactList>() {
								@Override
								public void onResponse(Call<ContactList> call, Response<ContactList> response) {
									//Dismiss Dialog
									dialog.dismiss();

									if(response.isSuccessful()) {
										/**
										 * Got Successfully
										 */
										contactList = response.body().getContacts();

										/**
										 * Binding that List to Adapter
										 */
										adapter = new MyContactAdapter(MainActivity1.this, contactList);
										listView.setAdapter(adapter);

									} else {
										Snackbar.make(parentView, R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
									}
								}

								@Override
								public void onFailure(Call<ContactList> call, Throwable t) {
									dialog.dismiss();
								}
							});

					} else {
						Snackbar.make(parentView, R.string.string_internet_connection_not_available, Snackbar.LENGTH_LONG).show();
					}
				}
				
			});

        DesignDemoPagerAdapter adapter = new DesignDemoPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.action_settings:
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DesignDemoFragment extends Fragment {
        private static final String TAB_POSITION = "tab_position";

        public DesignDemoFragment() {

        }

        public static DesignDemoFragment newInstance(int tabPosition) {
            DesignDemoFragment fragment = new DesignDemoFragment();
            Bundle args = new Bundle();
            args.putInt(TAB_POSITION, tabPosition);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int tabPosition = args.getInt(TAB_POSITION);

			ArrayList<String> items = new ArrayList<String>();
            for (int i = 0; i < 50; i++) {
                items.add("Tab #" + tabPosition + " item #" + i);
            }

            //View v =  inflater.inflate(R.layout.fragment_list_view, container, false);
            //RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
           // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //recyclerView.setAdapter(new BasicRecyclerAdapter(items));

            return null;
        }
    }

    static class DesignDemoPagerAdapter extends FragmentStatePagerAdapter {

        public DesignDemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
			//return DesignDemoFragment.newInstance(position);
//			if(position == 0)
//			{
//				return null;
//			}
//			else
//			{
//			 return new Fragment();
//
//			}
//			else if (position == 1)
//			{
//				return new AdvanceSettingFragment();
//			}
//			else
//			{
//				return new HelpFragment();
//			}
			return new Fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
			//  return "Tab " + position;

			if(position == 0)
			{
				return "Output";
			}
			else 
			{
				return "Settings";
			}

        }
    }

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
	}

	@Override
	public void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}


	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
	
}
