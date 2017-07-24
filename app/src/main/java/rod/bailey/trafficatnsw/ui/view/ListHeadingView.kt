package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import rod.bailey.trafficatnsw.util.DisplayUtils
import android.graphics.Color.*
import android.graphics.Typeface.*
import android.view.ViewGroup.LayoutParams.*

class ListHeadingView(ctx: Context, headingText: String?, addDivider: Boolean) : LinearLayout(
	ctx) {

	init {
		orientation = LinearLayout.VERTICAL
		// Text
		val textView = TextView(ctx)
		textView.text = headingText
		textView.setSingleLine()
		textView.setBackgroundColor(TRANSPARENT)
		textView.setTextColor(DKGRAY)
		// Note that the text size is in SP and so we don't need to
		// call DisplayUtils.sp2Px
		textView.textSize = TEXT_SIZE_SP.toFloat()
		textView.setAllCaps(true)
		textView.typeface = DEFAULT
		isFocusable = false
		val textLP = LinearLayout.LayoutParams(
			MATCH_PARENT, WRAP_CONTENT)
		textView.layoutParams = textLP

		addView(textView)

		if (addDivider) {
			// Divider
			val divider = View(ctx)
			divider.setBackgroundColor(LTGRAY)
			val dividerLP = LinearLayout.LayoutParams(
				MATCH_PARENT, DisplayUtils.sp2Px(ctx, 2))
			divider.layoutParams = dividerLP

			addView(divider)
		}
		val smallPx = DisplayUtils.dp2Px(ctx, 5)
		val bigPx = DisplayUtils.dp2Px(ctx, 25)
		setPadding(smallPx, bigPx, smallPx, smallPx)
	}

	companion object {
		private val TEXT_SIZE_SP = 14
	}
}
