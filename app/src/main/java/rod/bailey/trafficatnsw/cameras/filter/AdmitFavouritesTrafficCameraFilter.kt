package rod.bailey.trafficatnsw.cameras.filter

import rod.bailey.trafficatnsw.cameras.data.TrafficCamera

class AdmitFavouritesTrafficCameraFilter : ITrafficCameraFilter {
	override fun admit(camera: TrafficCamera): Boolean {
		return camera.isFavourite
	}
}
