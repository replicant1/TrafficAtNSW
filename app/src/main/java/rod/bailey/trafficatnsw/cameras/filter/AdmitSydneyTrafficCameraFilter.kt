package rod.bailey.trafficatnsw.cameras.filter

import rod.bailey.trafficatnsw.cameras.data.XCamera
import rod.bailey.trafficatnsw.hazard.data.XRegion

class AdmitSydneyTrafficCameraFilter : ITrafficCameraFilter {
	override fun admit(camera: XCamera): Boolean {
		val regionStr: String? = camera.properties?.region
		if (regionStr != null) {
			val xregion: XRegion = XRegion.valueOf(regionStr)
			return xregion.isSydney
		}
		return false
	}
}
