package rod.bailey.trafficatnsw.app

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import rod.bailey.trafficatnsw.BuildConfig
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.overview.TrafficCameraListFragment
import rod.bailey.trafficatnsw.cameras.overview.TrafficCameraListMode
import rod.bailey.trafficatnsw.hazard.overview.HazardListFragment
import rod.bailey.trafficatnsw.hazard.overview.HazardOverviewMode
import rod.bailey.trafficatnsw.traveltime.data.Motorway
import rod.bailey.trafficatnsw.traveltime.overview.TravelTimesFragment
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject


@EActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	private lateinit var drawerToggle: ActionBarDrawerToggle

	@Inject
	lateinit var config: ConfigSingleton

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		drawerToggle.onConfigurationChanged(newConfig)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Initialize config facility
		config.init(CONFIG_PROPERTIES_FILE_NAME, this)

		// This logging facility needs initialization. Log messages only
		// appear in DEBUG, not in production.
//		if (BuildConfig.DEBUG) {
			Timber.plant(DebugTree())
//		}
	}

	@AfterViews
	fun afterViews() {
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

		// Navigation View (drawer)
		val navView = findViewById(R.id.navigation_drawer_view) as NavigationView
		navView.setNavigationItemSelectedListener(DrawerItemClickListener(drawer))

		// Load the Sydney Incidents screen as the first screen the user sees on startup
		navigateToHazardsScreen(HazardOverviewMode.SYDNEY)
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

	private fun navigateToHazardsScreen(mode: HazardOverviewMode) {
		val fragment = HazardListFragment.create(mode)
		fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
	}

	private fun navigateToCamerasScreen(argCameraModeValue: TrafficCameraListMode) {
		val fragment = TrafficCameraListFragment.create(argCameraModeValue)
		fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
	}

	private fun navigateToTravelTimesScreen(motorway: Motorway) {
		val fragment = TravelTimesFragment.create(motorway)
		fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
	}

	/**
	 * Listens for a tap on an item in the navigation drawer and invokes the
	 * corresponding Fragment. Also changes the title approriately.
	 */
	inner class DrawerItemClickListener(
			private val drawerLayout: DrawerLayout) : NavigationView.OnNavigationItemSelectedListener {

		override fun onNavigationItemSelected(item: MenuItem): Boolean {
			Timber.d("onNavigationItemSelected: item=${item}")
			when (item.itemId) {
				R.id.menu_item_sydney_incidents ->
					navigateToHazardsScreen(HazardOverviewMode.SYDNEY)
				R.id.menu_item_regional_nsw_incidents ->
					navigateToHazardsScreen(HazardOverviewMode.REGIONAL)
				R.id.menu_item_favourite_cameras ->
					navigateToCamerasScreen(TrafficCameraListMode.FAVOURITES)
				R.id.menu_item_sydney_cameras -> navigateToCamerasScreen(
						TrafficCameraListMode.SYDNEY)
				R.id.menu_item_regional_nsw_cameras -> navigateToCamerasScreen(
						TrafficCameraListMode.REGIONAL)
				R.id.menu_item_travel_times_m1 -> navigateToTravelTimesScreen(Motorway.M1)
				R.id.menu_item_travel_times_m4 -> navigateToTravelTimesScreen(Motorway.M4)
				R.id.menu_item_travel_times_m7 -> navigateToTravelTimesScreen(Motorway.M7)
			} // when

			drawerLayout.closeDrawer(Gravity.START)
			return true
		} // onNavigationItemSelected()
	} // class DrawItemClickListener

	companion object {
		private val CONFIG_PROPERTIES_FILE_NAME = "config.properties"
		private val LOG_TAG = MainActivity::class.java.simpleName
	}
}
