package rod.bailey.trafficatnsw.hazard

import rod.bailey.trafficatnsw.hazard.filter.AdmitRegionalHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.AdmitSydneyHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter

/**
 * Mode of operation of HazardListFragment
 */
enum class HazardListMode private constructor(val displayName: String, val filter: IHazardFilter)  {
	REGIONAL("Regional Incidents", AdmitRegionalHazardFilter()), //
	SYDNEY("Sydney Incidents", AdmitSydneyHazardFilter())
}
