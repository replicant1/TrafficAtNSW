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

	/**
	 * @return negative if 'this' is < 'other', positive if 'this' is > 'other', zero if 'this' is equal to 'other'
	 */
	override fun compareTo(other: XTravelTimeSegment): Int {
		// Primary sort order is by first character in id
		// Secondary sort order is by remaining characters, with 'TOTAL'
		// appearing at ended
		// eg. E1, E2, E3, ETOTAL, W1, W2, W3, WTOTAL
		var result = 0
		val firstCharCompare = segmentId?.substring(0, 1)?.compareTo(other.segmentId?.substring(0, 1) ?: "")

		if (firstCharCompare == 0) {
			val thisEndChars: String = segmentId!!.substring(1)
			val otherEndChars: String = other.segmentId?.substring(1) ?: ""
			val thisSecondCharIsNumber: Boolean = isNumber(thisEndChars)
			val otherSecondCharIsNumber: Boolean = isNumber(otherEndChars)

			if (thisSecondCharIsNumber && otherSecondCharIsNumber) {
				result = thisEndChars.toInt().compareTo(otherEndChars.toInt())
			} else if (thisSecondCharIsNumber && !otherSecondCharIsNumber) {
				result = -1
			} else if (!thisSecondCharIsNumber && otherSecondCharIsNumber) {
				result = 1
			}
		} else {
			result = firstCharCompare ?: 0
		}

		return result
	}

	private fun isNumber(str: String): Boolean {
		var result = true

		try {
			Integer.parseInt(str)
		}
		catch (nfe: NumberFormatException) {
			result = false
		}

		return result
	}

	var includedInTotal: Boolean? = true
		get() = field
		set(includedInTotal) {
			field = includedInTotal
			firePropertyChangeEvent(PROPERTY_INCLUDED_IN_TOTAL, includedInTotal)
		}

	// TODO: This won't do anything silently
	fun setIncludedInTotalSilently(includedInTotal: Boolean) {
		this.includedInTotal = includedInTotal
	}

	private var support: PropertyChangeSupport? = null

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
