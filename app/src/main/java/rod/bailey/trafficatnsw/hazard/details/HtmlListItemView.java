package rod.bailey.trafficatnsw.hazard.details;

import android.content.Context;
import android.webkit.WebView;

import rod.bailey.trafficatnsw.util.DisplayUtils;

import static android.graphics.Color.*;

public class HtmlListItemView extends WebView {
	
	@SuppressWarnings("unused")
	private static final String TAG = HtmlListItemView.class.getSimpleName();

	private static final int DIV_FONT_SIZE_PX = 12;
	private static final int BODY_PADDING_PX = 7;
	private static final int UL_PADDING_LEFT_PX = 15;
	private static final int H1_FONT_SIZE_PX = 12;
	
	private final String html;

	public HtmlListItemView(Context ctx, String html) {
		super(ctx);
		this.html = html;
		
		loadData(html, "text/html", null);

		int p = DisplayUtils.dp2Px(ctx, 10);
		setPadding(p, p, p, p);
		setOverScrollMode(WebView.OVER_SCROLL_NEVER);

		StringBuffer buf = new StringBuffer();
		buf.append(String
				.format("<style>h1 { font-size: %dpx; } ul { padding-left: %dpx; } </style>",
						H1_FONT_SIZE_PX,
						UL_PADDING_LEFT_PX));
		buf.append(String
				.format("<body style=\" margin:0; padding:%dpx; background: white; overflow: hidden; \">",
						BODY_PADDING_PX));
		buf.append(String
				.format("<div style=\" font-family: Helvetica; font-size: %dpx; margin:0; padding:0; overflow: hidden; background: white; \">",
						DIV_FONT_SIZE_PX));

		buf.append(html);
		buf.append("</div>");
		buf.append("</body>");

		loadData(buf.toString(), "text/html", null);
		setBackgroundColor(WHITE);
	}

	public String getHtml() {
		return html;
	}
}
