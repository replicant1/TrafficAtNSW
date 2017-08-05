package rod.bailey.trafficatnsw.common.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.TrafficCamera
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsTask
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.data.TravelTimeConfig
import rod.bailey.trafficatnsw.traveltime.data.TravelTimesCacheSingleton
import rod.bailey.trafficatnsw.util.MLog
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Inject

/**
 * Created by rodbailey on 5/8/17.
 */
class RemoteDataService : IDataService {

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var cameraCache: TrafficCameraCacheSingleton

	@Inject
	lateinit var ttCache: TravelTimesCacheSingleton

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	companion object {
		private val LOG_TAG: String = RemoteDataService::class.java.simpleName
	}

	override fun getHazards(): List<XHazard>? {
		val jsonStr: String? = loadIncidentsFromRemoteJSONFile()
		return XHazard.parseIncidentJson(jsonStr)
	}

	override fun getMotorwayTravelTimes(motorway: TravelTimeConfig): MotorwayTravelTimesDatabase? {
		return ttCache.loadTravelTimesFromRemoteJSONFile(TrafficAtNSWApplication.context, motorway)
	}

	override fun getTrafficCameraImage(trafficCameraId: Int): Bitmap? {
		val camera: TrafficCamera? = cameraCache.getCamera(trafficCameraId)
		val urlToLoad:String? = camera?.url
		val stream = java.net.URL(urlToLoad).openStream()
		return BitmapFactory.decodeStream(stream)
	}

	private fun loadIncidentsFromRemoteJSONFile(): String? {
		var bufferedReader: BufferedReader? = null
		var result: String? = null

		try {
			MLog.d(LOG_TAG, "Loading incidents from remote JSON file")
			val url = URL(config.remoteIncidentsJSONFile())
			val inStreamReader = InputStreamReader(url.openStream())
			bufferedReader = BufferedReader(inStreamReader)
			var line: String?
			val lineBuffer = StringBuffer()
			var eof: Boolean = false

			while (!eof) {
				line = bufferedReader.readLine()
				if (line == null) {
					eof = true
				} else {
					lineBuffer.append(line)
				}
			}

			bufferedReader.close()
			result = lineBuffer.toString()
		}
		catch (e: Throwable) {
			MLog.w(LOG_TAG, "Failed to load hazards JSON", e)
		}
		finally {
			try {
				bufferedReader?.close()
			}
			catch (e: IOException) {
				MLog.w(LOG_TAG, e)
			}
		}

		return result
	}
}