package rod.bailey.trafficatnsw.hazard.data

import rod.bailey.trafficatnsw.hazard.filter.AdmitAllHazardFilter
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter
import java.util.*

/**
 * Instances of this class should always be injected as we rely on Dagger to keep it a
 * Singleton.
 */
class HazardCacheSingleton {

	companion object {
		private val LOG_TAG = HazardCacheSingleton::class.java.simpleName
	}

	var filter: IHazardFilter = AdmitAllHazardFilter()
	private val unfilteredHazardsPerRegion = HashMap<XRegion, MutableList<XHazard>>()
	private val filteredHazardsPerRegion = HashMap<XRegion, MutableList<XHazard>>()

	@Synchronized
	fun init(hazardsJSON: String) {
		val allHazards: List<XHazard>? = XHazardCollection.Companion.parseIncidentJson(hazardsJSON).hazards
		if (allHazards != null) {
			init(allHazards)
		}
	}

	@Synchronized
	fun init(hazards: List<XHazard>) {
		prime(hazards)
		filter()
	}

	private fun prime(allHazards: List<XHazard>) {
		unfilteredHazardsPerRegion.clear()
		// Put hazards into unfiltered hazards map
		for (hazard in allHazards) {
			val props: XProperties? = hazard.properties
			if ((props != null) && (props.isEnded != null) && (!props.isEnded)) {
				if ((props.roads != null) && (props.roads.size > 0)) {
					val regionStr: String? = props.roads[0].region
					if (regionStr != null) {
						val region = XRegion.valueOf(regionStr)
						// Add this hazard into the unfiltered map
						if (!unfilteredHazardsPerRegion.containsKey(region)) {
							unfilteredHazardsPerRegion.put(region, LinkedList<XHazard>())
						}

						unfilteredHazardsPerRegion[region]?.add(hazard)
					}
				}
			}
		}
	}

	fun getFilteredHazardsForRegion(region: XRegion): List<XHazard>? {
		return filteredHazardsPerRegion.get(region)
	}

	fun getUnfilteredHazard(hazardId: Int): XHazard? {
		var result: XHazard? = null
		val hazardCollections = unfilteredHazardsPerRegion.values
		for (hazardList in hazardCollections) {
			for (hazard in hazardList) {
				if (hazard.hazardId == hazardId) {
					result = hazard
					break
				}
			}
		}
		return result
	}

	private fun filter() {
		filteredHazardsPerRegion.clear()

		for (region in unfilteredHazardsPerRegion.keys) {
			val unfilteredHazards = unfilteredHazardsPerRegion[region]

			if ((unfilteredHazards != null) && !unfilteredHazards.isEmpty()) {
				val filteredHazards = LinkedList<XHazard>()

				for (hazard in unfilteredHazards) {
					if (filter.admit(hazard)) {
						filteredHazards.add(hazard)
					}
				}

				if (!filteredHazards.isEmpty()) {
					filteredHazardsPerRegion.put(region, filteredHazards)
				}
			}
		}
	}
}
