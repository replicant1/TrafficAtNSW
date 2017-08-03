package rod.bailey.trafficatnsw.cameras.data

import rod.bailey.trafficatnsw.hazard.data.XRegion
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

/**
 * Domain object that captures all we know about a given Traffic Camera
 *
 * @param street Street name on which camera resides
 * @param suburb Geographic suburb where the camera is located
 * @param description Displayable description of the camera angle eg. "Looking west on Anzaz Drive"
 * @param url URL to the camera image on the Live Traffic web site
 * @param region RMS region in which the camera resides
 * @param index Ordinal of this camera - serves as an id
 */
class TrafficCamera(val street: String?, val suburb: String?, val description: String?,
					val url: String?, val region: XRegion?,
					val index: Int) : Comparable<TrafficCamera> {

	/** True if this camera is one of the users favourites */
	private var favourite: Boolean = false

	private val support = PropertyChangeSupport(this)

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	private fun fireFavouritePropertyChangeEvent() {
		support.firePropertyChange(PROPERTY_FAVOURITE, null, favourite)
	}

	fun setFavouriteSilently(value: Boolean) {
		favourite = value
	}

	var isFavourite: Boolean
		get() = favourite
		set(value) {
			favourite = value
			fireFavouritePropertyChangeEvent()
		}

	/**
	 * @return TrafficCameras are sorted in increasing order by street name
	 * and then suburb name.
	 */
	override fun compareTo(other: TrafficCamera): Int {
		var result = 0

		if ((street != null) && (other.street != null)) {
			result = street.compareTo(other.street)
		}

		if ((result == 0) && (suburb  != null) && (other.suburb != null)) {
			result = suburb.compareTo(other.suburb)
		}

		return result
	}

	companion object {
		const val PROPERTY_FAVOURITE = "rod.bailey.trafficatnsw.extraFavourite"
	}
}
