package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.webkit.WebView
import android.widget.FrameLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R

@EViewGroup(R.layout.list_item_hazard_detail_html)
open class HtmlListItemView(ctx: Context, val html: String) : FrameLayout(ctx) {

	@ViewById(R.id.wv_hazard_detail)
	@JvmField
	var webView: WebView? = null

	@AfterViews
	fun afterViews() {
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

		webView?.loadData(buf.toString(), "text/html", null)
	}

	companion object {
		private const val DIV_FONT_SIZE_PX = 12
		private const val BODY_PADDING_PX = 0
		private const val UL_PADDING_LEFT_PX = 15
		private const val H1_FONT_SIZE_PX = 12
	}
}
