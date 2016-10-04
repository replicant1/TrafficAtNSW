package rod.bailey.trafficatnsw.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Looper;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Display;
import android.view.View.MeasureSpec;
import android.view.WindowManager;

public abstract class DisplayUtils {

	public static int viewBackgroundColor() {
		return Color.rgb(239, 239, 244);
	}

	/**
	 * @return Newly created TextPaint ready to draw the street sign text at the
	 *         given textSize
	 */
	public static TextPaint createTextPaintWithTextSize(float textSizePx) {
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(textSizePx);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.WHITE);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setShadowLayer(2F, 1F, 1F, Color.BLACK);
		textPaint.setTextAlign(Align.CENTER);

		return textPaint;
	}

	/**
	 * DP (Density Independent) is the appropriate measurement unit for
	 * dimensions (layout).
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2Px(Context context, int dp) {
		assert context != null;
		float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (((float) dp) * scale + 0.5F);
		return pixels;
	}

	/**
	 * 
	 * @param text
	 *            Text to be rendered
	 * @param maxTextSizePx
	 *            Maximum pixel size in which it should be rendered
	 * @param maximumWidthPx
	 *            When rendered, the text should be no more than this wide
	 * @return Largest text size that achieves the desired result. Units = PX.
	 */
	public static int findLargestTextSize(String text, int maxTextSizePx,
			int maximumWidthPx) {
		boolean textFits = false;
		// 200Px
		TextPaint textPaint = DisplayUtils
				.createTextPaintWithTextSize(maxTextSizePx);
		int scaledDownPx = maxTextSizePx;

		// Iteratively scale down the size of the text by 1 sp at a time
		// until it fits into a width of maximumWidthPx
		while (!textFits) {
			textPaint.setTextSize(scaledDownPx);
			Rect textRect = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), textRect);
			int textWidthPx = textRect.right - textRect.left;

			if (textWidthPx <= maximumWidthPx) {
				textFits = true;
			} else {
				scaledDownPx -= 1;
			}
		} // while !textFits

		return scaledDownPx;
	}

	/**
	 * @param ctx
	 *            Application context
	 * @return Size of the display. If in landscape mode it is W * H, if in
	 *         portrait mode it is H * W
	 */
	public static Point getDisplaySizePx(Context ctx) {
		assert ctx != null;
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		return new Point(width, height);
	}

	public static Point getDisplaySizeDp(Context ctx) {
		assert ctx != null;
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		float density = ctx.getResources().getDisplayMetrics().density;
		Display display = wm.getDefaultDisplay();
		int width = (int) (display.getWidth() / density);
		int height = (int) (display.getHeight() / density);
		return new Point(width, height);
	}

	/**
	 * @return True if the calling thread is the Android UI thread
	 */
	public static boolean isUIThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	/**
	 * SP (Scale Independent) is the appropriate measurement unit for text
	 * sizes.
	 * 
	 * @param context
	 * @param sp
	 * @return
	 */
	public static int sp2Px(Context context, int sp) {
		assert context != null;
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				context.getResources().getDisplayMetrics());
	}

	public static String measureSpec2String(int measureSpec) {
		int size = MeasureSpec.getSize(measureSpec);
		int mode = MeasureSpec.getMode(measureSpec);
		String modeStr = "?";

		switch (mode) {
		case MeasureSpec.AT_MOST:
			modeStr = "AT_MOST";
			break;
		case MeasureSpec.EXACTLY:
			modeStr = "EXACTLY";
			break;
		case MeasureSpec.UNSPECIFIED:
			modeStr = "UNSPECIFIED";
			break;
		}

		return modeStr + "(" + size + ")";
	}
}
