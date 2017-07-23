package rod.bailey.trafficatnsw.cameras

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.details.TrafficCameraImageActivity
import rod.bailey.trafficatnsw.util.DisplayUtils
import android.graphics.Color.*

class TrafficCameraListItemView(private val ctx: Context, private val camera: TrafficCamera,
								private var favourite: Boolean) : ViewGroup(ctx) {
	private inner class ItemTouchListener : View.OnTouchListener {
		override fun onTouch(v: View, event: MotionEvent): Boolean {
			if (event.action == MotionEvent.ACTION_DOWN) {
				setBackgroundColor(Color.LTGRAY)
			} else if (event.action == MotionEvent.ACTION_UP) {
				setBackgroundColor(Color.WHITE)
				val imageIntent = Intent(ctx,
										 TrafficCameraImageActivity::class.java)
				imageIntent
					.putExtra("index", camera.index.toString())
				imageIntent.putExtra("street", camera.street)
				imageIntent.putExtra("suburb", camera.suburb)
				imageIntent.putExtra("description", camera.description)
				imageIntent.putExtra("url", camera.url)
				imageIntent.putExtra("favourite",
									 camera.isFavourite.toString())
				// Change to startActivityForResult
				ctx.startActivity(imageIntent)
			} else if (event.action == MotionEvent.ACTION_CANCEL) {
				setBackgroundColor(Color.WHITE)
			}
			return true
		}
	}

	private val imageView: ImageView
	private val subtitleTextView: TextView
	private val titleTextView: TextView

	init {
		// create child components
		imageView = ImageView(ctx)
		titleTextView = TextView(ctx)
		subtitleTextView = TextView(ctx)

		addView(imageView)
		addView(titleTextView)
		addView(subtitleTextView)

		titleTextView.text = camera.street
		titleTextView.setSingleLine()
		titleTextView.setBackgroundColor(TRANSPARENT)
		titleTextView.textSize = TITLE_TEXT_SIZE_SP.toFloat()
		titleTextView.setTextColor(BLACK)
		titleTextView.invalidate()

		subtitleTextView.setSingleLine()
		subtitleTextView.setBackgroundColor(TRANSPARENT)
		subtitleTextView.invalidate()
		subtitleTextView.text = camera.suburb
		subtitleTextView.setTextColor(DKGRAY)
		subtitleTextView.textSize = SUB_TITLE_TEXT_SIZE_SP.toFloat()

		setFavourite(favourite)
		imageView.invalidate()

		setBackgroundColor(WHITE)
		setOnTouchListener(ItemTouchListener())
	}

	/**
	 * Assign a size and position to each child view. Call layout on each child.
	 * all children within this layout.
	 */
	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
						  bottom: Int) {
		if (changed) {
			val hpadPx = DisplayUtils.dp2Px(ctx, 6)
			val availableWidth = right - left
			val availableHeight = bottom - top
			val imageHeight = DisplayUtils.dp2Px(ctx, 30)
			val imageWidth = DisplayUtils.dp2Px(ctx, 30)
			// Layout the icon. Work out the location in our coordinate space.
			val iconTop = (availableHeight - imageHeight) / 2
			val iconBottom = iconTop + imageHeight
			imageView.layout(hpadPx, iconTop, imageWidth, iconBottom)
			// Layout the TextView containing the title
			val titleLeft = 2 * hpadPx + imageWidth
			val titleBottom = availableHeight / 2
			titleTextView.layout(titleLeft, 0, availableWidth, titleBottom)
			// Layout the TextView containing the subtitle
			val subtitleLeft = titleLeft
			subtitleTextView.layout(subtitleLeft, titleBottom, availableWidth,
									availableHeight)
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(DisplayUtils.getDisplaySizePx(ctx).x,
							 DisplayUtils.dp2Px(ctx, 50))
	}

	fun setFavourite(favourite: Boolean) {
		this.favourite = favourite
		imageView.setImageResource(if (favourite)
									   R.drawable.ic_action_important
								   else
									   R.drawable.ic_action_not_important)
		invalidate()
	}

	companion object {
		private val SUB_TITLE_TEXT_SIZE_SP = 14
		private val TITLE_TEXT_SIZE_SP = 16
		private val TAG = TrafficCameraListItemView::class.java
			.simpleName
	}
}
