package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XProperties
import rod.bailey.trafficatnsw.hazard.data.XRegion
import rod.bailey.trafficatnsw.util.MLog

class AdmitSydneyHazardFilter : IHazardFilter {
	override fun admit(hazard: XHazard): Boolean {
		var result = false
		val props: XProperties? = hazard.properties

		if (props != null) {
			val roads = props.roads

			if ((roads != null) && !roads.isEmpty()) {
				val road = roads[0]
				val roadRegionStr: String? = road.region

				if (roadRegionStr != null) {
					try {
						val roadRegion = XRegion.valueOf(roadRegionStr)
						result = roadRegion.isSydney
					}
					catch (thr: Throwable) {
						MLog.w(TAG,
							   "Found a region sring that could not be decoded to an enum element: " + roadRegionStr,
							   thr)
					}
				}
			}
		}

		return result
	}

	companion object {
		private val TAG = AdmitSydneyHazardFilter::class.java.simpleName
	}
}
