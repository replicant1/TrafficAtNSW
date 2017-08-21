package rod.bailey.trafficatnsw.traveltime.item

import android.support.annotation.VisibleForTesting
import rod.bailey.trafficatnsw.traveltime.data.*
import java.util.*

/**
 * Creates a list of model items that represents the contents of a MotorwayTravelTimeStore.
 */
class TTListItemFactory(store: MotorwayTravelTimesStore) {

	private val travelTimes: LinkedList<XTravelTimeSegment> = store.getTravelTimes()
	private val motorwayConfig: MotorwayConfig = store.config

	fun createTTListItems(): List<ITTListItem> {
		val result: LinkedList<ITTListItem> = createItemList()
		updateTotalsItems(result)
		return result
	}

	@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
	fun createItemList(): LinkedList<ITTListItem> {
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

				// Insert the top heading at the beginning of the item list
				result.add(HeadingTTListItem(topHeadingText))

				// Insert the bottom heading after the last travel time with
				// the same direction as the first seg.
				var bottomHeadingAdded = false

				for (travelTime in travelTimes) {
					val thisSegId: SegmentId? = SegmentId.parse(travelTime.segmentId ?: "")
					val thisSegDir: SegmentDirection? = thisSegId?.direction

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
	@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
	fun updateTotalsItems(items: LinkedList<ITTListItem>) {
		// Total up the times in the forward direction
		val headings = findHeadings(items)
		val topHeadingItemIndex: Int? = headings.first
		val bottomHeadingItemIndex: Int? = headings.second

		if ((topHeadingItemIndex != null) && (bottomHeadingItemIndex != null)) {
			calcTotalTravelTime(topHeadingItemIndex + 1, bottomHeadingItemIndex - 1, items)
			calcTotalTravelTime(bottomHeadingItemIndex + 1, items.size - 1, items)
		}
	}

	/**
	 * Total the [travelTime] property value for all ITTListItems in [items] between [startIndex]
	 * and [endIndex] (both inclusive). Each item in this range is assumed to be a [SimpleTTListItem].
	 * Only items that are [active] and [includedInTotal] contribute towards the total
	 */
	@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
	fun calcTotalTravelTime(startIndex: Int, endIndex: Int, items: LinkedList<ITTListItem>) {
		if ((startIndex < endIndex) && (startIndex >= 0) && (endIndex < items.size)) {
			var total: Int = 0

			for (j in startIndex..endIndex) {
				val item = items[j] as SimpleTTListItem
				val segment = item.travelTime

				if (segment.isTotal) {
					segment.properties?.travelTimeMinutes = total
					break
				} else {
					if ((segment.includedInTotal == true) && (segment.properties?.isActive == true)) {
						total += segment.properties.travelTimeMinutes ?: 0
					}
				}
			}
		}
	}

	/**
	 * @return A Pair in which the first number is the index in [items] of the first
	 * heading, and the second number is the index in [items] of the second heading.
	 * The first or both may be null.
	 */
	@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
	fun findHeadings(items: LinkedList<ITTListItem>) : Pair<Int?, Int?> {
		var topHeadingIndex: Int? = null
		var bottomHeadingIndex: Int? = null

		for (i in items.indices) {
			val item = items[i]

			if (item is HeadingTTListItem) {
				if (topHeadingIndex == null) {
					topHeadingIndex = i
				} else {
					bottomHeadingIndex = i
				}
			}
		}

		return Pair(topHeadingIndex, bottomHeadingIndex)
	}

}