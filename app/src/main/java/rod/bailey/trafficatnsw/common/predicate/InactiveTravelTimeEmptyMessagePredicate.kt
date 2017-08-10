package rod.bailey.trafficatnsw.common.predicate

import android.widget.ListAdapter
import rod.bailey.trafficatnsw.traveltime.overview.TravelTimesListAdapter
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem
import rod.bailey.trafficatnsw.traveltime.item.SimpleTTListItem

/**
 * Determines that the 'empty' message should only be shown if the given
 * TravelTimesListAdapter contains ONE OR MORE TravelTimes that is inactive.
 */
class InactiveTravelTimeEmptyMessagePredicate : IEmptyMessagePredicate {
	private val TAG = InactiveTravelTimeEmptyMessagePredicate::class.java.simpleName

	override fun showEmptyMessage(adapter: ListAdapter): Boolean {
		val ttadapter: TravelTimesListAdapter = adapter as TravelTimesListAdapter
		var foundAnInctiveSegment: Boolean = false

		for (i in 0..ttadapter.count - 1) {
			val item = ttadapter.getItem(i) as ITTListItem
			if (item is SimpleTTListItem) {
				val travelTime = item.travelTime

				if (!travelTime.isTotal && (travelTime.properties?.isActive == false)) {
					foundAnInctiveSegment = true
					break
				}
			}
		}

		return foundAnInctiveSegment
	}
}
