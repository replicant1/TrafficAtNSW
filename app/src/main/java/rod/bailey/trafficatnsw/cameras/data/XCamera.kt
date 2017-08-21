package rod.bailey.trafficatnsw.cameras.data

import android.util.Log
import android.util.Property
import com.google.gson.annotations.SerializedName
import rod.bailey.trafficatnsw.hazard.data.XGeometry
import java.beans.PropertyChangeEvent
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

	private var propertyChangeListener: PropertyChangeListener? = null

	fun setPropertyChangeListener(listener: PropertyChangeListener) {
		propertyChangeListener = listener
	}

	private fun fireFavouritePropertyChangeEvent() {
		propertyChangeListener?.propertyChange(
			PropertyChangeEvent(this, PROPERTY_FAVOURITE, null,  favourite))
	}

	fun setFavouriteSilently(value: Boolean) {
		val listener = propertyChangeListener
		propertyChangeListener = null

		favourite = value

		if (listener != null) {
			propertyChangeListener = listener
		}
	}

	var favourite: Boolean = false
		get() = field
		set(value) {
			field = value
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