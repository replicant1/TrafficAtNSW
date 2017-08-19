package rod.bailey.trafficatnsw.traveltime.data

/**
 * The direction you might travel along a segment of motorway.
 */
enum class SegmentDirection private constructor(val apiToken: String) {
	N("N"), S("S"), E("E"), W("W");

	companion object {

		fun findDirectionByApiToken(apiToken: String): SegmentDirection? {
			var result: SegmentDirection? = null
			for (element in SegmentDirection.values()) {
				if (element.apiToken == apiToken) {
					result = element
					break
				}
			}
			return result
		}
	}
}
