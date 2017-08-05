package rod.bailey.trafficatnsw.traveltime.data

import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
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

	companion object {
		private val TAG = TravelTimesCacheSingleton::class.java.simpleName
	}
}
