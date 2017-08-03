package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.hazard.data.XHazard

interface IHazardFilter {

	/**
	 * @param hazard XHazard to be filtered.
	 * @return true if the given hazard is admitted (gets through) this filter
	 */
	fun admit(hazard: XHazard): Boolean
}
