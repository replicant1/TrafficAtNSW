package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import rod.bailey.trafficatnsw.R

/**
 * Created by rodbailey on 26/7/17.
 */
class ListItemCircularIcon : TextView {


	var circleColor:Int
		set(@ColorRes value) {
			field = value
			invalidate()
		}

	var circleLetter: String
		set(value) {
			field = value
			this.text = value
			invalidate()
		}

	constructor(ctx: Context) : super(ctx) {
		circleColor = android.R.color.transparent
		circleLetter = ""
	}

	constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
		val a: TypedArray = ctx.obtainStyledAttributes(attrs, R.styleable.ListItemCircularIcon, 0, 0)
		circleColor = a.getColor(R.styleable.ListItemCircularIcon_circleColor, R.color.palette_rusty_red)
		circleLetter = a.getString(R.styleable.ListItemCircularIcon_circleLetter)
		a.recycle()

		Log.d(LOG_TAG, "from c'tor: circleColor=${circleColor}, circleLetter=${circleLetter}")
	}

	override fun onDraw(canvas: Canvas?) {
		if (canvas != null) {
			Log.d(LOG_TAG, "canvas.width=${canvas.width}, canvas.height=${canvas.height}, circleColor=${circleColor}")
			val rect: RectF = RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
			val paint: Paint = Paint()
			paint.color = circleColor
			paint.style = Paint.Style.FILL
			canvas.drawOval(rect, paint)
		}

		// Has to appear *after* the drawing of the circle background so that
		// the text appears on top of the circle.
		super.onDraw(canvas)
	}

	companion object {
		private val LOG_TAG: String = ListItemCircularIcon::class.java.simpleName
	}
}