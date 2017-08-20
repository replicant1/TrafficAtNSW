package rod.bailey.trafficatnsw.cameras.filter

import rod.bailey.trafficatnsw.cameras.data.XCamera

class AdmitAnyTrafficCameraFilter : ITrafficCameraFilter {
	override fun admit(camera: XCamera): Boolean {
		return true
	}
}
