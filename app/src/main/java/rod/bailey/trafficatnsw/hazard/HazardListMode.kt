package rod.bailey.trafficatnsw.hazard

import rod.bailey.trafficatnsw.hazard.filter.AdmitRegionalHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.AdmitSydneyHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter

/**
 * Mode of operation of HazardListFragment - determines which hazard list is displayed
 */
enum class HazardListMode private constructor(val filter: IHazardFilter)  {
	REGIONAL(AdmitRegionalHazardFilter()), //
	SYDNEY(AdmitSydneyHazardFilter())
}
