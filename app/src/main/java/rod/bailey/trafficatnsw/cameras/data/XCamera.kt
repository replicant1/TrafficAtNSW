package rod.bailey.trafficatnsw.cameras.data

import android.util.Log
import com.google.gson.annotations.SerializedName
import rod.bailey.trafficatnsw.hazard.data.XGeometry
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.io.Serializable

/**
 * A traffic camera
 */
data class XCamera(

	@SerializedName("id")
	val id: String?,

	@SerializedName("geometry")
	val geometry: XGeometry?,

	@SerializedName("properties")
	val properties: XCameraProperties?) : Comparable<XCamera>, Serializable {

	/** True if this camera is one of the users favourites */
	private var favourite: Boolean = false

	private var support: PropertyChangeSupport? = null

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		if (support == null) {
			support = PropertyChangeSupport(this)
		}
		support?.addPropertyChangeListener(listener)
	}

	private fun fireFavouritePropertyChangeEvent() {
		if (support == null) {
			support = PropertyChangeSupport(this)
		}
		support?.firePropertyChange(PROPERTY_FAVOURITE, null, favourite)
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
	override fun compareTo(other: XCamera): Int {
		if ((properties != null) && (other.properties != null)) {
			if ((properties.title != null) && (other.properties.title != null)) {
				return properties.title.compareTo(other.properties.title)
			}
		}
		return 0
	}

	companion object {
		private val LOG_TAG = XCamera::class.java.simpleName
		const val PROPERTY_FAVOURITE = "rod.bailey.trafficatnsw.extraFavourite"
	}
}