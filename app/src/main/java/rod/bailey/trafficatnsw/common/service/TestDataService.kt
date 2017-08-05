package rod.bailey.trafficatnsw.common.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication

import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.Motorway
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.data.TravelTimeConfig
import rod.bailey.trafficatnsw.traveltime.data.TravelTimesCacheSingleton
import rod.bailey.trafficatnsw.util.AssetUtils
import javax.inject.Inject

/**
 * Created by rodbailey on 5/8/17.
 */

class TestDataService : IDataService {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var travelTimesCache: TravelTimesCacheSingleton

	companion object {
		private val LOG_TAG: String = TestDataService::class.java.simpleName

		// These files are expected to be found in the assets folder
		private val LOCAL_INCIDENTS_JSON_FILE ="12jun2013.json"
		private val LOCAL_TRAFFIC_CAMERA_IMAGE = "sample_traffic_camera_image.jpg"
	}

	override fun getHazards(): List<XHazard>? {
		val text:String = AssetUtils.loadAssetFileAsString(TrafficAtNSWApplication.context, LOCAL_INCIDENTS_JSON_FILE)
		return XHazard.parseIncidentJson(text)
	}

	override fun getTrafficCameraImage(trafficCameraId: Int): Bitmap? {
		return AssetUtils.loadAssetFileAsImage(TrafficAtNSWApplication.context, LOCAL_TRAFFIC_CAMERA_IMAGE)
	}

	override fun getMotorwayTravelTimes(motorway: TravelTimeConfig): MotorwayTravelTimesDatabase? {
		return travelTimesCache.loadTravelTimesFromLocalJSONFile(TrafficAtNSWApplication.context, motorway)
	}
}
