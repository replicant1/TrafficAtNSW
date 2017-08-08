package rod.bailey.trafficatnsw.traveltime.data

import com.google.gson.annotations.SerializedName
import rod.bailey.trafficatnsw.hazard.data.XGeometry
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

/**
 * A segment of motorway for which we have travel time
 */
data class XTravelTimeSegment(
	@SerializedName("id")
	val segmentId: String? = null,

	@SerializedName("geometry")
	val geometry: XGeometry?,

	@SerializedName("properties")
	val properties: XTravelTimeProperties?) : Comparable<XTravelTimeSegment> {

	private var support: PropertyChangeSupport? = null

	/**
	 * @return negative if 'this' is < 'other', positive if 'this' is > 'other', zero if 'this' is equal to 'other'
	 */
	override fun compareTo(other: XTravelTimeSegment): Int {
		var result: Int = 0

		if ((segmentId != null) && (other.segmentId != null)) {
			result = segmentId.compareTo(other.segmentId)
		}

		return result
	}

	var includedInTotal: Boolean? = true
		get() = field
		set(includedInTotal) {
			field = includedInTotal
			firePropertyChangeEvent(PROPERTY_INCLUDED_IN_TOTAL, includedInTotal)
		}

	fun setIncludedInTotalSilently(includedInTotal: Boolean) {
		val listeners = support?.getPropertyChangeListeners()

		// By detaching any existing listeners, we ensure that the
		// change occurs silently
		if (listeners != null) {
			for (listener in listeners) {
				support?.removePropertyChangeListener(listener)
			}
		}

		this.includedInTotal = includedInTotal

		// Re-instate any previously detached listeners
		if (listeners != null) {
			for (listener in listeners) {
				support?.addPropertyChangeListener(listener)
			}
		}
	}

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		if (support == null) {
			support = PropertyChangeSupport(this)
		}
		support?.addPropertyChangeListener(listener)
	}

	fun removePropertyChangeListener(listener: PropertyChangeListener) {
		support?.removePropertyChangeListener(listener)
	}

	private fun firePropertyChangeEvent(propertyName: String, newValue: Any?) {
		support?.firePropertyChange(propertyName, null, newValue)
	}

	val isTotal: Boolean
		get() = if (segmentId == null) false else segmentId.endsWith("TOTAL")

	companion object {
		private val LOG_TAG = XTravelTimeSegment::class.java.simpleName
		val PROPERTY_INCLUDED_IN_TOTAL = "includedInTotal"
	}
}
