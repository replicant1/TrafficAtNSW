package rod.bailey.trafficatnsw.json.hazard

enum class XRegion private constructor(val description: String?) {
	SYD_MET("Inner Sydney"), // hello
	SYD_NORTH("Sydney North"), //
	SYD_SOUTH("Sydney South"), //
	SYD_WEST("Sydney West"), //
	REG_NORTH("Regional North"), //
	REG_SOUTH("Regional South"), //
	REG_WEST("Regional West");

	val isRegional: Boolean
		get() = this == REG_NORTH || this == REG_SOUTH || this == REG_WEST

	val isSydney: Boolean
		get() = this == SYD_SOUTH || this == SYD_MET || this == SYD_NORTH || this == SYD_WEST
}
