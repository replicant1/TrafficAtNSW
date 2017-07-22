package rod.bailey.trafficatnsw.hazard

import android.content.Context
import rod.bailey.trafficatnsw.hazard.filter.AdmitAllHazardFilter
import java.util.HashMap
import java.util.LinkedList
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.json.hazard.XRegion

class HazardDatabase private constructor() {
	private object Holder {
		val INSTANCE = HazardDatabase()
	}

	companion object {
		private val LOG_TAG = HazardDatabase::class.java.simpleName
		val instance: HazardDatabase by lazy { Holder.INSTANCE }
	}

	var filter: IHazardFilter = AdmitAllHazardFilter()
	private val unfilteredHazardsPerRegion = HashMap<XRegion, MutableList<XHazard>>()
	private val filteredHazardsPerRegion = HashMap<XRegion, MutableList<XHazard>>()

	@Synchronized
	fun init(hazardsJSON: String) {
		initUnfilteredHazards(hazardsJSON)
		filter()
	}

	private fun initUnfilteredHazards(hazardsJSON: String) {
		val allHazards = XHazard.parseIncidentJson(hazardsJSON)
		unfilteredHazardsPerRegion.clear()
		// Put hazards into unfiltered hazards map
		for (hazard in allHazards) {
			if (!hazard.isEnded) {
				val regionStr = hazard.roads[0].region
				val region = XRegion.valueOf(regionStr)

				// Add this hazard into the unfiltered map
				if (!unfilteredHazardsPerRegion.containsKey(region)) {
					unfilteredHazardsPerRegion.put(region, LinkedList<XHazard>())
				}

				unfilteredHazardsPerRegion[region]?.add(hazard)
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
