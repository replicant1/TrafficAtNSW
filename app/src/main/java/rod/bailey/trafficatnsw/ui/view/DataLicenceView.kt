package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.graphics.Color.GRAY
import android.graphics.Color.TRANSPARENT
import android.support.v7.widget.AppCompatTextView
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.util.DisplayUtils

class DataLicenceView(ctx: Context) : AppCompatTextView(ctx) {

	init {
		text = ctx.getString(R.string.data_licensing_msg)
		setBackgroundColor(TRANSPARENT)
		setTextColor(GRAY)
		// Note that setTextSize takes an argument in SP not PX
		textSize = TEXT_SIZE_SP.toFloat()
		val smallPx = DisplayUtils.dp2Px(ctx, 5)
		val bigPx = DisplayUtils.dp2Px(ctx, 10)
		setPadding(smallPx, bigPx, smallPx, bigPx)
	}

	companion object {
		private val TEXT_SIZE_SP = 10
	}
}
