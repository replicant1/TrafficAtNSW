package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.hazard.data.XHazard

/**
 * Unconditionally admits all hazards
 */
class AdmitAnyHazardFilter : IHazardFilter {
	override fun admit(hazard: XHazard): Boolean {
		return true
	}
}