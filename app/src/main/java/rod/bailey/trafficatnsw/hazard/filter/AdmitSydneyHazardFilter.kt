package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XProperties
import rod.bailey.trafficatnsw.hazard.data.XRegion
import timber.log.Timber

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
					} catch (thr: Throwable) {
						Timber.w(thr, "String could not be decoded to XRegion: %s", roadRegionStr)
					}
				}
			}
		}

		return result
	}
}
