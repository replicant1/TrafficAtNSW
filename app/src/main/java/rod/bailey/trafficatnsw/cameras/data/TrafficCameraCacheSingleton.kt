package rod.bailey.trafficatnsw.cameras.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import rod.bailey.trafficatnsw.cameras.filter.AdmitAnyTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.hazard.data.XRegion
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*

/**
 * Normally we would read this in from a JSON file, but the contents are hardcoded
 * here for expediency. Always inject this so it remains a singleton.
 */
class TrafficCameraCacheSingleton : PropertyChangeListener {

	companion object {
		const val PROPERTY_FAVOURITE_SET = "rod.bailey.trafficatnsw.favouriteSet"
		private const val FAVOURITE_CAMERAS_FILE_NAME = "favourite_cameras"
		private const val FAVOURITE_STATE_PREF_KEY = "FAVOURITE"
		private val LOG_TAG = TrafficCameraCacheSingleton::class.java.simpleName
	}

	private val support = PropertyChangeSupport(this)
	private val filteredCamerasPerRegion = HashMap<XRegion, List<XCamera>>()
	private val unfilteredCamerasPerRegion = HashMap<XRegion, List<XCamera>>()
	private lateinit var ctx: Context

	/**
	 * Set the filter that all subsequent "get camera" operations will be filtered through
	 */
	var filter: ITrafficCameraFilter = AdmitAnyTrafficCameraFilter()
		get() = field
		set(value) {
			field = value
			applyFilter(field)
		}

	init {
		initUnfiltered()
		applyFilter(AdmitAnyTrafficCameraFilter())
	}

	fun init(ctx: Context, cameras: XCameraCollection) {
		this.ctx = ctx
		loadFavourites()
	}

	fun getCamera(id: String): XCamera? {
		var result: XCamera? = null
		val cameraCollections = unfilteredCamerasPerRegion.values
		for (cameraList in cameraCollections) {
			for (camera in cameraList) {
				if (id == camera.id) {
					result = camera
					break
				}
			}
		}

		return result
	}

	fun getCamerasForRegion(region: XRegion): List<XCamera> {
		return filteredCamerasPerRegion.get(region) ?: LinkedList()
	}

	private fun initUnfiltered() {
//		unfilteredCamerasPerRegion.put(XRegion.SYD_MET, createSydMetData())
//		unfilteredCamerasPerRegion.put(XRegion.SYD_NORTH, createSydNorthData())
//		unfilteredCamerasPerRegion.put(XRegion.SYD_SOUTH, createSydSouthData())
//		unfilteredCamerasPerRegion.put(XRegion.SYD_WEST, createSydWestData())
//		unfilteredCamerasPerRegion.put(XRegion.REG_NORTH, createRegNorthData())
//		unfilteredCamerasPerRegion.put(XRegion.REG_SOUTH, createRegSouthData())
		// Note: There are no cameras in REG_WEST at this time

		addSelfAsListenerToAllCameras()
	}

	private fun addSelfAsListenerToAllCameras() {
		for (camerasInRegion in unfilteredCamerasPerRegion.values) {
			for (camera in camerasInRegion) {
				camera.addPropertyChangeListener(this)
			}
		}
	}

	private fun loadFavourites() {
		// To begin with, mark all as NOT favourites
		unfilteredCamerasPerRegion.values.forEach {
			for (camera in it) {
				camera.setFavouriteSilently(false)
			}
		}

		// Load ids of favourite cameras from prefs
		val prefs: SharedPreferences? = ctx.getSharedPreferences(FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE)
		val favouriteCameraIds = prefs?.getStringSet(FAVOURITE_STATE_PREF_KEY, null)

		Log.i(LOG_TAG, "Favourite cameras as loaded from prefs is: ${favouriteCameraIds}")

		// Set in-memory cameras' "isFavourite" to true if camera id is in prefs
		favouriteCameraIds?.let {
			unfilteredCamerasPerRegion.values.forEach {
				for (camera in it) {
					if (favouriteCameraIds.contains(camera.id)) {
						camera.isFavourite = true
					}
				}
			}
		}
	}

	/**
	 * We save those cameras that ARE favourites. Applies to all known cameras -
	 * even those not admitted by the current filter.
	 */
	fun saveFavourites() {
		val favouriteCameraIds = HashSet<String>()

		// Accumulate a single list of all cameras marked as favourites
		unfilteredCamerasPerRegion.values.forEach {
			for (camera in it) {
				if ((camera.isFavourite) && (camera.id != null)) {
					favouriteCameraIds.add(camera.id)
				}
			}
		}

		// Persist ids of favourite cameras to prefs
		val prefs = ctx.getSharedPreferences(FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE)
		prefs.edit().putStringSet(FAVOURITE_STATE_PREF_KEY, favouriteCameraIds).commit()
	}

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	fun removePropertyChangeListener(listener: PropertyChangeListener) {
		support.removePropertyChangeListener(listener)
	}

	private fun fireFavouritePropertyChangeEvent() {
		support.firePropertyChange(PROPERTY_FAVOURITE_SET, null, this)
	}

	private fun applyFilter(f: ITrafficCameraFilter) {
		filteredCamerasPerRegion.clear()
		for (region in unfilteredCamerasPerRegion.keys) {
			val unfilteredCameras: List<XCamera>? = unfilteredCamerasPerRegion[region]
			unfilteredCameras?.let {
				val filteredCameras = unfilteredCameras.filter { f.admit(it) }
				if (filteredCameras.isNotEmpty()) {
					filteredCamerasPerRegion.put(region, filteredCameras)
				}
			}
		}
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		if (XCamera.PROPERTY_FAVOURITE == event.propertyName) {
			saveFavourites()
			fireFavouritePropertyChangeEvent()
		}
	}

}
