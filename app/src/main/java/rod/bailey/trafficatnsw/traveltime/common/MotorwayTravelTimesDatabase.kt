package rod.bailey.trafficatnsw.traveltime.common

import android.content.Context
import android.content.SharedPreferences
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*

/**
 * Knows the 'isIncludedInTotal' state of each F3TravelTime that the user has
 * specified. Also knows how to store those inclusion states to disk and restore
 * them from disk. Note:contains data for a single motorway only.
 */
class MotorwayTravelTimesDatabase(ctx: Context, val config: TravelTimeConfig?) : PropertyChangeListener {
	private val prefs: SharedPreferences
	var isPrimed: Boolean = false
		private set
	private val support = PropertyChangeSupport(this)
	private val travelTimes: LinkedList<TravelTime> = LinkedList<TravelTime>()

	init {
		prefs = ctx.getSharedPreferences(config!!.preferencesFileName, Context.MODE_PRIVATE)
	}

	/**
	 * @param listener
	 * *            Listener will be notified whenever PROPERTY_TOTAL_TRAVEL_TIME
	 * *            changes.
	 */
	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	/**
	 * Add this database as a listener for changes in any property of any of the
	 * TravelTImes in the database.
	 */
	private fun addSelfAsPropertyChangeListener() {
		for (seg in travelTimes) {
			seg.addPropertyChangeListener(this)
		}
	}

	/**
	 * Notifies all registered listeners that the value of the
	 * 'isIncludedInTotal' property of the given TravelTime instance has
	 * changed.
	 */
	private fun fireExclusionStatePropertyChangedEvent(travelTime: TravelTime) {
		MLog.i(TAG, "Firing a PCE on property name "
			+ PROPERTY_TOTAL_TRAVEL_TIME + " with value " + travelTime)
		support.firePropertyChange(PROPERTY_TOTAL_TRAVEL_TIME, null, this)
	}

	fun getTravelTimes(): List<TravelTime> {
		return travelTimes
	}

	/**
	 * Sets the 'isIncludedInTotal' state of the F3TravelTimes from the states
	 * last persisted to disk using 'saveExclusionStates". The recovered state
	 * is stored in the 'isIncludedInTotal' property of each F3TravelTime, as
	 * identified by its segmentId property. Database must have been primed
	 * prior to this.
	 */
	private fun loadExclusionStates() {
		MLog.i(TAG, "Loading exclusion states")
		// To begin with mark all as included
		for (travelTime in travelTimes) {
			travelTime.setIncludedInTotalSilently(true)
		}
		val excludedSegmentIds = prefs.getStringSet(
			EXCLUSION_STATE_PREFS_KEY, null)

		MLog.i(TAG, "Exclusion set as loaded from prefs is:")
		MLog.i(TAG, "excludedSegmentIds = " + excludedSegmentIds)

		if (excludedSegmentIds != null) {
			for (segId in excludedSegmentIds) {
				MLog.i(TAG, segId + ",")
			}

			for (excludedSegmentId in excludedSegmentIds) {
				// Find the TravelTime instance in the cameraCache with this segment id
				// and mark it as excluded
				for (travelTime in travelTimes) {
					if (travelTime.segmentId == excludedSegmentId) {
						travelTime.setIncludedInTotalSilently(false)
					}
				}
			}
		}
	}

	/**
	 * segmentId - segment id of the TravelTime that is to be marked as
	 * 'excluded' travelTImes - all known TravelTime instances for the parent
	 * motorway
	 */
//	fun markTravelTimeAsExcluded(segmentId: String, fromTimes: List<*>) {
//	}

	/**
	 * Initializes the database with an array of travel times. The are the
	 * 'subject' of any subsequent 'save' and 'load' operations. times -
	 * Instances of F3TravelTime in no particular order
	 */
	fun primeWithTravelTimes(times: List<TravelTime>?) {
		removeSelfAsPropertyChangeListener()

		travelTimes.clear()
		if (times != null) {
			travelTimes.addAll(times)
		}
		isPrimed = true

		addSelfAsPropertyChangeListener()

		loadExclusionStates()
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		val source = event.source as TravelTime

		if (event.propertyName == TravelTime.PROPERTY_INCLUDED_IN_TOTAL) {
			if (!source.isTotal) {
				MLog.i(TAG,
					   "MotorwayTTDb gets notices that property "
						   + event.propertyName
						   + " has changed for segment id "
						   + source.segmentId)
				saveExclusionStates()
				fireExclusionStatePropertyChangedEvent(source)
			}
		}
	}

	fun removePropertyChangeListener(listener: PropertyChangeListener) {
		support.removePropertyChangeListener(listener)
	}

	private fun removeSelfAsPropertyChangeListener() {
			for (seg in travelTimes) {
				seg.removePropertyChangeListener(this)
			}
	}

	/**
	 * Saves the 'isIncludedInTotal' state of the TravelTimes in the database to
	 * disk but only if it is NO. The database must have been primed prior to
	 * this.Order is not important.
	 */
	private fun saveExclusionStates() {
		MLog.d(TAG, "Saving exclusion states for motorway " + config?.motorwayName)
		val excludedSegmentIds = HashSet<String>()

		for (travelTime in travelTimes) {
			if (!travelTime.isIncludedInTotal) {
				MLog.d(TAG, "Segment " + travelTime.segmentId
					+ " is excluded so save to prefs")
				val segmentIdStr: String = travelTime.segmentId ?: ""
				if (!segmentIdStr.isEmpty()) {
					excludedSegmentIds.add(segmentIdStr)
				}
			}
		}
		val editor = prefs.edit()
		editor.putStringSet(EXCLUSION_STATE_PREFS_KEY, excludedSegmentIds)
		editor.commit()
	}

	companion object {
		private val EXCLUSION_STATE_PREFS_KEY = "EXCLUDED"
		/**
		 * This database supports only this property. A PropertyChangeEvent is
		 * broadcast every time one of the TravelTimes currently in the database
		 * experiences a change in the value of its 'isIncludedInTotal' property.
		 * The new value of the property is the TravelTimeinstance that changed.
		 * This signals to listeners that the current total traveltime for some
		 * direction of travel on this motorway will need recalculating.
		 */
		val PROPERTY_TOTAL_TRAVEL_TIME = "totalTravelTime"
		private val TAG = MotorwayTravelTimesDatabase::class.java
			.simpleName
	}
}
