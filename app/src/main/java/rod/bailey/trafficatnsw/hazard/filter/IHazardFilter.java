package rod.bailey.trafficatnsw.hazard.filter;

import rod.bailey.trafficatnsw.json.hazard.XHazard;

public interface IHazardFilter {

	/**
	 * @param hazard
	 *            XHazard to be filtered. Must be non-null.
	 * @return true if teh given hazard is admitted (gets through) this filter
	 */
	public boolean admit(XHazard hazard);

}
