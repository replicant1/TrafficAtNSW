package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.widget.TextView
import android.graphics.Color.*
import android.graphics.Typeface.*
import android.text.TextUtils.TruncateAt.*
import rod.bailey.trafficatnsw.util.DisplayUtils.*

class LineListItemView(ctx: Context, text: String) : AppCompatTextView(ctx) {

	init {
		setText(text)
		setTextColor(BLACK)
		typeface = DEFAULT
		// Note that setTextSize() takes an argument in SP, not PX
		textSize = TEXT_SIZE_SP.toFloat()
		setSingleLine()
		ellipsize = END
		setBackgroundColor(TRANSPARENT)
		setBackgroundColor(WHITE)
		val p = dp2Px(ctx, 5)
		setPadding(p, p, p, p)
	}

	companion object {
		private val TEXT_SIZE_SP = 12
		private val TAG = LineListItemView::class.java.simpleName
	}
}
