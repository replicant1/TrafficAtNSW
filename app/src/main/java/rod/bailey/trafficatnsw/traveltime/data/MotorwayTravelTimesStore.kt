package rod.bailey.trafficatnsw.traveltime.data

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.VisibleForTesting
import timber.log.Timber
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*

/**
 * Stores all XTravelTimeSegment instances for a single motorway. Knows how to
 * persist the the 'included in total' state (as set by the user) to disk and restore
 * them from disk.
 */
class MotorwayTravelTimesStore(ctx: Context, val config: MotorwayConfig) : PropertyChangeListener {
	private val prefs: SharedPreferences
	var isPrimed: Boolean = false
		private set
	private val support = PropertyChangeSupport(this)
	private val travelTimes: LinkedList<XTravelTimeSegment> = LinkedList<XTravelTimeSegment>()

	init {
		prefs = ctx.getSharedPreferences(config.preferencesFileName, Context.MODE_PRIVATE)
	}

	/**
	 * @param listener
	 *            Listener will be notified whenever PROPERTY_TOTAL_TRAVEL_TIME changes.
	 */
	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	/**
	 * Add this database as a listener for changes in any property of any of the
	 * TravelTimes in the store.
	 */
	private fun addSelfAsPropertyChangeListener() {
		for (seg in travelTimes) {
			seg.addPropertyChangeListener(this)
		}
	}

	/**
	 * Notifies all registered listeners that the value of the
	 * 'isIncludedInTotal' property of the given XTravelTimeSegment instance has
	 * changed.
	 */
	private fun fireTotalTravelTimePropertyChangedEvent(travelTime: XTravelTimeSegment) {
		Timber.i("Firing a PCE on property ${PROPERTY_TOTAL_TRAVEL_TIME} with value ${travelTime}")
		support.firePropertyChange(PROPERTY_TOTAL_TRAVEL_TIME, null, this)
	}

	fun getTravelTimes(): LinkedList<XTravelTimeSegment> {
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
		Timber.i("Loading exclusion states")
		// To begin with mark all as included
		for (travelTime in travelTimes) {
			travelTime.setIncludedInTotalSilently(true)
		}
		val excludedSegmentIds = prefs.getStringSet(EXCLUSION_STATE_PREFS_KEY, null)

		Timber.i("Exclusion set as loaded from prefs is:")
		Timber.i("excludedSegmentIds = ${excludedSegmentIds}")

		if (excludedSegmentIds != null) {
			for (segId in excludedSegmentIds) {
				Timber.i(segId + ",")
			}

			for (excludedSegmentId in excludedSegmentIds) {
				// Find the XTravelTimeSegment instance in the cameraCache with this segment id
				// and mark it as excluded
				for (travelTime in travelTimes) {
					if (travelTime.segmentId == excludedSegmentId) {
						travelTime.setIncludedInTotalSilently(false)
					}
				}
			}
		}
	}

	fun getSavedExcludedSegmentIds(): Set<SegmentId> {
		val result = HashSet<SegmentId>()
		val excludedSegmentIds: Set<String> = prefs.getStringSet(EXCLUSION_STATE_PREFS_KEY, null)
		for (excludedSegmentIdStr in excludedSegmentIds) {
			val segmentId: SegmentId? = SegmentId.parse(excludedSegmentIdStr)
			if (segmentId != null) {
				result.add(segmentId)
			}
		}
		return result
	}

	/**
	 * Initializes the database with an array of travel times. The are the
	 * 'subject' of any subsequent 'save' and 'load' operations. times -
	 * @param times Instances of XTravelTimeSegment in no particular order
	 */
	fun primeWithTravelTimes(times: List<XTravelTimeSegment>?) {
		removeSelfAsPropertyChangeListener()

		travelTimes.clear()
		if (times != null) {
			travelTimes.addAll(times)
		}
		isPrimed = true

		Collections.sort(travelTimes)
		addSelfAsPropertyChangeListener()
		loadExclusionStates()
	}

	@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
	fun setAllIncludedInTotal() {
		for (seg: XTravelTimeSegment in travelTimes) {
			seg.includedInTotal = true;
		}
		saveExclusionStates();
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		val source = event.source as XTravelTimeSegment

		if (event.propertyName == XTravelTimeSegment.PROPERTY_SEGMENT_INCLUDED_IN_TOTAL) {
			if (!source.isTotal) {
				Timber.i("MotorwayTTStore gets notices that property %s has changed for segment id %s",
						event.propertyName, source.segmentId)
				saveExclusionStates()
				fireTotalTravelTimePropertyChangedEvent(source)
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
		Timber.d("Saving exclusion states for motorway ${config.motorwayName}")
		val excludedSegmentIds = HashSet<String>()

		for (travelTime in travelTimes) {
			if (travelTime.includedInTotal == false) {
				Timber.d("Segment${travelTime.segmentId} is excluded so save to prefs")
				val segmentIdStr: String = travelTime.segmentId ?: ""
				if (!segmentIdStr.isEmpty()) {
					excludedSegmentIds.add(segmentIdStr)
				}
			}
		}
		val editor = prefs.edit()
		editor.putStringSet(
				EXCLUSION_STATE_PREFS_KEY, excludedSegmentIds)
		editor.commit()
	}

	companion object {
		private val EXCLUSION_STATE_PREFS_KEY = "EXCLUDED"

		/**
		 * This database supports only this property. A PropertyChangeEvent is
		 * broadcast every time one of the TravelTimes currently in the database
		 * experiences a change in the value of its [includedInTotal] property.
		 * The new value of the property is the TravelTime instance that changed.
		 * This signals to listeners that the current total travel time for some
		 * direction of travel on this motorway will need recalculating.
		 */
		val PROPERTY_TOTAL_TRAVEL_TIME = "totalTravelTime"
	}
}
