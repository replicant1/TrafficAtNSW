package rod.bailey.trafficatnsw.cameras.filter

import rod.bailey.trafficatnsw.cameras.TrafficCamera

class AdmitRegionalTrafficCameraFilter : ITrafficCameraFilter {
	override fun admit(camera: TrafficCamera): Boolean {
		return camera.region?.isRegional ?: false
	}
}
