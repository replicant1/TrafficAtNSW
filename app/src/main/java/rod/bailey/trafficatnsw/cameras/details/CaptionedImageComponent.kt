package rod.bailey.trafficatnsw.cameras.details

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import rod.bailey.trafficatnsw.util.DisplayUtils
import rod.bailey.trafficatnsw.util.MLog

class CaptionedImageComponent (ctx: Context,
							   var frameBitmap: Bitmap? = null,
							   var imageBitmap: Bitmap? = null,
							   captionText: String? = null) : ViewGroup(ctx) {
	private val imageView: ImageView
	private val textView: TextView
	private val frameView: ImageView

	init {
		imageView = ImageView(ctx)
		textView = TextView(ctx)
		frameView = ImageView(ctx)

		setCaption(captionText)
		setFrame(frameBitmap)
		setImage(imageBitmap)

		textView.gravity = Gravity.CENTER
		textView.setBackgroundColor(Color.TRANSPARENT)
		textView.textSize = TEXT_SIZE_SP.toFloat()
		textView.setTextColor(Color.WHITE)

		addView(imageView)
		addView(frameView)
		addView(textView)
	}

	fun setCaption(value: String?) {
		textView.text = value ?: ""
		invalidate()
	}

	fun setFrame(value: Bitmap?) {
		this.frameBitmap = value
		invalidate()
	}

	fun setImage(value: Bitmap?) {
		imageBitmap = value
		invalidate()
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
						  bottom: Int) {
		MLog.i(TAG,
			   String.format(
				   "left:%d, top:%d, right:%d, bottom:%d, imageBItmap:%s, frameBitmap:%s",
				   left, top, right, bottom, imageBitmap, frameBitmap))
		val margin = DisplayUtils.dp2Px(context, MARGIN_DP)
		val rawWidth = right - left
		val rawHeight = bottom - top
		val scaledImageWidth = rawWidth - margin * 2
		val scaledImageHeight = (scaledImageWidth.toDouble() / ASPECT_RATIO).toInt()

		if (imageBitmap != null) {
			val scaledBitmap = Bitmap.createScaledBitmap(imageBitmap,
														 scaledImageWidth, scaledImageHeight, false)
			imageView.setImageBitmap(scaledBitmap)
		}

		if (frameBitmap != null) {
			val scaledFrameBitmap = Bitmap.createScaledBitmap(frameBitmap,
															  scaledImageWidth, scaledImageHeight,
															  false)
			frameView.setImageBitmap(scaledFrameBitmap)
		}
		// Work out what the top margin should be so that the combination of
		// camera image
		// and titleTextView appears vertically centered.
		val captionHeightPx = DisplayUtils.sp2Px(context, TEXT_SIZE_SP) * 3
		val excessHeight = rawHeight - scaledImageHeight - captionHeightPx
		val topMargin = excessHeight / 2

		if (imageBitmap != null) {
			imageView.layout(margin, topMargin, margin + scaledImageWidth,
							 topMargin + scaledImageHeight)
		}

		if (frameBitmap != null) {
			frameView.layout(margin, topMargin, margin + scaledImageWidth,
							 topMargin + scaledImageHeight)
		}

		textView.layout(margin, topMargin + scaledImageHeight, margin + scaledImageWidth,
						rawHeight - topMargin)
	}

	companion object {
		private val MARGIN_DP = 10
		private val TEXT_SIZE_SP = 12
		private val TAG = CaptionedImageComponent::class.java
			.simpleName
		private val ASPECT_RATIO = 352.0 / 288.0
	}
}
