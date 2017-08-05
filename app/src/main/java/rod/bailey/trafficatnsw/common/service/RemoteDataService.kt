package rod.bailey.trafficatnsw.common.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.TrafficCamera
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsTask
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.data.TravelTime
import rod.bailey.trafficatnsw.traveltime.data.TravelTimeConfig
import rod.bailey.trafficatnsw.traveltime.data.TravelTimesCacheSingleton
import rod.bailey.trafficatnsw.util.AssetUtils
import rod.bailey.trafficatnsw.util.MLog
import rod.bailey.trafficatnsw.util.NetUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
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

	@Inject
	lateinit var ttCache: TravelTimesCacheSingleton

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	override fun getHazards(): List<XHazard>? {
		val jsonStr: String? = NetUtils.loadRemoteFileAsString(config.remoteIncidentsJSONFile())
		return XHazard.parseIncidentJson(jsonStr)
	}

	override fun getMotorwayTravelTimes(motorway: TravelTimeConfig): MotorwayTravelTimesDatabase? {
		val jsonStr: String? = NetUtils.loadRemoteFileAsString(motorway.remoteJsonUrl)
		val tts = TravelTime.Companion.parseTravelTimesJson(jsonStr)
		val result = MotorwayTravelTimesDatabase(context, motorway)
		result.primeWithTravelTimes(tts)
		return result
	}

	override fun getTrafficCameraImage(trafficCameraId: Int): Bitmap? {
		val camera: TrafficCamera? = cameraCache.getCamera(trafficCameraId)
		val urlToLoad:String? = camera?.url
		return if (urlToLoad == null) null else NetUtils.loadRemoteFileAsImage(urlToLoad)
	}
}