package rod.bailey.trafficatnsw.common.predicate

import android.widget.ListAdapter
import rod.bailey.trafficatnsw.traveltime.ui.TravelTimesListAdapter
import rod.bailey.trafficatnsw.traveltime.ui.TravelTimesListAdapter.Item
import rod.bailey.trafficatnsw.traveltime.ui.TravelTimesListAdapter.TravelTimeItem

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
			val item = ttadapter.getItem(i) as Item
			if (item is TravelTimeItem) {
				val travelTime = item.travelTime

				if (!travelTime.isTotal && !travelTime.isActive) {
					foundAnInctiveSegment = true
					break
				}
			}
		}

		return foundAnInctiveSegment
	}
}
