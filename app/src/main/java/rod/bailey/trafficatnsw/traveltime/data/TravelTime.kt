package rod.bailey.trafficatnsw.traveltime.data

import org.json.JSONException
import org.json.JSONObject
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*

class TravelTime @Throws(JSONException::class)
constructor(featureJSONObject: JSONObject?) : Comparable<TravelTime> {

	private val support = PropertyChangeSupport(this)

	var isActive: Boolean = false
	var fromDisplayName: String? = null
	var segmentId: String? = null
	var toDisplayName: String? = null

	private var includedInTotal: Boolean = false
	private var travelTimeMinutes: Int = 0

	init {
		val properties = featureJSONObject!!.getJSONObject("properties")
		// 'id' is mandatory
		try {
			segmentId = featureJSONObject.getString("id")
		}
		catch (e: JSONException) {
			MLog.e(LOG_TAG, "Failed to parse 'id' property of travel time segment",
				   e)
		}
		// 'fromDisplayName' is mandatory
		try {
			fromDisplayName = properties.getString("fromDisplayName")
		}
		catch (e: JSONException) {
			MLog.d(LOG_TAG,
				   "Failed to parse 'fromDisplayName' property of travel time segment")
		}
		// 'toDisplayName' is mandatory
		try {
			toDisplayName = properties.getString("toDisplayName")
		}
		catch (e: JSONException) {
			MLog.d(LOG_TAG,
				   "Failed to parse 'toDisplayName' property of travel time segment")
		}
		// 'isActive' is optional
		try {
			isActive = properties.getBoolean("isActive")
		}
		catch (e: JSONException) {
			MLog.d(LOG_TAG,
				   "Failed to parse 'isActive' property of travel time segment")
		}

		try {
			travelTimeMinutes = properties.getInt("travelTimeMinutes")
		}
		catch (e: JSONException) {
			MLog.d(LOG_TAG,
				   "Failed to parse 'travelTimeMinutes' property of travel time segment")
		}

		includedInTotal = true
	}

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	fun removePropertyChangeListener(listener: PropertyChangeListener) {
		support.removePropertyChangeListener(listener)
	}

	private fun firePropertyChangeEvent(propertyName: String, newValue: Any) {
		support.firePropertyChange(propertyName, null, newValue)
	}

	/**
	 * @return negative if 'this' is < 'other', positive if 'this' is > 'other', zero if 'this' is equal to 'other'
	 */
	override fun compareTo(other: TravelTime): Int {
		// Primary sort order is by first character in id
		// Secondary sort order is by remaining characters, with 'TOTAL'
		// appearing at ended
		// eg. E1, E2, E3, ETOTAL, W1, W2, W3, WTOTAL
		var result = 0
		val otherSegmentId = other.segmentId
		val firstCharCompare = segmentId!!.substring(0, 1).compareTo(
			otherSegmentId!!.substring(0, 1))

		if (firstCharCompare == 0) {
			val thisEndChars: String = segmentId!!.substring(1)
			val otherEndChars: String = otherSegmentId.substring(1)
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
			result = firstCharCompare
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

	fun getTravelTimeMinutes(): Int {
		return travelTimeMinutes
	}

	var isIncludedInTotal: Boolean
		get() = includedInTotal
		set(includedInTotal) {
			this.includedInTotal = includedInTotal
			firePropertyChangeEvent(
				PROPERTY_INCLUDED_IN_TOTAL, includedInTotal)
		}
	val isTotal: Boolean
		get() = if (segmentId == null) false else segmentId!!.endsWith("TOTAL")

	fun setIncludedInTotalSilently(includedInTotal: Boolean) {
		this.includedInTotal = includedInTotal
	}

	fun setTravelTimeMinutes(travelTimeMinutes: Int) {
		this.travelTimeMinutes = travelTimeMinutes
		firePropertyChangeEvent(PROPERTY_TRAVEL_TIME_IN_MINUTES, travelTimeMinutes)
	}

	companion object {
		/**
		 * Only changes in value of the following properties are broadcast to
		 * registered PropertyChangeListeners
		 */
		val PROPERTY_INCLUDED_IN_TOTAL = "includedInTotal"
		val PROPERTY_TRAVEL_TIME_IN_MINUTES = "travelTimesInMinutes"
		private val LOG_TAG = TravelTime::class.java.simpleName

		/**
		 * Takes the contents of the remote JSON file as a single string and breaks
		 * it down into an array of TravelTime instances.
		 * jsonFileContents - textual contents of JSON file return - array of
		 * TravelTime instances. Includes TOTAL features.
		 *
		 * @param jsonFileContents
		 * @return List of TravelTIme instances filled out with data from the JSON
		 *          file. Order of list is same as order of JSON objects appearance
		 *          in the given jsonFileContents.
		 */
		fun parseTravelTimesJson(
			jsonFileContents: String?): List<TravelTime> {
			val result = LinkedList<TravelTime>()

			try {
				MLog.d(LOG_TAG, "Parsing top level object")
				val topLevelObject = JSONObject(jsonFileContents)

				MLog.d(LOG_TAG, "Parsed top level object OK")
				val features = topLevelObject.getJSONArray("features")

				MLog.d(LOG_TAG, "Number of features in JSON file: " + features.length())
				for (i in 0..features.length() - 1) {
					val featureJSONObject = features.getJSONObject(i)
					val featureId = featureJSONObject.getString("id")

					MLog.d(LOG_TAG,
						   String.format("Hazard[%d] has id %s", i, featureId))
					MLog.d(LOG_TAG, String.format(
						"Creating a TravelTime object with id %s", featureId))
					val travelTime = TravelTime(featureJSONObject)
					result.add(travelTime)
				}
			}
			catch (e: JSONException) {
				MLog.e(LOG_TAG, "Failed to parse JSON", e)
			}

			return result
		}
	}
}
