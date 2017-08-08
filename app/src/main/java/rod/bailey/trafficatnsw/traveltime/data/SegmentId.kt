package rod.bailey.trafficatnsw.traveltime.data

/**
 * Symbolically represents the "id" of a XTravelTimeSegment. In string form,
 * a segment id looks like "N7" (7th north-bound segment) or "NTOTAL" (total
 * of all north-bound segments)
 */
class SegmentId : Comparable<SegmentId> {

	val isTotalSegment: Boolean
	val direction: SegmentDirection
	val ordinal: Int

	/**
	 * Creates a "TOTAL" segment id for the given direction
	 *
	 * @param direction That part of the segment id that represents direction
	 */
	constructor(direction: SegmentDirection) {
		isTotalSegment = true
		this.direction = direction
		ordinal = -1
	}

	/**
	 *  Constructs a non-total segment id
	 *  @param direction That part of the segment id that represents direction
	 *  @param ordinal The numeric index of this segment id in the sequence of segments
	 *  linked end-to-end in one direction along a motorway.
	 */
	constructor(direction: SegmentDirection, ordinal: Int) {
		isTotalSegment = false
		this.direction = direction
		this.ordinal = ordinal
	}

	/**
	 * Primary sort order is by direction. Secondary sort order is by
	 * ordinal, with 'TOTAL' appearing at ended
	 * Example sorted list: E1, E2, E3, ETOTAL, W1, W2, W3, WTOTAL
	 * @param o
	 * @return Negative if this segmentId is less than o ; Positive if this
	 * segmentId is greater than 0; Zero if they are equal
	 */
	override fun compareTo(other: SegmentId): Int {
		var result: Int
		val directionCompare = direction.compareTo(other.direction)

		if (directionCompare == 0) {
			if (this.isTotalSegment) {
				if (other.isTotalSegment) {
					result = 0
				} else {
					result = 1
				}
			} else if (other.isTotalSegment) {
				result = -1
			} else {
				result = this.ordinal.compareTo(other.ordinal)
			}
		} else {
			result = directionCompare
		}

		return result
	}

	override fun toString(): String {
		return "direction=${direction}, ordinal=${ordinal}, isTotalSegment=${isTotalSegment}"
	}

	override fun equals(other: Any?): Boolean {
		var result: Boolean = false
		if (other is SegmentId) {
			result = (this.compareTo(other) == 0)
		}
		return result
	}

	override fun hashCode(): Int {
		return 17 + direction.hashCode() + ordinal.hashCode() + isTotalSegment.hashCode();
	}

	companion object {
		fun parse(str: String): SegmentId? {
			var result: SegmentId? = null

			if ((str.length == 2) || (str.length == 6)) {

				if (str.length == 2) {
					val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken(str.first().toString())
					if (dir != null) {
						if (str.last().isDigit()) {
							result = SegmentId(dir, str.last().toString().toInt())
						}
					}
				} else {
					if (str.endsWith("TOTAL")) {
						val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken(str.first().toString())
						if (dir != null) {
							result = SegmentId(dir)
						}
					}
				}
			}

			return result
		}
	}
}
