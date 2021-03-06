package rod.bailey.trafficatnsw.instrument.service

import android.content.Context
import android.graphics.Bitmap
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.XCameraCollection
import rod.bailey.trafficatnsw.service.IDataService

import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection
import rod.bailey.trafficatnsw.instrument.dagger.TestComponent
import rod.bailey.trafficatnsw.traveltime.data.*
import rod.bailey.trafficatnsw.util.assetFileAsImage
import rod.bailey.trafficatnsw.util.assetFileAsString
import javax.inject.Inject

/**
 * Created by rodbailey on 5/8/17.
 */

class TestDataService : IDataService {

	init {
		(TrafficAtNSWApplication.graph as TestComponent).inject(this)
	}

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var context: Context

	companion object {
		private val LOG_TAG: String = TestDataService::class.java.simpleName

		// These files are expected to be found in the assets folder
		private val LOCAL_INCIDENTS_JSON_FILE ="hazard/test_hazards_12jun2013.json"
		private val LOCAL_TRAFFIC_CAMERA_IMAGE = "camera/test_traffic_camera_image.jpg"
	}

	override fun getHazards(): List<XHazard>? {
		return XHazardCollection.Companion.parseIncidentJson(
			context.assetFileAsString(LOCAL_INCIDENTS_JSON_FILE)).hazards
	}

	override fun getTrafficCameraImage(trafficCameraId: String): Bitmap? {
		return context.assetFileAsImage(LOCAL_TRAFFIC_CAMERA_IMAGE)
	}

	override fun getMotorwayTravelTimes(motorway: MotorwayConfig): MotorwayTravelTimesStore? {
		val jsonStr:String = context.assetFileAsString(motorway.localJsonFileName)
		val times: List<XTravelTimeSegment> = XTravelTimeCollection.Companion.parseTravelTimesJson(jsonStr).travelTimes
		val result = MotorwayTravelTimesStore(context, motorway)
		result.primeWithTravelTimes(times)
		return result
	}

	override fun getTrafficCameras(): XCameraCollection? {
		val jsonStr: String? = context.assetFileAsString("cameras.json");
		return if (jsonStr == null) null else XCameraCollection.parseCameraJson(jsonStr);
	}
}
