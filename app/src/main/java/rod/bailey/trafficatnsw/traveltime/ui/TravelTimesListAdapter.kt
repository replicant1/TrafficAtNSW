package rod.bailey.trafficatnsw.traveltime.ui

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.common.ui.ListHeadingView_
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig
import java.util.*

class TravelTimesListAdapter(db: MotorwayTravelTimesStore?) : BaseAdapter(), ListAdapter {

	/** An Item that is simply a heading e.g. "Eastbound" or "Westbound"  */
	inner class HeadingItem(val text: String) : Item

	/**
	 * Every element in the list implements this interface. There are two
	 * implementations of it - one for Headings and another for travel time road
	 * segments
	 */
	interface Item

	/** An Item that is not a heading but a travel time road segment  */
	inner class TravelTimeItem(val travelTime: XTravelTimeSegment) : Item

	private val items: List<Item>
	private val travelTimes: LinkedList<XTravelTimeSegment> = LinkedList<XTravelTimeSegment>()
	private val config: MotorwayConfig?

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
				tt.properties?.travelTimeMinutes = topTotal
				break
			} else {
				if ((tt.includedInTotal == true) && (tt.properties?.isActive == true)) {
					topTotal += tt.properties?.travelTimeMinutes ?: 0
				}
			}
		}
		var bottomTotal = 0
		for (k in bottomHeadingItemIndex + 1..items.size - 1) {
			val ttItem = items[k] as TravelTimeItem
			val tt = ttItem.travelTime

			if (tt.isTotal) {
				tt.properties?.travelTimeMinutes = bottomTotal
				break
			} else {
				if ((tt.includedInTotal == true) && (tt.properties?.isActive == true)) {
					bottomTotal += tt.properties?.travelTimeMinutes ?: 0
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
			result = !dataObj.isTotal && (dataObj.properties?.isActive == true)
		}

		return result
	}

	private fun createItemList(): List<Item> {
		val result = LinkedList<Item>()
		// The first heading identifies the direction of travel for the
		// first road segments
		val firstTT = travelTimes[0]
		val segmentId: String? = firstTT.segmentId

		if (segmentId != null) {
			val firstTTIdPrefix = segmentId.substring(0, 1)
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
				val aTT = travelTime.segmentId
				if (aTT != null) {
					val segmentIdPrefix = aTT.substring(0, 1)

					if (segmentIdPrefix != firstTTIdPrefix && !bottomHeadingAdded) {
						result.add(HeadingItem(bottomHeadingText ?: ""))
						bottomHeadingAdded = true
					}
				}

				result.add(TravelTimeItem(travelTime))
			}
		}

		return result
	}

	override fun getCount(): Int = items.size
	override fun getItem(position: Int): Any = items[position]
	override fun getItemId(position: Int): Long = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var result: View
		val item = items[position]

		if (item is HeadingItem) {
			result = ListHeadingView_.build(parent.context, item.text)
		} else {
			val ttItem = item as TravelTimeItem
			result = TravelTimesListItemView_.build(parent.context, ttItem.travelTime)
		}

		return result
	}
}
