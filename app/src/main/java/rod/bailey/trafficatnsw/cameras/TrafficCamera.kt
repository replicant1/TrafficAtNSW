package rod.bailey.trafficatnsw.cameras

import rod.bailey.trafficatnsw.json.hazard.XRegion
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

class TrafficCamera(val street: String?, val suburb: String?, val description: String?,
					val url: String?, val region: XRegion?,
					val index: Int) : Comparable<TrafficCamera> {
	private var favourite: Boolean = false
	private val support = PropertyChangeSupport(this)

	init {
		assert(street != null)
		assert(suburb != null)
		assert(description != null)
		assert(url != null)
		assert(region != null)
		assert(index >= 0)
	}

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	fun removePropertyChangeListener(listener: PropertyChangeListener) {
		support.removePropertyChangeListener(listener)
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
		val PROPERTY_FAVOURITE = "favourite"
	}
}
