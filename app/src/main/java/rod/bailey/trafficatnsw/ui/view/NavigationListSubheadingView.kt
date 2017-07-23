package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import rod.bailey.trafficatnsw.util.DisplayUtils

/**
 * An item in the navigation bar that is not a heading, but can be tapped on
 * Constructs a NavigationListSubheadingView that appears in the nav bar and
 * is just a piece of text
 * @param ctx
 * *            Context
 * *
 * @param text
 * *            Text of the subheading
 */

class NavigationListSubheadingView(ctx: Context, text: String) : AppCompatTextView(ctx) {

	init {
		setBackgroundColor(Color.TRANSPARENT)
		typeface = Typeface.DEFAULT
		// Note that setTextSize accepts an argument in SP, not pixels
		textSize = TEXT_SIZE_SP.toFloat()
		setText(text)
		setPadding(
			DisplayUtils.dp2Px(ctx, 8),
			DisplayUtils.dp2Px(ctx, 5),
			DisplayUtils.dp2Px(ctx, 10),
			DisplayUtils.dp2Px(ctx, 10))
		setTextColor(Color.DKGRAY)
	}

	companion object {
		private val TEXT_SIZE_SP = 14
	}
}