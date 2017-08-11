package rod.bailey.trafficatnsw.cameras.ui

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.FragmentArg
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage_
import javax.inject.Inject

/**
 * Presents a list of all traffic cameras in a given region (mode). Always
 * create instances of this using static create() method.
 */
@EFragment
open class TrafficCameraListFragment : Fragment(), ITrafficCameraOverviewView {

	init {
		// Enable field injection
		TrafficAtNSWApplication.graph.inject(this)
	}

	/** Top level layout has list with DataLicenceView in footer  */
	private lateinit var cameraListView: ListViewWithEmptyMessage

	@FragmentArg(ARG_MODE_KEY)
	@JvmField
	var modeKey: Int? = null

	@Inject
	lateinit var presenter: TrafficCameraOverviewPresenter

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		cameraListView = ListViewWithEmptyMessage_.build(activity,
														 getString(R.string.camera_no_favourites),
														 EmptyListEmptyMessagePredicate())
		cameraListView.listViewAutoHideFooter.lv_list.divider =
			ContextCompat.getDrawable(activity, R.drawable.line_list_divider_partial)
		cameraListView.listViewAutoHideFooter.lv_list.dividerHeight = 2
		presenter.onIViewCreated(this, modeKey)
		return cameraListView
	}

	override fun setScreenTitle(title: String) {
		activity.title = title
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.onIViewDestroyed()
	}

	override fun setAdapter(adapter: TrafficCameraListAdapter) {
		cameraListView.setAdapter(adapter)
	}

	companion object {
		/** Key for the sole argument passed to this fragment.  */
		private const val ARG_MODE_KEY = "MODE"

		fun create(mode: TrafficCameraListMode): TrafficCameraListFragment {
			return TrafficCameraListFragment_.builder().arg(
				ARG_MODE_KEY, mode.ordinal).build()
		}
	}
}
