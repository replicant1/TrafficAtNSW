package rod.bailey.trafficatnsw.service

import android.content.Context
import android.graphics.Bitmap
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.cameras.data.XCamera
import rod.bailey.trafficatnsw.cameras.data.XCameraCollection
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection
import rod.bailey.trafficatnsw.traveltime.data.*
import rod.bailey.trafficatnsw.util.NetUtils
import rod.bailey.trafficatnsw.util.assetFileAsString
import javax.inject.Inject

/**
 * Implementation of IDataService that should be used in production. Gets its data from
 * the livetraffic.com web site.
 */
class RemoteDataService : IDataService {

	@Inject
	lateinit var context: Context

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var cameraCache: TrafficCameraCacheSingleton

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	override fun getHazards(): List<XHazard>? {
		val jsonStr: String? = NetUtils.loadRemoteFileAsString(config.remoteIncidentsJSONFile())
		return if (jsonStr == null) null else  XHazardCollection.Companion.parseIncidentJson(jsonStr).hazards
	}

	override fun getMotorwayTravelTimes(motorway: MotorwayConfig): MotorwayTravelTimesStore? {
		var result: MotorwayTravelTimesStore? = null
		val jsonStr: String? = NetUtils.loadRemoteFileAsString(motorway.remoteJsonUrl)

		if (jsonStr != null) {
			val tts = XTravelTimeCollection.Companion.parseTravelTimesJson(jsonStr)
			result = MotorwayTravelTimesStore(context, motorway)
			result.primeWithTravelTimes(tts.travelTimes)
		}
		return result
	}

	override fun getTrafficCameraImage(trafficCameraId: String): Bitmap? {
		val camera: XCamera? = cameraCache.getUnfilteredCamera(trafficCameraId)
		val urlToLoad:String? = camera?.properties?.imageURL
		return if (urlToLoad == null) null else NetUtils.loadRemoteFileAsImage(urlToLoad)
	}

	override fun getTrafficCameras(): XCameraCollection? {
		val jsonStr: String? = context.assetFileAsString("cameras.json")
		return if (jsonStr == null) null else XCameraCollection.parseCameraJson(jsonStr);
	}
}