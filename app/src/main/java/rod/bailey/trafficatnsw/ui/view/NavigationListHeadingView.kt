package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import rod.bailey.trafficatnsw.util.DisplayUtils

/**
 * Constructs a NavigationListHeadingView. A single line heading with an
 * icon on the left, text ontheright and a horizontal underline.

 * @param ctx
 * *            Context
 * *
 * @param text
 * *            Text of heading
 * *
 * @param resId
 * *            Id of the icon
 * *
 * @param addTopPadding
 * *            If true a large gap appears above this heading, otherwise,
 * *            only a small gap appears.
 */
class NavigationListHeadingView(ctx: Context, text: String, resId: Int, addTopPadding: Boolean) : RelativeLayout(ctx) {
	/** Icon to the left of the heading  */
	private val imageView: ImageView
	/** Text to the right of the icon  */
	private val textView: TextView

	init {
		val bothRows = LinearLayout(ctx)
		bothRows.orientation = LinearLayout.VERTICAL
		val topRow = LinearLayout(ctx)
		topRow.gravity = Gravity.CENTER_VERTICAL
		// Icon
		imageView = ImageView(ctx)
		imageView.setImageResource(resId)
		topRow.addView(imageView)
		// Text
		textView = TextView(ctx)
		textView.setBackgroundColor(Color.TRANSPARENT)
		textView.typeface = Typeface.DEFAULT_BOLD
		// Note that text size is already in SP so we don't have to
		// call DisplayUtils.sp2Px
		textView.textSize = TEXT_SIZE_SP.toFloat()
		textView.text = text
		textView.setAllCaps(true)
		textView.setSingleLine()
		textView.setPadding(DisplayUtils.dp2Px(ctx, 4), DisplayUtils.dp2Px(ctx, 12), 0, 4)
		textView.setTextColor(Color.DKGRAY)
		topRow.addView(textView)
		// Divider
		val divider = View(ctx)
		divider.setBackgroundColor(Color.LTGRAY)
		val dividerRLP = RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.MATCH_PARENT, DisplayUtils.dp2Px(ctx, 2))
		divider.layoutParams = dividerRLP

		bothRows.addView(topRow)
		bothRows.addView(divider)

		addView(bothRows)

		setPadding(
			DisplayUtils.dp2Px(ctx, 4),
			if (addTopPadding) DisplayUtils.dp2Px(ctx, 10) else DisplayUtils.dp2Px(ctx, 5),
			DisplayUtils.dp2Px(ctx, 10),
			DisplayUtils.dp2Px(ctx, 10))
	}

	companion object {
		/** Size of text in heading in SP units  */
		private val TEXT_SIZE_SP = 14
	}
}