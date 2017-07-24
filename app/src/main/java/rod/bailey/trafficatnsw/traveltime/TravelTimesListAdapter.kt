package rod.bailey.trafficatnsw.traveltime

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import java.util.Collections
import java.util.LinkedList
import rod.bailey.trafficatnsw.traveltime.common.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.common.TravelTime
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig
import rod.bailey.trafficatnsw.ui.view.ListHeadingView

class TravelTimesListAdapter(db: MotorwayTravelTimesDatabase?) : BaseAdapter(), ListAdapter {
	/** An Item that is simply a heading e.g. "Eastbound" or "Westbound"  */
	inner class HeadingItem(val text: String) : Item

	/**
	 * Every element in the list implements this interface. There are two
	 * implementations of it - one for Headings and another for travel time road
	 * segments
	 */
	interface Item

	/** An Item that is not a heading but a travel time road segment  */
	inner class TravelTimeItem(val travelTime: TravelTime) : Item

	private val items: List<Item>
	private val travelTimes: LinkedList<TravelTime> = LinkedList<TravelTime>()
	private val config: TravelTimeConfig?

	init {
		if (db?.getTravelTimes() != null) {
			travelTimes.addAll(db.getTravelTimes())
		}
		Collections.sort(travelTimes)
		config = db?.config
		items = createItemList()

		updateTotalsItems()
	}

	/**
	 * Find the TT segments that represent total travel times for both forward
	 * and backward direction e.g. ETOTAL and WTOTAL then update their
	 * 'travelTimeInMinutes' attributes so tat they correctly reflect total
	 * travel time. Account for some segments having been excluded from the
	 * calculation by the user, and for some segments being inactive due to
	 * sensor faults etc.
	 */
	private fun updateTotalsItems() {
		// Total up the times in the forward direction
		var topHeadingItemIndex = -1
		var bottomHeadingItemIndex = -1

		for (i in items.indices) {
			val item = items[i]

			if (item is HeadingItem) {
				if (topHeadingItemIndex == -1) {
					topHeadingItemIndex = i
				} else {
					bottomHeadingItemIndex = i
				}
			}
		}
		var topTotal = 0
		for (j in topHeadingItemIndex + 1..bottomHeadingItemIndex - 1) {
			val ttItem = items[j] as TravelTimeItem
			val tt = ttItem.travelTime

			if (tt.isTotal) {
				tt.setTravelTimeMinutes(topTotal)
				break
			} else {
				if (tt.isIncludedInTotal && tt.isActive) {
					topTotal += tt.getTravelTimeMinutes()
				}
			}
		}
		var bottomTotal = 0
		for (k in bottomHeadingItemIndex + 1..items.size - 1) {
			val ttItem = items[k] as TravelTimeItem
			val tt = ttItem.travelTime

			if (tt.isTotal) {
				tt.setTravelTimeMinutes(bottomTotal)
				break
			} else {
				if (tt.isIncludedInTotal && tt.isActive) {
					bottomTotal += tt.getTravelTimeMinutes()
				}
			}
		}
	}

	override fun isEnabled(position: Int): Boolean {
		val item = items[position]
		var result = false

		if (item is TravelTimeItem) {
			val dataObj = item.travelTime
			// The row in the travel time can only be selected by the user iff
			// it is not a TOTAL row AND if the corresponding segment is active.
			result = !dataObj.isTotal && dataObj.isActive
		}

		return result
	}

	private fun createItemList(): List<Item> {
		val result = LinkedList<Item>()
		// The first heading identifies the direction of travel for the
		// first road segments
		val firstTT = travelTimes[0]
		val firstTTIdPrefix = firstTT.segmentId!!.substring(0, 1)
		var topHeadingText: String?
		var bottomHeadingText: String?

		if (firstTTIdPrefix == config?.forwardSegmentIdPrefix) {
			topHeadingText = config.forwardName
			bottomHeadingText = config.backwardName
		} else {
			topHeadingText = config?.backwardName
			bottomHeadingText = config?.forwardName
		}
		// Insert the top heading at the beginning of the item list
		result.add(HeadingItem(topHeadingText ?: ""))
		// Insert the bottom heading after the last travel time with
		// the same segmentId prefix as the firstTTIdPrefix
		var bottomHeadingAdded = false

		for (travelTime in travelTimes) {
			val segmentIdPrefix = travelTime.segmentId!!.substring(0, 1)

			if (segmentIdPrefix != firstTTIdPrefix && !bottomHeadingAdded) {
				result.add(HeadingItem(bottomHeadingText ?: ""))
				bottomHeadingAdded = true
			}

			result.add(TravelTimeItem(travelTime))
		}

		return result
	}

	override fun getCount(): Int {
		return items.size
	}

	override fun getItem(position: Int): Any {
		return items[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var result: View?
		val item = items[position]

		if (item is HeadingItem) {
			val heading = ListHeadingView(parent.context,
										  item.text, false)
			result = heading
		} else {
			val ttItem = item as TravelTimeItem
			result = TravelTimesListItemView(parent.context,
											 ttItem.travelTime)
		}

		return result
	}

	companion object {
		private val TAG = TravelTimesListAdapter::class.java
			.simpleName
	}
}
