package rod.bailey.trafficatnsw.traveltime.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.FragmentArg
import org.androidannotations.annotations.OptionsItem
import org.androidannotations.annotations.OptionsMenu
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.predicate.InactiveTravelTimeEmptyMessagePredicate
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.traveltime.data.*
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.inject.Inject

@EFragment
@OptionsMenu(R.menu.menu_travel_times_options)
open class TravelTimesFragment : Fragment(), PropertyChangeListener {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var motorwayRegistry: MotorwayConfigRegistry

	private lateinit var listView: ListViewWithEmptyMessage

	/** Travel times for the motorway currently being displayed  */
	var db: MotorwayTravelTimesStore? = null

	/** Config for the motorway currently being display  */
	private var travelTimeConfig: MotorwayConfig? = null

	@FragmentArg(ARG_MWAY_KEY)
	@JvmField
	var motorwayKey: Int? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val motorway = Motorway.values()[motorwayKey ?: 0]

		travelTimeConfig = when (motorway) {
			Motorway.M1 -> motorwayRegistry.m1Config
			Motorway.M2 -> motorwayRegistry.m2Config
			Motorway.M4 -> motorwayRegistry.m4Config
			Motorway.M7 -> motorwayRegistry.m7Config
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		setHasOptionsMenu(true)
		activity.title = getString(R.string.tt_screen_title, travelTimeConfig?.motorwayName ?: "")
		listView = ListViewWithEmptyMessage_.build(activity,
												   getString(R.string.tt_times_unavailable),
												   InactiveTravelTimeEmptyMessagePredicate())
		refreshAsync()
		return listView
	}

	@OptionsItem(R.id.refresh_travel_times)
	fun refreshAsync() {
		DownloadTravelTimesTask(activity, this, travelTimeConfig, listView).execute()
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		MLog.i(LOG_TAG, "Property ${event.propertyName} has changed")
		if (event.propertyName == MotorwayTravelTimesStore.PROPERTY_TOTAL_TRAVEL_TIME) {
			if (db != null) {
				listView.setAdapter(TravelTimesListAdapter(db))
			}
		}
	}

	companion object {
		private const val ARG_MWAY_KEY = "rod.bailey.trafficatnsw.motorway"
		private val LOG_TAG = TravelTimesFragment::class.java.simpleName

		fun create(mway: Motorway): TravelTimesFragment {
			return TravelTimesFragment_.builder().arg(
				ARG_MWAY_KEY, mway.ordinal).build()
		}
	}
}