package rod.bailey.trafficatnsw.traveltime.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig;
import rod.bailey.trafficatnsw.util.MLog;

/**
 * Knows the 'isIncludedInTotal' state of each F3TravelTime that the user has
 * specified. Also knows how to store those inclusion states to disk and restore
 * them from disk. Note:contains data for a single motorway only.
 */
public class MotorwayTravelTimesDatabase implements PropertyChangeListener {

	private static final String EXCLUSION_STATE_PREFS_KEY = "EXCLUDED";

	/**
	 * This database supports only this property. A PropertyChangeEvent is
	 * broadcast every time one of the TravelTimes currently in the database
	 * experiences a change in the value of its 'isIncludedInTotal' property.
	 * The new value of the property is the TravelTimeinstance that changed.
	 * This signals to listeners that the current total traveltime for some
	 * direction of travel on this motorway will need recalculating.
	 */
	public static final String PROPERTY_TOTAL_TRAVEL_TIME = "totalTravelTime";
	private static final String TAG = MotorwayTravelTimesDatabase.class
			.getSimpleName();
	private TravelTimeConfig config;
	private SharedPreferences prefs;

	private boolean primed;

	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	private List<TravelTime> travelTimes;

	/**
	 * @param Name
	 *            of file in which the exlusions are stored. This is the basic
	 *            name only, not the full path.
	 */
	public MotorwayTravelTimesDatabase(Context ctx, TravelTimeConfig config) {
		assert config != null;
		this.config = config;

		prefs = ctx.getSharedPreferences(config.preferencesFileName,
				Context.MODE_PRIVATE);
	}

	/**
	 * @param listener
	 *            Listener will be notified whenever PROPERTY_TOTAL_TRAVEL_TIME
	 *            changes.
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	/**
	 * Add this database as a listener for changes in any property of any of the
	 * TravelTImes in the database.
	 */
	private void addSelfAsPropertyChangeListener() {
		if (travelTimes != null) {
			for (TravelTime seg : travelTimes) {
				seg.addPropertyChangeListener(this);
			}
		}
	}

	/**
	 * Notifies all registered listeners that the value of the
	 * 'isIncludedInTotal' property of the given TravelTime instance has
	 * changed.
	 */
	private void fireExclusionStatePropertyChangedEvent(TravelTime travelTime) {
		MLog.i(TAG, "Firing a PCE on property name "
				+ PROPERTY_TOTAL_TRAVEL_TIME + " with value " + travelTime);
		support.firePropertyChange(PROPERTY_TOTAL_TRAVEL_TIME, null, this);
	}

	public TravelTimeConfig getConfig() {
		return config;
	}

	public List<TravelTime> getTravelTimes() {
		assert primed;
		return travelTimes;
	}

	public boolean isPrimed() {
		return primed;
	}

	/**
	 * Sets the 'isIncludedInTotal' state of the F3TravelTimes from the states
	 * last persisted to disk using 'saveExclusionStates". The recovered state
	 * is stored in the 'isIncludedInTotal' property of each F3TravelTime, as
	 * identified by its segmentId property. Database must have been primed
	 * prior to this.
	 */
	private void loadExclusionStates() {
		MLog.i(TAG, "Loading exclusion states");

		// To begin with mark all as included
		for (TravelTime travelTime : travelTimes) {
			travelTime.setIncludedInTotalSilently(true);
		}

		Set<String> excludedSegmentIds = prefs.getStringSet(
				EXCLUSION_STATE_PREFS_KEY, null);

		MLog.i(TAG, "Exclusion set as loaded from prefs is:");
		MLog.i(TAG, "excludedSegmentIds = " + excludedSegmentIds);

		if (excludedSegmentIds != null) {
			for (String segId : excludedSegmentIds) {
				MLog.i(TAG, segId + ",");
			}

			for (String excludedSegmentId : excludedSegmentIds) {
				// Find the TravelTime instance in the db with this segment id
				// and mark it as excluded
				for (TravelTime travelTime : travelTimes) {
					if (travelTime.getSegmentId().equals(excludedSegmentId)) {
						travelTime.setIncludedInTotalSilently(false);
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
	public void markTravelTimeAsExcluded(String segmentId, List fromTimes) {

	}

	/**
	 * Initializes the database with an array of travel times. The are the
	 * 'subject' of any subsequent 'save' and 'load' operations. times -
	 * Instances of F3TravelTime in no particular order
	 */
	public void primeWithTravelTimes(List<TravelTime> times) {
		assert times != null;

		removeSelfAsPropertyChangeListener();

		travelTimes = new LinkedList<TravelTime>(times);
		primed = true;

		addSelfAsPropertyChangeListener();

		loadExclusionStates();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		TravelTime source = (TravelTime) event.getSource();

		if (event.getPropertyName().equals(
				TravelTime.PROPERTY_INCLUDED_IN_TOTAL)) {
			if (!source.isTotal()) {
				MLog.i(TAG,
						"MotorwayTTDb gets notices that property "
								+ event.getPropertyName()
								+ " has changed for segment id "
								+ source.getSegmentId());
				saveExclusionStates();
				fireExclusionStatePropertyChangedEvent(source);
			}
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	private void removeSelfAsPropertyChangeListener() {
		if (travelTimes != null) {
			for (TravelTime seg : travelTimes) {
				seg.removePropertyChangeListener(this);
			}
		}
	}

	/**
	 * Saves the 'isIncludedInTotal' state of the TravelTimes in the database to
	 * disk but only if it is NO. The database must have been primed prior to
	 * this.Order is not important.
	 */
	private void saveExclusionStates() {
		MLog.d(TAG, "Saving exclusion states for motorway "
				+ config.motorwayName);

		Set<String> excludedSegmentIds = new HashSet<String>();

		for (TravelTime travelTime : travelTimes) {
			if (!travelTime.isIncludedInTotal()) {
				MLog.d(TAG, "Segment " + travelTime.getSegmentId()
						+ " is excluded so save to prefs");
				excludedSegmentIds.add(travelTime.getSegmentId());
			}
		}

		Editor editor = prefs.edit();
		editor.putStringSet(EXCLUSION_STATE_PREFS_KEY, excludedSegmentIds);
		editor.commit();
	}
}
