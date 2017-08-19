package rod.bailey.trafficatnsw.cameras.data

/**
 * The direction a traffic camera might be pointing.
 */
enum class XCameraDirection private constructor(val apiToken: String) {
	N("N"), S("S"), E("E"), W("W"),
	NE("N-E"), NW("N-W"), SE("S-E"), SW("S-W");

	companion object {
		fun findDirectionByApiToken(apiToken: String): XCameraDirection? {
			var result: XCameraDirection? = null
			for (element in XCameraDirection.values()) {
				if (element.apiToken == apiToken) {
					result = element
					break
				}
			}
			return result
		}
	}
}