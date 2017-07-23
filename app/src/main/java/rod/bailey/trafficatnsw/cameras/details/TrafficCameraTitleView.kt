package rod.bailey.trafficatnsw.cameras.details

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import rod.bailey.trafficatnsw.util.DisplayUtils
import android.graphics.Color.*
import android.graphics.Typeface.*
import android.text.TextUtils.TruncateAt.END
import android.view.Gravity.*
import android.view.ViewGroup.LayoutParams.*

class TrafficCameraTitleView(ctx: Context, title: String, subTitle: String) : LinearLayout(ctx) {

	init {
		// Title
		val titleTextView = TextView(ctx)
		titleTextView.text = title
		titleTextView.setBackgroundColor(TRANSPARENT)
		titleTextView.typeface = DEFAULT
		titleTextView.textSize = TITLE_TEXT_SIZE_SP.toFloat()
		titleTextView.setTextColor(DKGRAY)
		titleTextView.setSingleLine()
		titleTextView.ellipsize = END
		val titleTextViewLP = LinearLayout.LayoutParams(
			MATCH_PARENT, WRAP_CONTENT)
		titleTextViewLP.gravity = BOTTOM
		// Subtitle
		val subTitleTextView = TextView(ctx)
		subTitleTextView.text = subTitle
		subTitleTextView.setBackgroundColor(TRANSPARENT)
		subTitleTextView.typeface = DEFAULT
		subTitleTextView.textSize = SUB_TITLE_TEXT_SIZE_SP.toFloat()
		subTitleTextView.setTextColor(GRAY)
		subTitleTextView.setSingleLine()
		subTitleTextView.ellipsize = END
		// Title on top, subtitle underneath, padding all around
		val subTitleTextViewLP = LinearLayout.LayoutParams(
			MATCH_PARENT, WRAP_CONTENT)
		subTitleTextViewLP.gravity = TOP

		addView(titleTextView, titleTextViewLP)
		addView(subTitleTextView, subTitleTextViewLP)

		orientation = LinearLayout.VERTICAL
		setPadding(0,
				   DisplayUtils.dp2Px(ctx, 3),
				   DisplayUtils.dp2Px(ctx, 3),
				   DisplayUtils.dp2Px(ctx, 3))
	}

	companion object {
		private val SUB_TITLE_TEXT_SIZE_SP = 12
		private val TITLE_TEXT_SIZE_SP = 14
	}
}
