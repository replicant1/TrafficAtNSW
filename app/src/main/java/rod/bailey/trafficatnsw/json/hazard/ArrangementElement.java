package rod.bailey.trafficatnsw.json.hazard;

import org.json.JSONObject;

import static rod.bailey.trafficatnsw.util.JSONUtils.*;

public class ArrangementElement {

	private String title;
	private String html;

	public ArrangementElement(JSONObject elementJSONObject) {
		assert elementJSONObject != null;

		title = safeGetString(elementJSONObject, "title");
		html = safeGetString(elementJSONObject, "html");
	}

	public String getTitle() {
		return title;
	}

	public String getHtml() {
		return html;
	}
}
