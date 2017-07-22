package rod.bailey.trafficatnsw.hazard

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.json.hazard.XRegion
import rod.bailey.trafficatnsw.ui.HazardListItemView
import rod.bailey.trafficatnsw.ui.ListHeadingView
import rod.bailey.trafficatnsw.util.MLog
import java.util.*

class HazardListAdapter() : BaseAdapter(), ListAdapter {
	private val db: HazardCacheSingleton
	private val listData = ArrayList<Any>()

	init {
		db = HazardCacheSingleton.instance
		primeListDataFromHazardDatabase()
	}

	fun primeListDataFromHazardDatabase() {
		MLog.d(TAG, "About to collate the listData from HazardDatase contents")

		for (region in sortedHazardRegions()) {
			listData.add(region)
			val hazards = db.getFilteredHazardsForRegion(region)
			Collections.sort(hazards!!)
			listData.addAll(hazards)
		}
	}

	private fun createHazardListItem(ctx: Context, hazard: XHazard): View {
		return HazardListItemView(ctx, hazard, true, true)
	}

	private fun createHeading(ctx: Context, region: XRegion): View {
		return ListHeadingView(ctx, region.description, false)
	}

	override fun getCount(): Int {
		return listData.size
	}

	override fun getItem(position: Int): Any {
		return listData[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var result: View?
		val listItem = listData[position]

		if (listItem is XRegion) {
			result = createHeading(parent.context, listItem)
		} else {
			result = createHazardListItem(parent.context, listItem as XHazard)
		}

		return result
	}

	override fun isEnabled(position: Int): Boolean {
		val dataObj = listData[position]
		return dataObj is XHazard
	}

	private fun sortedHazardRegions(): List<XRegion> {
		val sortedRegions = LinkedList<XRegion>()

		for (region in XRegion.values()) {
			val hazardsForRegion = db.getFilteredHazardsForRegion(region)

			if (hazardsForRegion != null && !hazardsForRegion.isEmpty()) {
				sortedRegions.add(region)
			}
		}

		return sortedRegions
	}

	companion object {
		private val TAG = HazardListAdapter::class.java.simpleName
	}
}
