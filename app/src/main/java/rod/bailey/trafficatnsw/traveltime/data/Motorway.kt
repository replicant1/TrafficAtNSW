package rod.bailey.trafficatnsw.traveltime.data

import java.io.Serializable

/**
 * Each member is a motorway that we have travel time data for
 */
enum class Motorway constructor(val apiToken: String) : Serializable {
	M1("M1"), M2("M2"), M4("M4"), M7("M7")
}
