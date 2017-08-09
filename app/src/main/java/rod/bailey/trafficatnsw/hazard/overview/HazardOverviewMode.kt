package rod.bailey.trafficatnsw.hazard.overview

import rod.bailey.trafficatnsw.hazard.filter.AdmitRegionalHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.AdmitSydneyHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter

/**
 * Mode of operation of HazardListFragment - determines which hazard list is displayed
 */
enum class HazardOverviewMode private constructor(val filter: IHazardFilter)  {
	REGIONAL(AdmitRegionalHazardFilter()), //
	SYDNEY(AdmitSydneyHazardFilter())
}
