package rod.bailey.trafficatnsw.util

import android.content.Context
import android.graphics.Color
import android.graphics.Paint.Align
import android.graphics.Point
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Looper
import android.text.TextPaint
import android.util.TypedValue
import android.view.Display
import android.view.View.MeasureSpec
import android.view.WindowManager

object DisplayUtils {
	fun viewBackgroundColor(): Int {
		return Color.rgb(239, 239, 244)
	}

	/**
	 * @return Newly created TextPaint ready to draw the street sign text at the
	 * *         given textSize
	 */
	fun createTextPaintWithTextSize(textSizePx: Float): TextPaint {
		val textPaint = TextPaint()
		textPaint.textSize = textSizePx
		textPaint.isAntiAlias = true
		textPaint.color = Color.WHITE
		textPaint.typeface = Typeface.DEFAULT_BOLD
		textPaint.setShadowLayer(2f, 1f, 1f, Color.BLACK)
		textPaint.textAlign = Align.CENTER

		return textPaint
	}

	/**
	 * DP (Density Independent) is the appropriate measurement unit for
	 * dimensions (layout).

	 * @param context
	 * *
	 * @param dp
	 * *
	 * @return
	 */
	fun dp2Px(context: Context, dp: Int): Int {
		val scale = context.resources.displayMetrics.density
		val pixels = (dp.toFloat() * scale + 0.5f).toInt()
		return pixels
	}

	/**

	 * @param text
	 * *            Text to be rendered
	 * *
	 * @param maxTextSizePx
	 * *            Maximum pixel size in which it should be rendered
	 * *
	 * @param maximumWidthPx
	 * *            When rendered, the text should be no more than this wide
	 * *
	 * @return Largest text size that achieves the desired result. Units = PX.
	 */
	fun findLargestTextSize(text: String, maxTextSizePx: Int, maximumWidthPx: Int): Int {
		var textFits = false
		// 200Px
		val textPaint = DisplayUtils
			.createTextPaintWithTextSize(maxTextSizePx.toFloat())
		var scaledDownPx = maxTextSizePx
		// Iteratively scale down the size of the text by 1 sp at a time
		// until it fits into a width of maximumWidthPx
		while (!textFits) {
			textPaint.textSize = scaledDownPx.toFloat()
			val textRect = Rect()
			textPaint.getTextBounds(text, 0, text.length, textRect)
			val textWidthPx = textRect.right - textRect.left

			if (textWidthPx <= maximumWidthPx) {
				textFits = true
			} else {
				scaledDownPx -= 1
			}
		} // while !textFits
		return scaledDownPx
	}

	/**
	 * @param ctx
	 * *            Application context
	 * *
	 * @return Size of the display. If in landscape mode it is W * H, if in
	 * *         portrait mode it is H * W
	 */
	fun getDisplaySizePx(ctx: Context): Point {
		val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val display = wm.defaultDisplay
		val outPoint = Point()
		display.getSize(outPoint)
		return outPoint
	}

//	fun getDisplaySizeDp(ctx: Context): Point {
//		val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//		val density = ctx.resources.displayMetrics.density
//		val display = wm.defaultDisplay
//		val width = (display.width / density).toInt()
//		val height = (display.height / density).toInt()
//		return Point(width, height)
//	}

	/**
	 * @return True if the calling thread is the Android UI thread
	 */
	val isUIThread: Boolean
		get() = Looper.getMainLooper().thread === Thread.currentThread()

	/**
	 * SP (Scale Independent) is the appropriate measurement unit for text
	 * sizes.

	 * @param context
	 * *
	 * @param sp
	 * *
	 * @return
	 */
	fun sp2Px(context: Context, sp: Int): Int {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
										 sp.toFloat(),
										 context.resources.displayMetrics).toInt()
	}

	fun measureSpec2String(measureSpec: Int): String {
		val size = MeasureSpec.getSize(measureSpec)
		val mode = MeasureSpec.getMode(measureSpec)
		var modeStr = "?"

		when (mode) {
			MeasureSpec.AT_MOST -> modeStr = "AT_MOST"
			MeasureSpec.EXACTLY -> modeStr = "EXACTLY"
			MeasureSpec.UNSPECIFIED -> modeStr = "UNSPECIFIED"
		}

		return "$modeStr($size)"
	}
}
