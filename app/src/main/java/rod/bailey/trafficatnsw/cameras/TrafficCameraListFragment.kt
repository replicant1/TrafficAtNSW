package rod.bailey.trafficatnsw.cameras

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import rod.bailey.trafficatnsw.cameras.filter.AdmitFavouritesTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.AdmitRegionalTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.AdmitSydneyTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.ui.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.ui.view.ListWithEmptyMessage
import rod.bailey.trafficatnsw.util.MLog

class TrafficCameraListFragment : Fragment(), PropertyChangeListener {
	enum class TrafficCameraListMode private constructor(val displayName: String,
														 val actionBarTitle: String,
														 val filter: ITrafficCameraFilter) {
		FAVOURITES("Favourites", "Favourite Cameras", AdmitFavouritesTrafficCameraFilter()), //
		REGIONAL("Regional", "Cameras in Regional NSW", AdmitRegionalTrafficCameraFilter()), //
		SYDNEY("Sydney", "Cameras in Sydney", AdmitSydneyTrafficCameraFilter())
	}
	/** List of traffic cameras, divided into sections by XRegion  */
	// private ListView listView;
	/** Top level layout has list on top, DataLicenceView at bottom  */
	private var mainLayout: ListWithEmptyMessage? = null
	/**
	 * Mode of display = what cameras appear in list. Derived from the value
	 * passed into this fragment for the ARG_HAZARDS_FRAGMENT_MODE argument.
	 */
	private var mode: TrafficCameraListMode? = TrafficCameraListMode.SYDNEY

	private fun createUI() {
		Log.i(TAG, "Into TrafficCameraListFragment.createUI")
		val ctx = activity

		mainLayout = ListWithEmptyMessage(ctx, EMPTY_MESSAGE,
										  EmptyListEmptyMessagePredicate())
		val adapter = TrafficCameraListAdapter(
			mode!!.filter)
		mainLayout!!.setAdapter(adapter)

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
			MLog.i(TAG, "args was null")
		}

		MLog.i(TAG, "Setting camera mode to " + newmode.name)
		mode = newmode
		// Initialize TrafficCameraCacheSingleton
		val db = TrafficCameraCacheSingleton.instance
		db.init(activity)

		MLog.i(TAG, "TCLFragment add self as listener to TCDb " + db.hashCode())
		db.addPropertyChangeListener(this)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle): View? {
		createUI()
		return mainLayout
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		if (TrafficCameraCacheSingleton.PROPERTY_FAVOURITE_SET == event
			.propertyName) {
			if (mainLayout != null && mode != null) {
				// TODO: WOuld be nice to save list's scroll pos here and them
				// restore after setting the new adapter.
				val adapter = TrafficCameraListAdapter(
					mode!!.filter)
				mainLayout!!.setAdapter(adapter)
			}
		}
	}

	companion object {
		private val EMPTY_MESSAGE:String = """You have no favourite cameras.\n\n
		To make a camera one of your favourites, view the camera image and
		tap the star at the top right of the screen."""
		/** Key for the sole argument passed to this fragment.  */
		val ARG_MODE_KEY = "MODE"
		/**
		 * Value for ARG_HAZARDS_FRAGMENT_MODE that indicates the list should contain ALL traffic
		 * cameras
		 */
		val ARG_MODE_VALUE_ALL = 0
		val ARG_MODE_VALUE_SYDNEY = 1
		val ARG_MODE_VALUE_REGIONAL = 2
		/**
		 * Value for ARG_HAZARDS_FRAGMENT_MODE that indicates the list should contain only those
		 * traffic cameras marked as favourites.
		 */
		val ARG_MODE_VALUE_FAVOURITES = 3
		/** Tag for logging  */
		private val TAG = TrafficCameraListFragment::class.java
			.simpleName
	}
}
