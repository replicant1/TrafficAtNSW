package rod.bailey.trafficatnsw.cameras

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.Trace
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.filter.AdmitFavouritesTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.AdmitRegionalTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.AdmitSydneyTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.ui.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class TrafficCameraListFragment : Fragment(), PropertyChangeListener {
	enum class TrafficCameraListMode private constructor(val displayName: String,
														 val actionBarTitle: String,
														 val filter: ITrafficCameraFilter) {
		FAVOURITES("Favourites", "Favourite Cameras", AdmitFavouritesTrafficCameraFilter()), //
		REGIONAL("Regional", "Cameras in Regional NSW", AdmitRegionalTrafficCameraFilter()), //
		SYDNEY("Sydney", "Cameras in Sydney", AdmitSydneyTrafficCameraFilter())
	}

	/** Top level layout has list with DataLicenceView in footer  */
	private lateinit var cameraListView: ListViewWithEmptyMessage

	/**
	 * Mode of display = what cameras appear in list. Derived from the value
	 * passed into this fragment for the ARG_HAZARDS_FRAGMENT_MODE argument.
	 */
	private var mode: TrafficCameraListMode? = TrafficCameraListMode.SYDNEY

	private fun createUI() {
		cameraListView = ListViewWithEmptyMessage_.build(activity,
														 EMPTY_MESSAGE,
														 EmptyListEmptyMessagePredicate())
		cameraListView.listViewAutoHideFooter.lv_list.divider =
			ContextCompat.getDrawable(activity, R.drawable.line_list_divider_partial)
		cameraListView.listViewAutoHideFooter.lv_list.dividerHeight = 2
		val adapter = TrafficCameraListAdapter(mode!!.filter)
		cameraListView.setAdapter(adapter)
		activity.title = mode!!.actionBarTitle
	}

	override fun onCreate(bundle: Bundle?) {
		super.onCreate(bundle)
		val args = arguments
		var newmode = TrafficCameraListMode.SYDNEY
		if (args != null) {
			if (args.containsKey(ARG_MODE_KEY)) {
				val modeKey = args.getInt(ARG_MODE_KEY)

				when (modeKey) {
					ARG_MODE_VALUE_FAVOURITES -> newmode = TrafficCameraListMode.FAVOURITES
					ARG_MODE_VALUE_REGIONAL -> newmode = TrafficCameraListMode.REGIONAL
					ARG_MODE_VALUE_SYDNEY -> newmode = TrafficCameraListMode.SYDNEY
				}
			}
		} else {
			MLog.i(LOG_TAG, "args was null")
		}

		MLog.i(LOG_TAG, "Setting camera mode to " + newmode.name)
		mode = newmode

		// Initialize TrafficCameraCacheSingleton
		val db = TrafficCameraCacheSingleton.instance
		db.init(activity)

		MLog.i(LOG_TAG, "TCLFragment add self as listener to TCDb " + db.hashCode())
		db.addPropertyChangeListener(this)
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		createUI()
		return cameraListView
	}

	override fun propertyChange(event: PropertyChangeEvent) {
//		if (TrafficCameraCacheSingleton.PROPERTY_FAVOURITE_SET == event.gpropertyName) {
			if (mode != null) {
				// TODO: WOuld be nice to save list's scroll pos here and them
				// restore after setting the new adapter.
				val adapter = TrafficCameraListAdapter(
					mode!!.filter)
				cameraListView.setAdapter(adapter)
			}
//		}
	}

	companion object {
		private val EMPTY_MESSAGE: String = """You have no favourite cameras.\n\n
		To make a camera one of your favourites, view the camera imageView and
		tap the star at the top right of the screen."""

		/** Key for the sole argument passed to this fragment.  */
		val ARG_MODE_KEY = "MODE"
		val ARG_MODE_VALUE_SYDNEY = 1
		val ARG_MODE_VALUE_REGIONAL = 2
		val ARG_MODE_VALUE_FAVOURITES = 3

		private val LOG_TAG = TrafficCameraListFragment::class.java.simpleName

		fun create(mode: Int): TrafficCameraListFragment {
			val result = TrafficCameraListFragment()
			val bundle = Bundle()
			bundle.putInt(ARG_MODE_KEY, mode)
			result.arguments = bundle
			return result
		}
	}
}
