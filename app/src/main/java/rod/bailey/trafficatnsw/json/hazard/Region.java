package rod.bailey.trafficatnsw.json.hazard;

public enum Region {
	SYD_MET("Inner Sydney"), // hello
	SYD_NORTH("Sydney North"), //
	SYD_SOUTH("Sydney South"), //
	SYD_WEST("Sydney West"), //
	REG_NORTH("Regional North"), //
	REG_SOUTH("Regional South"), //
	REG_WEST("Regional West");

	private final String description;

	Region(String description) {
		assert description != null;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public boolean isRegional() {
		return (this == REG_NORTH)|| (this == REG_SOUTH) || (this == REG_WEST);
	}
	
	public boolean isSydney() {
		return (this == SYD_SOUTH) || (this == SYD_MET) || (this == SYD_NORTH) || (this == SYD_WEST);
	}
}
