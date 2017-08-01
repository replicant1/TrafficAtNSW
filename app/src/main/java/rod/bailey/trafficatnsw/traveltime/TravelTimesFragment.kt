package rod.bailey.trafficatnsw.traveltime

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.*
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.FragmentArg
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.traveltime.common.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.common.TravelTimesSingleton
import rod.bailey.trafficatnsw.traveltime.config.Motorway
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig
import rod.bailey.trafficatnsw.ui.predicate.InactiveTravelTimeEmptyMessagePredicate
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

@EFragment
open class TravelTimesFragment : Fragment(), PropertyChangeListener {

	private var listView: ListViewWithEmptyMessage? = null

	/** Travel times for the motorway currently being displayed  */
	var db: MotorwayTravelTimesDatabase? = null

	/** Config for the motorway currently being display  */
	private var travelTimeConfig: TravelTimeConfig? = null

	@FragmentArg(ARG_MWAY_KEY)
	@JvmField
	var motorwayKey: Int? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val motorway = Motorway.values()[motorwayKey ?: 0]
		TravelTimesSingleton.singleton.init(activity)

		travelTimeConfig = when (motorway) {
			Motorway.M1 -> TravelTimesSingleton.singleton.m1Config
			Motorway.M2 -> TravelTimesSingleton.singleton.m2Config
			Motorway.M4 -> TravelTimesSingleton.singleton.m4Config
			Motorway.M7 -> TravelTimesSingleton.singleton.m7Config
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		setHasOptionsMenu(true)
		activity.title = getString(R.string.tt_screen_title,
								   travelTimeConfig?.motorwayName ?: "")
		listView = ListViewWithEmptyMessage_.build(activity,
												   getString(R.string.tt_times_unavailable),
												   InactiveTravelTimeEmptyMessagePredicate())
		refreshAsync()
		return listView
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
		DownloadTravelTimesTask(activity, this, travelTimeConfig, listView).execute()
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		MLog.i(LOG_TAG, "Property ${event.propertyName} has changed")
		if (event.propertyName == MotorwayTravelTimesDatabase.PROPERTY_TOTAL_TRAVEL_TIME) {
			if (db != null) {
				listView!!.setAdapter(TravelTimesListAdapter(db))
			}
		}
	}

	companion object {
		private const val ARG_MWAY_KEY = "MWAY"
		private val LOG_TAG = TravelTimesFragment::class.java.simpleName

		fun create(mway: Motorway): TravelTimesFragment {
			return TravelTimesFragment_.builder().arg(ARG_MWAY_KEY, mway.ordinal).build()
		}
	}
}