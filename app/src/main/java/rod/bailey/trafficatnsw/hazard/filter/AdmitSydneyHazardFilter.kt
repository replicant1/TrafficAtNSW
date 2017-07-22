package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.json.hazard.XRegion
import rod.bailey.trafficatnsw.json.hazard.XRoad
import rod.bailey.trafficatnsw.util.MLog

class AdmitSydneyHazardFilter : IHazardFilter {
	override fun admit(hazard: XHazard): Boolean {
		assert(hazard != null)
		var result = false
		val roads = hazard.roads

		if (roads != null && !roads.isEmpty()) {
			val road = roads[0]
			val roadRegionStr = road.region

			if (roadRegionStr != null) {
				try {
					val roadRegion = XRegion.valueOf(roadRegionStr)
					if (roadRegion != null) {
						result = roadRegion.isSydney
					}
				}
				catch (thr: Throwable) {
					MLog.w(TAG,
						   "Found a region sring that could not be decoded to an enum element: " + roadRegionStr,
						   thr)
				}
			}
		}

		return result
	}

	companion object {
		private val TAG = AdmitSydneyHazardFilter::class.java
			.simpleName
	}
}
