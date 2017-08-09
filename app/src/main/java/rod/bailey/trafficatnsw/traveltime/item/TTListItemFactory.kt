package rod.bailey.trafficatnsw.traveltime.item

import rod.bailey.trafficatnsw.traveltime.data.*
import java.util.*

/**
 * Creates a list of model items that represents the contents of a MotorwayTravelTimeStore.
 */
class TTListItemFactory(store: MotorwayTravelTimesStore) {

	init {
		System.out.println("** At time of setMode, store= **")
		for (segment in store.getTravelTimes()) {
			System.out.println(segment.toString());
		}
		System.out.println("** ------------------------ **");
	}

	private val travelTimes: LinkedList<XTravelTimeSegment> = store.getTravelTimes()
	private val motorwayConfig: MotorwayConfig = store.config

	fun createTTListItems(): List<ITTListItem> {
		val result: LinkedList<ITTListItem> = createItemList()
		updateTotalsItems(result)
		return result
	}

	private fun createItemList(): LinkedList<ITTListItem> {
		val result = LinkedList<ITTListItem>()

		if (!travelTimes.isEmpty()) {
			// The first heading identifies the direction of travel for the first road segments
			val firstSeg: XTravelTimeSegment = travelTimes[0]
			val firstSegId: SegmentId? = SegmentId.parse(firstSeg.segmentId ?: "")

			if (firstSegId != null) {
				val firstSegDir = firstSegId.direction
				val mwayFwdDir = SegmentDirection.Companion.findDirectionByApiToken(
					motorwayConfig.forwardSegmentIdPrefix)

				val topHeadingText: String
				val bottomHeadingText: String

				if (firstSegDir == mwayFwdDir) {
					topHeadingText = motorwayConfig.forwardName
					bottomHeadingText = motorwayConfig.backwardName
				} else {
					topHeadingText = motorwayConfig.backwardName
					bottomHeadingText = motorwayConfig.forwardName
				}

				System.out.println("** firstSegDir=${firstSegDir}")
				System.out.println("** mwayFwdDir=${mwayFwdDir}")
				System.out.println("** topHeadingText=${topHeadingText}")
				System.out.println("** bottomHeadingText=${bottomHeadingText}")

				// Insert the top heading at the beginning of the item list
				result.add(HeadingTTListItem(topHeadingText))

				// Insert the bottom heading after the last travel time with
				// the same direction as the first seg.
				var bottomHeadingAdded = false

				for (travelTime in travelTimes) {
					val thisSegId: SegmentId? = SegmentId.parse(travelTime.segmentId ?: "")
					val thisSegDir: SegmentDirection? = thisSegId?.direction

					System.out.println("** thisSegId=${thisSegId} , thisSegDir=${thisSegDir}")

					if ((thisSegDir != firstSegDir) && !bottomHeadingAdded) {
						result.add(HeadingTTListItem(bottomHeadingText))
						bottomHeadingAdded = true
					}

					result.add(SimpleTTListItem(travelTime))
				}
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