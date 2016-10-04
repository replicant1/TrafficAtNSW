package rod.bailey.trafficatnsw.hazard.filter;

import rod.bailey.trafficatnsw.json.hazard.Hazard;

public interface IHazardFilter {

	/**
	 * @param hazard
	 *            Hazard to be filtered. Must be non-null.
	 * @return true if teh given hazard is admitted (gets through) this filter
	 */
	public boolean admit(Hazard hazard);

}
