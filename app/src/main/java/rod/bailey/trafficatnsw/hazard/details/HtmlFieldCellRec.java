package rod.bailey.trafficatnsw.hazard.details;

import android.view.View;

public class HtmlFieldCellRec implements CellRec {
	public final String fieldHtml;
	public final View webView;
	public final int height;
	public HtmlFieldCellRec(String fieldHtml, View webView, int height) {
		this.fieldHtml = fieldHtml;
		this.webView = webView;
		this.height = height;
	}
}
