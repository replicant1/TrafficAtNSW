package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color.*
import android.graphics.Typeface.*
import android.text.TextUtils.TruncateAt.END
import android.view.Gravity.*
import android.view.ViewGroup.LayoutParams.*
import rod.bailey.trafficatnsw.util.DisplayUtils

class TextFieldListItemView(private val ctx: Context, fieldName: String,
							fieldValue: String) : LinearLayout(ctx) {
	private val fieldNameTextView: TextView
	private val fieldValueTextView: TextView

	init {
		fieldNameTextView = TextView(ctx)
		fieldValueTextView = TextView(ctx)
		// Field name
		fieldNameTextView.text = fieldName
		fieldNameTextView.setTextColor(BLACK)
		fieldNameTextView.typeface = DEFAULT
		// Note that setTextSize() takes an argument in SP, not PX
		fieldNameTextView.textSize = TEXT_SIZE_SP.toFloat()
		fieldNameTextView.setSingleLine()
		fieldNameTextView.ellipsize = END
		fieldNameTextView.setBackgroundColor(TRANSPARENT)
		val nameLLP = LinearLayout.LayoutParams(
			WRAP_CONTENT, WRAP_CONTENT)
		fieldNameTextView.layoutParams = nameLLP
		// Field value
		fieldValueTextView.text = fieldValue
		fieldValueTextView.setTextColor(DKGRAY)
		fieldValueTextView.typeface = DEFAULT
		// Note that setTextSize() takes an argument in SP, not PX
		fieldValueTextView.textSize = TEXT_SIZE_SP.toFloat()
		fieldValueTextView.setSingleLine()
		fieldValueTextView.ellipsize = END
		fieldValueTextView.setBackgroundColor(TRANSPARENT)
		// Field name aligned left, field value aligned right
		val valueLLP = LinearLayout.LayoutParams(
			WRAP_CONTENT, WRAP_CONTENT)
		valueLLP.weight = 1f
		fieldValueTextView.layoutParams = valueLLP
		fieldValueTextView.gravity = RIGHT

		setBackgroundColor(WHITE)
		val p = DisplayUtils.dp2Px(ctx, 5)
		setPadding(p, p, p, p)
		orientation = LinearLayout.HORIZONTAL

		addView(fieldNameTextView)
		addView(fieldValueTextView)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(DisplayUtils.getDisplaySizePx(ctx).x, DisplayUtils.dp2Px(ctx, 30))
	}

	companion object {
		private val TEXT_SIZE_SP = 12
		private val TAG = TextFieldListItemView::class.java
			.simpleName
	}
}
