package rod.bailey.trafficatnsw.traveltime.item

import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment
import java.util.*

/**
 * Creates a list of model items that represents the contents of a MotorwayTravelTimeStore.
 */
class TTListItemFactory(store: MotorwayTravelTimesStore) {

	private val travelTimes: LinkedList<XTravelTimeSegment> = store.getTravelTimes()
	private val motorwayConfig: MotorwayConfig? = store.config

	fun createTTListItems() : List<ITTListItem> {
		val result: LinkedList<ITTListItem> = createItemList()
		updateTotalsItems(result)
		return result
	}

	private fun createItemList(): LinkedList<ITTListItem> {
		val result = LinkedList<ITTListItem>()

		// The first heading identifies the direction of travel for the first road segments
		val firstTT = travelTimes[0]
		val segmentId: String? = firstTT.segmentId

		if (segmentId != null) {
			val firstTTIdPrefix = segmentId.substring(0, 1)
			val topHeadingText: String?
			val bottomHeadingText: String?

			if (firstTTIdPrefix == motorwayConfig?.forwardSegmentIdPrefix) {
				topHeadingText = motorwayConfig.forwardName
				bottomHeadingText = motorwayConfig.backwardName
			} else {
				topHeadingText = motorwayConfig?.backwardName
				bottomHeadingText = motorwayConfig?.forwardName
			}

			// Insert the top heading at the beginning of the item list
			result.add(HeadingTTListItem(topHeadingText ?: ""))

			// Insert the bottom heading after the last travel time with
			// the same segmentId prefix as the firstTTIdPrefix
			var bottomHeadingAdded = false

			for (travelTime in travelTimes) {
				val aTT = travelTime.segmentId
				if (aTT != null) {
					val segmentIdPrefix = aTT.substring(0, 1)

					if (segmentIdPrefix != firstTTIdPrefix && !bottomHeadingAdded) {
						result.add(HeadingTTListItem(bottomHeadingText ?: ""))
						bottomHeadingAdded = true
					}
				}

				result.add(SimpleTTListItem(travelTime))
			}
		}

		return result
	}

	/**
	 * Find the TT segments that represent total travel times for both forward
	 * and backward direction e.g. ETOTAL and WTOTAL then update their
	 * 'travelTimeInMinutes' attributes so tat they correctly reflect total
	 * travel time. Account for some segments having been excluded from the
	 * calculation by the user, and for some segments being inactive due to
	 * sensor faults etc.
	 */
	private fun updateTotalsItems(items: LinkedList<ITTListItem>) {
		// Total up the times in the forward direction
		var topHeadingItemIndex = -1
		var bottomHeadingItemIndex = -1

		for (i in items.indices) {
			val item = items[i]

			if (item is HeadingTTListItem) {
				if (topHeadingItemIndex == -1) {
					topHeadingItemIndex = i
				} else {
					bottomHeadingItemIndex = i
				}
			}
		}
		var topTotal = 0
		for (j in topHeadingItemIndex + 1..bottomHeadingItemIndex - 1) {
			val ttItem = items[j] as SimpleTTListItem
			val tt = ttItem.travelTime

			if (tt.isTotal) {
				tt.properties?.travelTimeMinutes = topTotal
				break
			} else {
				if ((tt.includedInTotal == true) && (tt.properties?.isActive == true)) {
					topTotal += tt.properties.travelTimeMinutes ?: 0
				}
			}
		}
		var bottomTotal = 0
		for (k in bottomHeadingItemIndex + 1..items.size - 1) {
			val ttItem = items[k] as SimpleTTListItem
			val tt = ttItem.travelTime

			if (tt.isTotal) {
				tt.properties?.travelTimeMinutes = bottomTotal
				break
			} else {
				if ((tt.includedInTotal == true) && (tt.properties?.isActive == true)) {
					bottomTotal += tt.properties.travelTimeMinutes ?: 0
				}
			}
		}
	}

}