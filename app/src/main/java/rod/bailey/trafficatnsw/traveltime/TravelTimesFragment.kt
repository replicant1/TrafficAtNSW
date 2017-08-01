package rod.bailey.trafficatnsw.traveltime

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.traveltime.common.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.common.TravelTimesSingleton
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig
import rod.bailey.trafficatnsw.ui.predicate.InactiveTravelTimeEmptyMessagePredicate
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.util.MLog

class TravelTimesFragment : Fragment(), PropertyChangeListener {
	private var mainLayout: ListViewWithEmptyMessage? = null
	/** Travel times for the motorway currently being displayed  */
	var db: MotorwayTravelTimesDatabase? = null
	/** Config for the motorway currently being display  */
	private var travelTimeConfig: TravelTimeConfig? = null

	private fun createUI() {
		MLog.i(LOG_TAG, "Into TravelTimesFragment.createUI()")
		val ctx = activity
		mainLayout = ListViewWithEmptyMessage_.build(ctx, EMPTY_MESSAGE,
													 InactiveTravelTimeEmptyMessagePredicate())
		setHasOptionsMenu(true)

		activity.title = travelTimeConfig!!.motorwayName + " Travel Times"

		refreshAsync()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val args = arguments

		if (args != null) {
			if (args.containsKey(ARG_MWAY_KEY)) {
				val value = args.getString(ARG_MWAY_KEY)
				val singleton = TravelTimesSingleton.singleton
				singleton.init(activity)

				if (ARG_MWAY_VALUE_M1 == value) {
					travelTimeConfig = singleton.m1Config
				} else if (ARG_MWAY_VALUE_M2 == value) {
					travelTimeConfig = singleton.m2Config
				} else if (ARG_MWAY_VALUE_M4 == value) {
					travelTimeConfig = singleton.m4Config
				} else if (ARG_MWAY_VALUE_M7 == value) {
					travelTimeConfig = singleton.m7Config
				}
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		createUI()
		refreshAsync()
		return mainLayout
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.menu_travel_times_options, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null) {
			if (item.itemId == R.id.refresh_travel_times) {
				refreshAsync()
			}
		}
		return true
	}

	private fun refreshAsync() {
		MLog.i(LOG_TAG, "Refreshing travel times")
		val task = DownloadTravelTimesTask(activity, this, travelTimeConfig,
																			  mainLayout)
		task.execute()
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		MLog.i(LOG_TAG,
			   "TravelTimesFragment gets notice that property "
				   + event.propertyName + " has changed")
		if (event.propertyName == MotorwayTravelTimesDatabase.PROPERTY_TOTAL_TRAVEL_TIME) {
			if (db != null) {
				mainLayout!!.setAdapter(TravelTimesListAdapter(db))
			}
		}
	}

	companion object {
		private val EMPTY_MESSAGE = "Travel Times for this motorway are unavailable at the moment."
		val ARG_MWAY_KEY = "MWAY"
		val ARG_MWAY_VALUE_M1 = "M1"
		val ARG_MWAY_VALUE_M2 = "M2"
		val ARG_MWAY_VALUE_M4 = "M4"
		val ARG_MWAY_VALUE_M7 = "M7"
		private val LOG_TAG = TravelTimesFragment::class.java.simpleName

		fun create(mwayKey: String): TravelTimesFragment {
			val result = TravelTimesFragment()
			val bundle = Bundle()
			bundle.putString(ARG_MWAY_KEY, mwayKey)
			MLog.d(LOG_TAG, "Creating TravelTimesFragment,key=${mwayKey}")
			result.arguments = bundle
			return result
		}
	}
}