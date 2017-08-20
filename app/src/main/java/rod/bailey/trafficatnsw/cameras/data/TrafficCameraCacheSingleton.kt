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
import javax.inject.Inject

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
	private val filteredCamerasPerRegion = HashMap<XRegion, MutableList<XCamera>>()
	private val unfilteredCamerasPerRegion = HashMap<XRegion, MutableList<XCamera>>()

	@Inject
	@JvmField
	var context: Context? = null

	/**
	 * Set the filter that all subsequent "get camera" operations will be filtered through
	 */
	var filter: ITrafficCameraFilter = AdmitAnyTrafficCameraFilter()
		get() = field
		set(value) {
			field = value
			applyFilter()
		}

	fun init(camerasJSON: String) {
		val allCameras: XCameraCollection = XCameraCollection.parseCameraJson(camerasJSON)
		if (allCameras.cameras != null) {
			init(allCameras.cameras)
		}
	}

	fun init(cameras: List<XCamera>) {
		prime(cameras)
		applyFilter()
		loadFavourites()
	}

	private fun prime(allCameras: List<XCamera>) {
		unfilteredCamerasPerRegion.clear()
		for (camera in allCameras) {
			val props: XCameraProperties? = camera.properties
			if (props != null) {
				val regionStr: String? = props.region
				if (regionStr != null) {
					val region = XRegion.valueOf(regionStr)
					if (!unfilteredCamerasPerRegion.containsKey(region)) {
						unfilteredCamerasPerRegion.put(region, LinkedList<XCamera>())
					}

					unfilteredCamerasPerRegion[region]?.add(camera)
				}
			}
		}

		addSelfAsListenerToAllCameras()
	}

	fun getUnfilteredCamera(id: String): XCamera? {
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
		val prefs: SharedPreferences? = context?.getSharedPreferences(FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE)
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
		val prefs = context?.getSharedPreferences(FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE)
		prefs?.edit()?.putStringSet(FAVOURITE_STATE_PREF_KEY, favouriteCameraIds)?.commit()
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

	private fun applyFilter() {
		filteredCamerasPerRegion.clear()
		for (region in unfilteredCamerasPerRegion.keys) {
			val unfilteredCameras: MutableList<XCamera>? = unfilteredCamerasPerRegion[region]
			unfilteredCameras?.let {
				val filteredCameras: List<XCamera> = unfilteredCameras.filter { filter.admit(it) }
				if (filteredCameras.isNotEmpty()) {
					val mutableFilteredCameras = LinkedList<XCamera>()
					mutableFilteredCameras.addAll(filteredCameras)
					filteredCamerasPerRegion.put(region, mutableFilteredCameras)
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
