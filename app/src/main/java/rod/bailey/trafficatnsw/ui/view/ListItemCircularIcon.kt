package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import rod.bailey.trafficatnsw.R

/**
 * Created by rodbailey on 26/7/17.
 */
class ListItemCircularIcon : TextView {

	val circleColor: Int
	val circleLetter: String

	constructor(ctx: Context): super(ctx) {
		circleColor = android.R.color.transparent
		circleLetter = ""
	}

	constructor(ctx: Context, attrs:AttributeSet): super(ctx, attrs) {
		Log.d(LOG_TAG, "Constructing a ListItemCircularIcon with ctx=${ctx} and attrs=${attrs}")
		Log.d(LOG_TAG, "attributeCount=${attrs.attributeCount}")

		val a: TypedArray = ctx.obtainStyledAttributes(attrs, R.styleable.ListItemCircularIcon, 0, 0)
		circleColor = a.getColor(R.styleable.ListItemCircularIcon_circleColor, R.color.palette_rusty_red)
		circleLetter = a.getString(R.styleable.ListItemCircularIcon_circleLetter)
		a.recycle()

		Log.d(LOG_TAG, "circleColor=${circleColor}, circleLetter=${circleLetter}")

		this.text = circleLetter
		this.setTextColor(circleColor)
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}

	companion object {
		private val LOG_TAG: String = ListItemCircularIcon::class.java.simpleName
	}
}