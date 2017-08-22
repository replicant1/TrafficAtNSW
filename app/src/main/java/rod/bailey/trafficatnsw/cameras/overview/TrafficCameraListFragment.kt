package rod.bailey.trafficatnsw.cameras.overview

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.FragmentArg
import org.androidannotations.annotations.OptionsItem
import org.androidannotations.annotations.OptionsMenu
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.common.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage_
import javax.inject.Inject

/**
 * Presents a list of all traffic cameras in a given region (mode). Always
 * create instances of this using static create() method.
 */
@EFragment
@OptionsMenu(R.menu.menu_travel_times_options)
open class TrafficCameraListFragment : Fragment(), ITrafficCameraOverviewView {

	init {
		// Enable field injection
		TrafficAtNSWApplication.graph.inject(this)
	}

	/** Top level layout has list with DataLicenceView in footer  */
	private lateinit var cameraListView: ListViewWithEmptyMessage

	private lateinit var mode: TrafficCameraListMode

	@FragmentArg(ARG_MODE_KEY)
	@JvmField
	var modeKey: Int? = null

	@Inject
	lateinit var cameraCacheSingleton: TrafficCameraCacheSingleton

	@Inject
	lateinit var presenter: TrafficCameraOverviewPresenter

	@OptionsItem(R.id.menu_item_refresh_travel_time_list)
	fun loadCamerasAsync() {
		presenter.loadCamerasAsync(activity, cameraListView)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mode = TrafficCameraListMode.values()[modeKey ?: 0]
		cameraCacheSingleton.filter = mode.filter
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		cameraListView = ListViewWithEmptyMessage_.build(activity,
				getString(R.string.camera_no_favourites),
				EmptyListEmptyMessagePredicate())
		cameraListView.listViewAutoHideFooter.lv_list.divider =
				ContextCompat.getDrawable(activity, R.drawable.line_list_divider_partial)
		cameraListView.listViewAutoHideFooter.lv_list.dividerHeight = 2
		activity.title = getString(presenter.getScreenTitleForMode(mode))
		return cameraListView
	}

	override fun onPause() {
		super.onPause()
		presenter.onDetachView()
	}

	override fun onResume() {
		super.onResume()
		presenter.onAttachView(this, modeKey)
		loadCamerasAsync()
	}

	override fun refreshCameraList() {
		cameraListView.setAdapter(TrafficCameraListAdapter(mode.filter))
	}

	companion object {
		private val LOG_TAG = TrafficCameraListFragment::class.java.simpleName

		/** Key for the sole argument passed to this fragment.  */
		private const val ARG_MODE_KEY = "MODE"

		fun create(mode: TrafficCameraListMode): TrafficCameraListFragment {
			return TrafficCameraListFragment_.builder().arg(
					ARG_MODE_KEY, mode.ordinal).build()
		}
	}
}
