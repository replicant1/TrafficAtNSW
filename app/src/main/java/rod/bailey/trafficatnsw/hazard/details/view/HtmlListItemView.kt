package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.graphics.Color.WHITE
import android.webkit.WebView
import rod.bailey.trafficatnsw.util.DisplayUtils

class HtmlListItemView(ctx: Context, val html: String) : WebView(ctx) {

	init {
		loadData(html, "text/html", null)
		val p = DisplayUtils.dp2Px(ctx, 10)
		setPadding(p, p, p, p)
		overScrollMode = WebView.OVER_SCROLL_NEVER
		val buf = StringBuffer()
		buf.append(String
					   .format("<style>h1 { font-size: %dpx; } ul { padding-left: %dpx; } </style>",
							   H1_FONT_SIZE_PX,
							   UL_PADDING_LEFT_PX))
		buf.append(String
					   .format(
						   "<body style=\" margin:0; padding:%dpx; background: white; overflow: hidden; \">",
						   BODY_PADDING_PX))
		buf.append(String
					   .format(
						   "<div style=\" font-family: Helvetica; font-size: %dpx; margin:0; padding:0; overflow: hidden; background: white; \">",
						   DIV_FONT_SIZE_PX))

		buf.append(html)
		buf.append("</div>")
		buf.append("</body>")

		loadData(buf.toString(), "text/html", null)
		setBackgroundColor(WHITE)
	}

	companion object {
		private val TAG = HtmlListItemView::class.java.simpleName
		private val DIV_FONT_SIZE_PX = 12
		private val BODY_PADDING_PX = 7
		private val UL_PADDING_LEFT_PX = 15
		private val H1_FONT_SIZE_PX = 12
	}
}
