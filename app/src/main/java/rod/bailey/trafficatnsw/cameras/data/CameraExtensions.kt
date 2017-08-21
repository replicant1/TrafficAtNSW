package rod.bailey.trafficatnsw.cameras.data

/**
 * @return Given a title of form "FIRST(SECOND)" returns "SECOND"
 */
fun XCameraProperties.deriveSuburb(): String? {
	val titleVal = this.title
	if (titleVal != null) {
		val indexOfOpenBracket: Int = titleVal.indexOfFirst { c: Char ->  c == '('}
		val indexOfCloseBracket: Int = titleVal.indexOfLast { c: Char -> c == ')'}
		if ((indexOfCloseBracket != -1) && (indexOfCloseBracket != -1) && (indexOfOpenBracket < indexOfCloseBracket)) {
			return titleVal.substring(indexOfOpenBracket + 1, indexOfCloseBracket).trim()
		}
	}
	return null
}

/**
 * @return Given a title of form "FIRST(SECOND)" returns "FIRST"
 */
fun XCameraProperties.deriveTitle(): String? {
	val titleVal = this.title
	if (titleVal != null) {
		val indexOfOpenBracket: Int = titleVal.indexOfFirst { c: Char -> c == '(' }
		if (indexOfOpenBracket == -1) {
			return titleVal.trim()
		} else {
			return titleVal.substring(0, indexOfOpenBracket).trim()
		}
	}
	return null
}