package rod.bailey.trafficatnsw.traveltime.data

import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import javax.inject.Inject

/**
 * Always @Inject the singleton instance of this class to guarentee you get
 * the Singleton instance. All known motorways are registered here.
 */
class MotorwayConfigRegistry {

	val m1Config: MotorwayConfig
	val m4Config: MotorwayConfig
	val m7Config: MotorwayConfig

	@Inject
	lateinit var config: ConfigSingleton

	private fun createM1Config(): MotorwayConfig {
		return MotorwayConfig(Motorway.M1,
							  "M1", //
							  "Northbound", //
							  "Southbound", //
							  "N", //
							  "S", //
							  "excluded_from_total_m1", //
							  config.remoteM1JSONFile(), //
							  config.localM1JSONFile())
	}

	private fun createM4Config(): MotorwayConfig {
		return MotorwayConfig(Motorway.M4,
							  "M4", //
							  "Eastbound", //
							  "Westbound", //
							  "E", //
							  "W", //
							  "excluded_from_total_m4", //
							  config.remoteM4JSONFile(), //
							  config.localM4JSONFile())
	}

	private fun createM7Config(): MotorwayConfig {
		return MotorwayConfig(Motorway.M7,
							  "M7", //
							  "Northbound", //
							  "Southbound", //
							  "N", //
							  "S", //
							  "excluded_from_total_m7", //
							  config.remoteM7JSONFile(), //
							  config.localM7JSONFile())
	}

	init {
		TrafficAtNSWApplication.graph.inject(this)

		m1Config = createM1Config()
		m4Config = createM4Config()
		m7Config = createM7Config()
	}
}
