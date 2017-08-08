package rod.bailey.trafficatnsw.traveltime.data

import com.google.gson.annotations.SerializedName
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

/**
 * Created by rodbailey on 7/8/17.
 */
data class XTravelTimeProperties(

	@SerializedName("direction")
	val direction: String?,

	@SerializedName("toDisplayName")
	val toDisplayName: String? = null,

	@SerializedName("isActive")
	val isActive: Boolean? = false,

	@SerializedName("fromDisplayName")
	val fromDisplayName: String? = null) {

	@SerializedName("travelTimeMinutes")
	var travelTimeMinutes: Int? = 0
		get() = field
		set(travelTimeMinutes) {
			field = travelTimeMinutes
			//firePropertyChangeEvent(PROPERTY_TRAVEL_TIME_IN_MINUTES, travelTimeMinutes)
		}

	companion object {
		/**
		 * Only changes in value of the following properties are broadcast to
		 * registered PropertyChangeListeners
		 */

		//val PROPERTY_TRAVEL_TIME_IN_MINUTES = "travelTimesInMinutes"
	}
}