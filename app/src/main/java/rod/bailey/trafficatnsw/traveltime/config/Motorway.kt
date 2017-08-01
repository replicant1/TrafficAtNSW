package rod.bailey.trafficatnsw.traveltime.config

import java.io.Serializable

/**
 * Created by rodbailey on 1/8/17.
 */
enum class Motorway constructor(val apiToken: String) : Serializable {
	M1("M1"), M2("M2"), M4("M4"), M7("M7")
}
