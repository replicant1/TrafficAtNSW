package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import rod.bailey.trafficatnsw.R

class HtmlListItemView(ctx: Context, val html: String) : FrameLayout(ctx) {

	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.list_item_hazard_detail_html, this)
		val webView: WebView = content.findViewById(R.id.wv_hazard_detail) as WebView

		val buf = StringBuffer()
		buf.append(String
					   .format("<style>h1 { font-size: %dpx; } ul { padding-left: %dpx; } </style>",
							   H1_FONT_SIZE_PX,
							   UL_PADDING_LEFT_PX))
		buf.append(String
					   .format(
						   "<body style=\" margin:0; padding:%dpx; overflow: hidden; \">",
						   BODY_PADDING_PX))
		buf.append(String
					   .format(
						   "<div style=\" font-family: Helvetica; font-size: %dpx; margin:0; padding:0; overflow: hidden; \">",
						   DIV_FONT_SIZE_PX))

		buf.append(html)
		buf.append("</div>")
		buf.append("</body>")

		webView.loadData(buf.toString(), "text/html", null)
	}

	companion object {
		private val TAG = HtmlListItemView::class.java.simpleName
		private val DIV_FONT_SIZE_PX = 12
		private val BODY_PADDING_PX = 0
		private val UL_PADDING_LEFT_PX = 15
		private val H1_FONT_SIZE_PX = 12
	}
}
