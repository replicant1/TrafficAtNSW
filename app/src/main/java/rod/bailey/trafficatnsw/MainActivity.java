package rod.bailey.trafficatnsw;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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

    private static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    @SuppressWarnings("unused")
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ActionBarDrawerToggle drawerToggle;

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

        NavigationView navView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        navView.setNavigationItemSelectedListener(new DrawerItemClickListener(drawer));

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);
        return view;
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
    public class DrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        private DrawerLayout drawerLayout;

        public DrawerItemClickListener(DrawerLayout drawerLayout) {
            this.drawerLayout = drawerLayout;
        }

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            Log.d(LOG_TAG, "onNavigationItemSelected: item=" + item);

            switch (item.getItemId()) {
                case R.id.menu_item_sydney_incidents:
                    navigateToIncidents(HazardsFragment.ARG_MODE_VALUE_SYDNEY);
                    break;

                case R.id.menu_item_regional_nsw_incidents:
                    navigateToIncidents(HazardsFragment.ARG_MODE_VALUE_REGIONAL);
                    break;

                case R.id.menu_item_favourite_cameras:
                    navigateToTrafficCamera(TrafficCameraListFragment.ARG_MODE_VALUE_FAVOURITES);
                    break;

                case R.id.menu_item_sydney_cameras:
                    navigateToTrafficCamera(TrafficCameraListFragment.ARG_MODE_VALUE_SYDNEY);
                    break;

                case R.id.menu_item_regional_nsw_cameras:
                    navigateToTrafficCamera(TrafficCameraListFragment.ARG_MODE_VALUE_REGIONAL);
                    break;

                case R.id.menu_item_travel_times_m1:
                    navigateToTravelTimes(TravelTimesFragment.ARG_MWAY_VALUE_M1);
                    break;

                case R.id.menu_item_travel_times_m2:
                    navigateToTravelTimes(TravelTimesFragment.ARG_MWAY_VALUE_M2);
                    break;

                case R.id.menu_item_travel_times_m3:
                    navigateToTravelTimes(TravelTimesFragment.ARG_MWAY_VALUE_M4);
                    break;

                case R.id.menu_item_travel_times_m4:
                    navigateToTravelTimes(TravelTimesFragment.ARG_MWAY_VALUE_M7);
                    break;
            } // switch

            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;

        } // onNavigationItemSelected()

        private void navigateToIncidents(int argHazardModeValue) {
            Fragment fragment = new HazardsFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(ARG_MODE_KEY, argHazardModeValue);
            fragment.setArguments(bundle);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }

        private void navigateToTrafficCamera(int argCameraModeValue) {
            Fragment fragment = new TrafficCameraListFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(ARG_MODE_KEY, argCameraModeValue);
            fragment.setArguments(bundle);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }

        private void navigateToTravelTimes(String argTravelTimeValue) {
            Fragment fragment = new TravelTimesFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_MWAY_KEY, argTravelTimeValue);
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }
    } // class DrawItemClickListener

}
