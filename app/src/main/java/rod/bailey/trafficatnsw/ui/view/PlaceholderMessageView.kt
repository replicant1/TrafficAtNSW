package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.graphics.Color.*
import android.graphics.Typeface.*
import android.text.TextUtils.TruncateAt.END
import android.view.Gravity.*

class PlaceholderMessageView(ctx: Context) : AppCompatTextView(ctx) {

	init {
		setTextColor(GRAY)
		typeface = DEFAULT
		//		setBackgroundColor(viewBackgroundColor());
		gravity = CENTER
		textSize = TEXT_SIZE_SP.toFloat()
		isClickable = false
		ellipsize = END
	}

	constructor(ctx: Context, message: String) : this(ctx) {
		setMessage(message)
	}

	fun setMessage(msg: String) {
		text = msg
	}

	companion object {
		private val TEXT_SIZE_SP = 14
	}
}
