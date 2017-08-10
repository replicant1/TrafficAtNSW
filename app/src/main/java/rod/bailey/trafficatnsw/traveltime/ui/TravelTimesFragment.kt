package rod.bailey.trafficatnsw.traveltime.ui

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.androidannotations.annotations.*
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.predicate.InactiveTravelTimeEmptyMessagePredicate
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.traveltime.data.Motorway
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import javax.inject.Inject

@EFragment
@OptionsMenu(R.menu.menu_travel_times_options)
open class TravelTimesFragment : Fragment(), ITravelTimesOverviewView {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@FragmentArg(ARG_MWAY_KEY)
	@JvmField
	var motorwayKey: Int? = null

	private lateinit var listView: ListViewWithEmptyMessage

	@Inject
	lateinit var presenter: TravelTimesOverviewPresenter

	override fun onDestroyView() {
		super.onDestroyView()
		presenter.onIViewDestroyed()
	}

	override fun setScreenTitle(title: String) {
		Log.d(LOG_TAG, "Setting screen title to $title")
		activity.title = title
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		setHasOptionsMenu(true)
		listView = ListViewWithEmptyMessage_.build(activity,
												   getString(R.string.tt_times_unavailable),
												   InactiveTravelTimeEmptyMessagePredicate())
		presenter.onIViewCreated(this, motorwayKey)
		return listView
	}

	@AfterViews
	@OptionsItem(R.id.refresh_travel_times)
	fun refreshTravelTimesAsync() {
		presenter.onFreshMotorwayDataRequested(activity)
	}

	override fun setMotorwayData(db: MotorwayTravelTimesStore?) {
		Log.d(LOG_TAG, "Into setMotorwayData with db=$db")
		if (db != null)
			listView.setAdapter(TravelTimesListAdapter(db))
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