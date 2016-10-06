package rod.bailey.trafficatnsw;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import rod.bailey.trafficatnsw.cameras.TrafficCameraListFragment;
import rod.bailey.trafficatnsw.hazard.HazardsFragment;
import rod.bailey.trafficatnsw.traveltime.TravelTimesFragment;
import rod.bailey.trafficatnsw.util.ConfigSingleton;
import rod.bailey.trafficatnsw.util.MLog;

import static rod.bailey.trafficatnsw.cameras.TrafficCameraListFragment.*;
import static rod.bailey.trafficatnsw.traveltime.TravelTimesFragment.*;

public class MainActivity extends AppCompatActivity {

    /* Width of the navigation drawer when fully extended */
    private static final int DRAWER_WIDTH_DP = 160;

    private static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    private static final int CONTENT_FRAME_ID = 1010;

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout drawer;

    private ActionBarDrawerToggle drawerToggle;

    private ListView navList;

//	private void createUI() {
//		drawer = new DrawerLayout(this);
//		drawer.setDrawerShadow(R.drawable.drawer_shadow, LEFT);
//
//		// 'content is the bit that is NOT the nav drawer
//		final FrameLayout contentframeLayout = new FrameLayout(this);
//		contentframeLayout.setId(CONTENT_FRAME_ID);
//		contentframeLayout.setBackgroundColor(DisplayUtils
//				.viewBackgroundColor());
//
//		// List of items that appear in the nav drawer
//		navList = new ListView(this);
//		navList.setDividerHeight(0);
//		navList.setAdapter(new NavigationListAdapter(this));
//		navList.setBackgroundColor(WHITE);
//		DrawerItemClickListener navClickListener = new DrawerItemClickListener();
//		navList.setOnItemClickListener(navClickListener);
//
//		DrawerLayout.LayoutParams navListDLP = new DrawerLayout.LayoutParams(
//				dp2Px(this, DRAWER_WIDTH_DP), MATCH_PARENT);
//		navListDLP.gravity = Gravity.START;
//
//		navList.setLayoutParams(navListDLP);
//
//		// Drawer
//		drawer.addView(contentframeLayout, new FrameLayout.LayoutParams(
//				WRAP_CONTENT, MATCH_PARENT));
//		drawer.addView(navList);
//
//		drawerToggle = new ActionBarDrawerToggle(this, drawer,
//												 R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
//		drawer.setDrawerListener(drawerToggle);
//
//		setContentView(drawer);
//
//		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
////		getActionBar().setDisplayHomeAsUpEnabled(true);
////		getActionBar().setHomeButtonEnabled(true);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//
//		// Start by auto-navigating to the "Incidents in Sydney" - which will
//		// probably be
//		// the most commonly used screen.
//		// Is it OK to do this now, or should I wait a while for the UI to
//		// finish setup>?
//		navClickListener.selectItem(0);
//	}

    // A method to find height of the status bar
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigSingleton config = ConfigSingleton.getInstance();
        config.init(CONFIG_PROPERTIES_FILE_NAME, this);

        MLog.init(config, this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return false;
            }
        });

        View topLevel = findViewById(R.id.top_level_linear_layout);

        Log.i(TAG, "Status bar height is " + getStatusBarHeight());

//		topLevel.setPadding(0, // left
//						   getStatusBarHeight(), //top
//						   0, // right
//						   0); // bottom
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//		drawerToggle.syncState();
    }

    /**
     * Listens for a tap on an item in the navigation drawer and invokes the
     * corresponding Fragment. Also changes the title approriately.
     */
    public class DrawerItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            // Create a new fragment and specify the planet to show based on
            // position

            int argCameraModeValue = TrafficCameraListFragment.ARG_MODE_VALUE_ALL;
            String argTravelTimeValue = null;
            int argHazardModeValue = HazardsFragment.ARG_MODE_VALUE_SYDNEY;

            switch (position) {
                // Hazards
                case 0:
                case 1:
                    argHazardModeValue = HazardsFragment.ARG_MODE_VALUE_SYDNEY;
                    break;
                case 2:
                    argHazardModeValue = HazardsFragment.ARG_MODE_VALUE_REGIONAL;
                    break;

                // Cameras
                case 3:
                case 4:
                    argCameraModeValue = TrafficCameraListFragment.ARG_MODE_VALUE_FAVOURITES;
                    break;
                case 5:
                    argCameraModeValue = TrafficCameraListFragment.ARG_MODE_VALUE_SYDNEY;
                    break;
                case 6:
                    argCameraModeValue = TrafficCameraListFragment.ARG_MODE_VALUE_REGIONAL;
                    break;

                // Travel Times
                case 7:
                case 8:
                    argTravelTimeValue = TravelTimesFragment.ARG_MWAY_VALUE_M1;
                    break;

                case 9:
                    argTravelTimeValue = TravelTimesFragment.ARG_MWAY_VALUE_M2;
                    break;

                case 10:
                    argTravelTimeValue = TravelTimesFragment.ARG_MWAY_VALUE_M4;
                    break;

                case 11:
                    argTravelTimeValue = TravelTimesFragment.ARG_MWAY_VALUE_M7;
                    break;
            }

            // NOTE: If I could find a way of retrieving the fragments (if
            // previously created) from the FragmentManager Iwouldn't have to
            // "new" the fragment every time.
            // This would prevent the data from reloading in the fragment's
            // onCreate() method every time, which would perhaps be preferable.
            // Try fragmentManager.beginTransaction().replace(int, Fragment,S
            // tring tag). Then you can retrieve the fragment by "tag"

            // Selected an item in the Incidents section of the nav list
            if ((position >= 0) && (position <= 2)) {
                Fragment fragment = new HazardsFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(ARG_MODE_KEY, argHazardModeValue);
                fragment.setArguments(bundle);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(CONTENT_FRAME_ID, fragment).commit();
            }

            // Selected an item in the Traffic Cameras section of the list
            if ((position >= 3) && (position <= 6)) {
                Fragment fragment = new TrafficCameraListFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(ARG_MODE_KEY, argCameraModeValue);
                fragment.setArguments(bundle);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(CONTENT_FRAME_ID, fragment).commit();
            }

            // Selected an item in the Trvel Times section of the list
            if ((position >= 7) && (position <= 11)) {
                Fragment fragment = new TravelTimesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ARG_MWAY_KEY, argTravelTimeValue);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(CONTENT_FRAME_ID, fragment).commit();
            }

            // Highlight the selected item, update the title, and close the
            // drawer
            navList.setItemChecked(position, true);
            drawer.closeDrawer(navList);
        }
    }

}
