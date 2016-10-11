package rod.bailey.trafficatnsw.json.hazard;

import org.json.JSONObject;

import static rod.bailey.trafficatnsw.util.JSONUtils.*;

public class XPeriod {

	private String closureType;
	private String direction;
	private String finishTime;
	private String fromDay;
	private String startTime;
	private String toDay;

	public XPeriod(JSONObject json) {
		assert json != null;

		closureType = safeGetString(json, "closureType");
		direction = safeGetString(json, "direction");
		finishTime = safeGetString(json, "finishTime");
		fromDay = safeGetString(json, "fromDay");
		startTime = safeGetString(json, "startTime");
		toDay = safeGetString(json, "toDay");
	}

	public String getClosureType() {
		return closureType;
	}

	public String getDirection() {
		return direction;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public String getFromDay() {
		return fromDay;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getToDay() {
		return toDay;
	}

}
