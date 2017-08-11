package rod.bailey.trafficatnsw.hazard.overview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.ui.ListHeadingView_
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XRegion
import rod.bailey.trafficatnsw.hazard.overview.HazardListItemView_
import rod.bailey.trafficatnsw.util.MLog
import java.util.*
import javax.inject.Inject

/**
 * Adapts data in the HazardCacheSingleton to a list of hazards
 */
class HazardListAdapter : BaseAdapter(), ListAdapter {

	@Inject
	lateinit var hazardCacheSingleton: HazardCacheSingleton

	/** Each element is an instance of XRegion (sub-heading) or XHazard (list item) */
	private val listData = ArrayList<Any>()

	init {
		TrafficAtNSWApplication.graph.inject(this)

		MLog.d(TAG, "About to collate the listData from HazardCacheSingleton contents")
		for (region in sortedHazardRegions()) {
			listData.add(region)
			val hazards: List<XHazard>? =
				hazardCacheSingleton.getFilteredHazardsForRegion(region)
			if (hazards != null) {
				listData.addAll(hazards.sorted())
			}
		}
	}

	private fun createHazardListItem(ctx: Context, hazard: XHazard): View {
		return HazardListItemView_.build(ctx, hazard, true, true)
	}

	private fun createHeading(ctx: Context, region: XRegion): View
		= ListHeadingView_.build(ctx, region.description)

	override fun getCount(): Int = listData.size
	override fun getItem(position: Int): Any = listData[position]
	override fun getItemId(position: Int): Long = position.toLong()

	// TODO: Introduce ViewHolder pattern for efficiency
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val result: View
		val listItem: Any = listData[position]

		if (listItem is XRegion) {
			result = createHeading(parent.context, listItem)
		} else {
			result = createHazardListItem(parent.context, listItem as XHazard)
		}

		return result
	}

	override fun isEnabled(position: Int): Boolean = true
	override fun areAllItemsEnabled(): Boolean = true

	private fun sortedHazardRegions(): List<XRegion> {
		val sortedRegions = LinkedList<XRegion>()

		for (region in XRegion.values()) {
			val hazardsForRegion =
				hazardCacheSingleton.getFilteredHazardsForRegion(region)

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
