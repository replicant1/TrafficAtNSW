package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.json.hazard.XHazard

/**
 * Unconditionally admits all hazards
 */
class AdmitAllHazardFilter: IHazardFilter {
	override fun admit(hazard: XHazard): Boolean {
		return true
	}
}