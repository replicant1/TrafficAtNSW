package rod.bailey.trafficatnsw.hazard

import android.content.Context
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
		private val TAG = HazardDatabase::class.java.simpleName
		val instance: HazardDatabase by lazy { Holder.INSTANCE }
	}

	private var filter: IHazardFilter? = null
	private val unfilteredHazardsPerRegion = HashMap<XRegion, MutableList<XHazard>>()
	private val filteredHazardsPerRegion = HashMap<XRegion, MutableList<XHazard>>()

	@Synchronized fun init(ctx: Context, hazardsJSON: String) {
		initUnfilteredHazards(ctx, hazardsJSON)

		if (filter != null) {
			refilter()
		}
	}

	fun unfilteredSize(): Int {
		var result = 0
		for (unfilteredHazards in unfilteredHazardsPerRegion.values) {
			result += unfilteredHazards.size
		}
		return result
	}

	fun filteredSize(): Int {
		var result = 0

		for (filteredHazards in filteredHazardsPerRegion.values) {
			result += filteredHazards.size
		}

		return result
	}

	private fun initUnfilteredHazards(ctx: Context, hazardsJSON: String) {
		val allHazards = XHazard
			.createHazardsFromIncidentJsonContents(hazardsJSON)

		unfilteredHazardsPerRegion.clear()

		// Put hazards into unfiltered hazards map
		for (hazard in allHazards) {
			if (!hazard.isEnded) {
				val regionStr = hazard.roads[0].region
				val region = XRegion.valueOf(regionStr)

				// Add this hazard into the unfiltered map
				var hazardsPerRegion: MutableList<XHazard>?

				if (unfilteredHazardsPerRegion.containsKey(region)) {
					hazardsPerRegion = unfilteredHazardsPerRegion[region]
				} else {
					hazardsPerRegion = LinkedList<XHazard>()
					unfilteredHazardsPerRegion.put(region, hazardsPerRegion)
				}

				hazardsPerRegion!!.add(hazard)
			}
		}
	}

	fun getHazardsForRegion(region: XRegion): List<XHazard>? {
		return filteredHazardsPerRegion.get(region)
	}

	fun getHazard(hazardId: Int): XHazard? {
		var result: XHazard? = null

		val hazardCollections = unfilteredHazardsPerRegion
			.values
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

	fun setFilter(filter: IHazardFilter?) {
		assert(filter != null)
		this.filter = filter
		refilter()
	}

	private fun refilter() {
		filteredHazardsPerRegion.clear()

		for (region in unfilteredHazardsPerRegion.keys) {
			val unfilteredHazards = unfilteredHazardsPerRegion[region]

			if ((unfilteredHazards != null) && !unfilteredHazards.isEmpty()) {
				val filteredHazards = LinkedList<XHazard>()

				for (hazard in unfilteredHazards) {
					if (filter!!.admit(hazard)) {
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
