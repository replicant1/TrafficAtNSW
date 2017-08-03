package rod.bailey.trafficatnsw.traveltime.data

import android.content.Context
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.util.MLog
import javax.inject.Inject

class TravelTimesCacheSingleton {

	var m1Config: TravelTimeConfig? = null
		private set
	var m2Config: TravelTimeConfig? = null
		private set
	var m4Config: TravelTimeConfig? = null
		private set
	var m7Config: TravelTimeConfig? = null
		private set

	@Inject
	lateinit var config: ConfigSingleton

	private fun createM1Config(): TravelTimeConfig {
		return TravelTimeConfig("M1", //
																		"Northbound", //
																		"Southbound", //
																		"N", //
																		"S", //
																		"excluded_from_total_m1", //
																		config.remoteM1JSONFile(), //
																		config.localM1JSONFile())
	}

	private fun createM2Config(): TravelTimeConfig {
		return TravelTimeConfig("M2", //
																		"Eastbound", //
																		"Westbound", //
																		"E", //
																		"W", //
																		"excluded_from_total_m2", //
																		config.remoteM2JSONFile(), //
																		config.localM2JSONFile())
	}

	private fun createM4Config(): TravelTimeConfig {
		return TravelTimeConfig("M4", //
																		"Eastbound", //
																		"Westbound", //
																		"E", //
																		"W", //
																		"excluded_from_total_m4", //
																		config.remoteM4JSONFile(), //
																		config.localM4JSONFile())
	}

	private fun createM7Config(): TravelTimeConfig {
		return TravelTimeConfig("M7", //
																		"Northbound", //
																		"Southbound", //
																		"N", //
																		"S", //
																		"excluded_from_total_m7", //
																		config.remoteM7JSONFile(), //
																		config.localM7JSONFile())
	}

	fun init() {
		TrafficAtNSWApplication.graph.inject(this)

		if (m1Config == null || m2Config == null || m4Config == null || m7Config == null) {
			m1Config = createM1Config()
			m2Config = createM2Config()
			m4Config = createM4Config()
			m7Config = createM7Config()
		}
	}

	fun loadTravelTimesFromLocalJSONFile(
		ctx: Context, config: TravelTimeConfig): MotorwayTravelTimesDatabase? {

		try {
			val assetFileName = config.localJsonFileName
			MLog.d(TAG, String.format("Fetching %s from assets folder",
									  assetFileName))
			val input = ctx.assets.open(assetFileName)
			val size = input.available()

			MLog.d(TAG, String.format("Found %d available bytes in %s", size,
									  assetFileName))
			val buffer = ByteArray(size)
			input.read(buffer)
			input.close()
			// byte buffer into a string
			val text = String(buffer)
			val tts = TravelTime
				.parseTravelTimesFromJsonFile(text)

			var result = MotorwayTravelTimesDatabase(ctx, config)
			result.primeWithTravelTimes(tts)
			return result
		}
		catch (ioe: IOException) {
			MLog.e(TAG, "M4 JSON parsing failed", ioe)
		}
		return null
	}

	fun loadTravelTimesFromRemoteJSONFile(ctx: Context,
										  config: TravelTimeConfig): MotorwayTravelTimesDatabase? {
		MLog.i(TAG, "Beginning load of " + config.motorwayName + " travel times from remote file "
			+ config.remoteJsonUrl)
		var result: MotorwayTravelTimesDatabase?
		var bufferedReader: BufferedReader? = null

		try {
			val jsonUrl = URL(config.remoteJsonUrl)
			val instreamReader = InputStreamReader(
				jsonUrl.openStream())
			bufferedReader = BufferedReader(instreamReader)
			var line: String?
			val lineBuffer:StringBuffer = StringBuffer()
			var eof:Boolean = false

			while (!eof) {
				line = bufferedReader.readLine()
				if (line == null) {
					eof = true
				} else {
					lineBuffer.append(line)
				}
			}

			val text = lineBuffer.toString()
			val tts = TravelTime.parseTravelTimesFromJsonFile(text)
			result = MotorwayTravelTimesDatabase(ctx, config)
			result.primeWithTravelTimes(tts)
		}
		catch (e: Exception) {
			MLog.e(TAG, "Failed to retrive and/or parse travel times for " + config.motorwayName, e)
			result = null
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close()
				}
				catch (e: IOException) {
				}
			}
		}

		return result
	}

	companion object {
		private val TAG = TravelTimesCacheSingleton::class.java.simpleName
	}
}
