package rod.bailey.trafficatnsw

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import rod.bailey.trafficatnsw.cameras.TrafficCameraListFragment
import rod.bailey.trafficatnsw.hazard.HazardListFragment
import rod.bailey.trafficatnsw.hazard.HazardListMode
import rod.bailey.trafficatnsw.traveltime.TravelTimesFragment
import rod.bailey.trafficatnsw.util.ConfigSingleton
import rod.bailey.trafficatnsw.util.MLog

class MainActivity : AppCompatActivity() {
	private lateinit var drawerToggle: ActionBarDrawerToggle

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		drawerToggle.onConfigurationChanged(newConfig)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Initialize config facility
		val config = ConfigSingleton.instance
		config.init(CONFIG_PROPERTIES_FILE_NAME, this)

		// This logging facility needs initialization
		MLog.init(config)

		// Init screen with main layout
		setContentView(R.layout.activity_main)

		val toolbar = findViewById(R.id.toolbar) as Toolbar
		setSupportActionBar(toolbar)

		// Link drawer toggle icon to nav drawer open/close state
		val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
		drawerToggle = ActionBarDrawerToggle(
			this,
			drawer,
			toolbar,
			R.string.app_navigation_drawer_open,
			R.string.app_navigation_drawer_close)
		drawer.addDrawerListener(drawerToggle)
		drawerToggle.syncState()

		// Navigation
		val navView = findViewById(R.id.navigation_drawer_view) as NavigationView
		navView.setNavigationItemSelectedListener(DrawerItemClickListener(drawer))
	}

	override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
		return super.onCreateView(name, context, attrs)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onPostCreate(savedInstanceState: Bundle?) {
		super.onPostCreate(savedInstanceState)
		drawerToggle.syncState()
	}

	/**
	 * Listens for a tap on an item in the navigation drawer and invokes the
	 * corresponding Fragment. Also changes the title approriately.
	 */
	inner class DrawerItemClickListener(
		private val drawerLayout: DrawerLayout) : NavigationView.OnNavigationItemSelectedListener {
		override fun onNavigationItemSelected(item: MenuItem): Boolean {
			Log.d(LOG_TAG, "onNavigationItemSelected: item=" + item)

			when (item.itemId) {
				R.id.menu_item_sydney_incidents ->
					navToHazards(HazardListMode.SYDNEY)
				R.id.menu_item_regional_nsw_incidents ->
					navToHazards(HazardListMode.REGIONAL)
				R.id.menu_item_favourite_cameras ->
					navToCameras(TrafficCameraListFragment.ARG_MODE_VALUE_FAVOURITES)
				R.id.menu_item_sydney_cameras -> navToCameras(
					TrafficCameraListFragment.ARG_MODE_VALUE_SYDNEY)
				R.id.menu_item_regional_nsw_cameras -> navToCameras(
					TrafficCameraListFragment.ARG_MODE_VALUE_REGIONAL)
				R.id.menu_item_travel_times_m1 -> navToTimes(
					TravelTimesFragment.ARG_MWAY_VALUE_M1)
				R.id.menu_item_travel_times_m2 -> navToTimes(
					TravelTimesFragment.ARG_MWAY_VALUE_M2)
				R.id.menu_item_travel_times_m3 -> navToTimes(
					TravelTimesFragment.ARG_MWAY_VALUE_M4)
				R.id.menu_item_travel_times_m4 -> navToTimes(
					TravelTimesFragment.ARG_MWAY_VALUE_M7)
			} // when

			drawerLayout.closeDrawer(Gravity.START)
			return true
		} // onNavigationItemSelected()

		private fun navToHazards(mode: HazardListMode) {
			val fragment = HazardListFragment.create(mode)
			fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
		}

		private fun navToCameras(argCameraModeValue: Int) {
			val fragment = TrafficCameraListFragment()
			val bundle = Bundle()
			bundle.putInt(TrafficCameraListFragment.ARG_MODE_KEY, argCameraModeValue)
			fragment.arguments = bundle
			val fragmentManager = fragmentManager
			fragmentManager.beginTransaction()
				.replace(R.id.content_main, fragment).commit()
		}

		private fun navToTimes(argTravelTimeValue: String) {
			val fragment = TravelTimesFragment.create(argTravelTimeValue)
			fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
		}
	} // class DrawItemClickListener

	companion object {
		private val CONFIG_PROPERTIES_FILE_NAME = "config.properties"
		private val LOG_TAG = MainActivity::class.java.simpleName

	}
}
