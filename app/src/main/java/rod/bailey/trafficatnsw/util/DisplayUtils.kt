package rod.bailey.trafficatnsw.util

import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager

object DisplayUtils {
	/**
	 * DP (Density Independent) is the appropriate measurement unit for
	 * dimensions (layout).
	 * @param context
	 * @param dp
	 * @return
	 */
	fun dp2Px(context: Context, dp: Int): Int {
		val scale = context.resources.displayMetrics.density
		val pixels = (dp.toFloat() * scale + 0.5f).toInt()
		return pixels
	}

	/**
	 * @param ctx
	 * @return Size of the display. If in landscape mode it is W * H, if in
	 *         portrait mode it is H * W
	 */
	fun getDisplaySizePx(ctx: Context): Point {
		val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val display = wm.defaultDisplay
		val outPoint = Point()
		display.getSize(outPoint)
		return outPoint
	}

	/**
	 * SP (Scale Independent) is the appropriate measurement unit for text
	 * sizes.
	 * @param context
	 * @param sp
	 * @return
	 */
	fun sp2Px(context: Context, sp: Int): Int {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
										 sp.toFloat(),
										 context.resources.displayMetrics).toInt()
	}
}
