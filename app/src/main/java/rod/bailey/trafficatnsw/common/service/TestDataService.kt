package rod.bailey.trafficatnsw.common.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication

import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.*
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

	@Inject
	lateinit var context: Context

	companion object {
		private val LOG_TAG: String = TestDataService::class.java.simpleName

		// These files are expected to be found in the assets folder
		private val LOCAL_INCIDENTS_JSON_FILE ="12jun2013.json"
		private val LOCAL_TRAFFIC_CAMERA_IMAGE = "sample_traffic_camera_image.jpg"
	}

	override fun getHazards(): List<XHazard>? {
		return XHazard.parseIncidentJson(
			AssetUtils.loadAssetFileAsString(context, LOCAL_INCIDENTS_JSON_FILE))
	}

	override fun getTrafficCameraImage(trafficCameraId: Int): Bitmap? {
		return AssetUtils.loadAssetFileAsImage(context, LOCAL_TRAFFIC_CAMERA_IMAGE)
	}

	override fun getMotorwayTravelTimes(motorway: TravelTimeConfig): MotorwayTravelTimesDatabase? {
		val jsonStr:String = AssetUtils.loadAssetFileAsString(context, motorway.localJsonFileName)
		val times: List<TravelTime> = TravelTime.Companion.parseTravelTimesJson(jsonStr)
		val result = MotorwayTravelTimesDatabase(context, motorway)
		result.primeWithTravelTimes(times)
		return result
	}
}
