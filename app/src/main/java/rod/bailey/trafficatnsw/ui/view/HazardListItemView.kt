package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.Typeface.DEFAULT
import android.text.TextUtils.TruncateAt.END
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivty
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.util.DateUtils
import rod.bailey.trafficatnsw.util.DisplayUtils
import rod.bailey.trafficatnsw.util.DisplayUtils.dp2Px
import rod.bailey.trafficatnsw.util.DisplayUtils.getDisplaySizePx
import java.text.SimpleDateFormat
import java.util.*

class HazardListItemView(private val ctx: Context,
						 val hazard: XHazard,
						 private val showLastUpdatedDate: Boolean,
						 clickable: Boolean) : ViewGroup(ctx) {
	private inner class ItemTouchListener : View.OnTouchListener {
		override fun onTouch(v: View, event: MotionEvent): Boolean {
			if (event.action == MotionEvent.ACTION_DOWN) {
				setBackgroundColor(Color.LTGRAY)
			} else if (event.action == MotionEvent.ACTION_UP) {
				setBackgroundColor(Color.WHITE)
				val hazardIntent = Intent(
					this@HazardListItemView.context,
					HazardDetailsActivty::class.java)
				hazardIntent.putExtra("hazardId",
									  this@HazardListItemView.hazard.hazardId)
				this@HazardListItemView.context
					.startActivity(hazardIntent)
			} else if (event.action == MotionEvent.ACTION_CANCEL) {
				setBackgroundColor(Color.WHITE)
			}
			return true
		}
	}

	private val dateLinearLayout: LinearLayout
	private val dateView: TextView
	private val imageView: ImageView
	private val subSubTitleView: TextView
	private val subTitleView: TextView
	private val titleView: TextView

	init {
		// Create child components
		imageView = ImageView(ctx)
		titleView = TextView(ctx)
		subTitleView = TextView(ctx)
		subSubTitleView = TextView(ctx)

		dateLinearLayout = LinearLayout(ctx)
		dateView = TextView(ctx)
		val dateViewLLP = LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
		dateView.layoutParams = dateViewLLP
		dateLinearLayout.setBackgroundColor(Color.TRANSPARENT)
		dateLinearLayout.orientation = LinearLayout.HORIZONTAL
		dateLinearLayout.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
		dateView.setBackgroundColor(Color.GREEN)
		dateLinearLayout.addView(dateView)

		addView(imageView)
		addView(titleView)
		addView(subTitleView)
		addView(subSubTitleView)

		if (showLastUpdatedDate) {
			addView(dateLinearLayout)
		}
		if (!hazard.roads.isEmpty()) {
			val road = hazard.roads[0]
			val resId = if ((hazard.isInitialReport != null) && hazard.isInitialReport)
				R.drawable.incident_initial_report
			else
				R.drawable.incident

			imageView
				.setImageResource(resId)
			titleView.text = road.suburb
			subTitleView.text = road.mainStreet
		}
		subSubTitleView.text = hazard.displayName
		val lu: Date? = hazard.lastUpdated
		if (lu != null) {
			var dateText: String?
			if (DateUtils.isYesterday(lu)) {
				dateText = "Yesterday"
			} else if (DateUtils.isToday(lu)) {
				dateText = SimpleDateFormat("h:mm a").format(lu).toLowerCase()
			} else {
				dateText = SimpleDateFormat("dd/MM/yyyy").format(lu)
			}

			if (dateText != null) {
				dateView.text = dateText
			}
		}

		titleView.setTextColor(BLACK)
		titleView.typeface = Typeface.DEFAULT_BOLD
		titleView.textSize = TITLE_VIEW_TEXT_SIZE_SP.toFloat()
		titleView.setSingleLine()
		titleView.ellipsize = END

		subTitleView.setTextColor(BLUE)
		subTitleView.typeface = DEFAULT
		subTitleView.textSize = SUBTITLE_VIEW_TEXT_SIZE_SP.toFloat()
		subTitleView.setSingleLine()
		subTitleView.ellipsize = END
		subTitleView.setBackgroundColor(TRANSPARENT)

		subSubTitleView.setTextColor(DKGRAY)
		subSubTitleView.typeface = DEFAULT
		subSubTitleView.textSize = SUB_SUB_TITLE_TEXT_SIZE_SP.toFloat()
		subSubTitleView.setSingleLine()
		subSubTitleView.ellipsize = END
		subSubTitleView.setBackgroundColor(TRANSPARENT)

		if (showLastUpdatedDate) {
			dateView.setTextColor(GRAY)
			dateView.typeface = DEFAULT
			dateView.textSize = DATE_VIEW_TEXT_SIZE_SP.toFloat()
			dateView.setSingleLine()
			dateView.setBackgroundColor(TRANSPARENT)
		}

		setBackgroundColor(WHITE)

		if (clickable) {
			setOnTouchListener(ItemTouchListener())
		}
	}

	override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
						  bottom: Int) {
		if (changed) {
			val hpadPx = dp2Px(ctx, 4)
			val vpadPx = dp2Px(ctx, 2)
			val availableWidth = right - left - hpadPx * 2
			val availableHeight = bottom - top - vpadPx * 2
			val imageHeight = dp2Px(ctx, 30)
			val imageWidth = dp2Px(ctx, 30)
			// Layout the icon. Work out the location in local coordinate space
			val iconTop = (availableHeight - imageHeight) / 2
			val iconBottom = iconTop + imageHeight
			imageView.layout(hpadPx, iconTop, imageWidth + hpadPx, iconBottom)
			// How wide will 'dateTExt' be when rendered.
			val dateTextRect = Rect()
			val paint = DisplayUtils.createTextPaintWithTextSize(
				DisplayUtils.sp2Px(ctx,
								   DATE_VIEW_TEXT_SIZE_SP).toFloat())
			paint.getTextBounds(dateView.text.toString(), 0, dateView.text.length, dateTextRect)
			val dateTextWidthPx = ((dateTextRect.right - dateTextRect.left) * 1.10).toInt()
			// Layout the titleView
			val titleLeft = imageWidth + hpadPx * 3
			val titleTop = vpadPx
			val titleRight = hpadPx + availableWidth
			val titleBottom = (availableHeight * 0.4).toInt()
			titleView.layout(titleLeft, titleTop, titleRight - dateTextWidthPx, titleBottom)
			// Layout the subTitleView
			val subTitleLeft = titleLeft
			val subTitleTop = titleBottom
			val subTitleRight = titleRight
			val subTitleBottom = (availableHeight * 0.7).toInt()
			subTitleView.layout(subTitleLeft, subTitleTop, subTitleRight,
								subTitleBottom)
			// Layout the subSubTitleView
			val subSubTitleLeft = titleLeft
			val subSubTitleTop = subTitleBottom
			val subSubTitleRight = titleRight
			val subSubTitleBottom = availableHeight - vpadPx
			subSubTitleView.layout(subSubTitleLeft, subSubTitleTop,
								   subSubTitleRight, subSubTitleBottom)
			// Layout the dateView
			if (showLastUpdatedDate) {
				val dateLeft = titleRight - availableWidth
				val dateRight = titleRight
				val dateTop = titleTop
				val dateBottom = titleBottom
				dateLinearLayout.layout(dateLeft, dateTop, dateRight,
										dateBottom)
			}
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(getDisplaySizePx(ctx).x, dp2Px(ctx, 75))
		val dateWidthSpec = View.MeasureSpec.makeMeasureSpec(dp2Px(ctx, 200),
															 View.MeasureSpec.AT_MOST)
		val dateHeightSpec = View.MeasureSpec.makeMeasureSpec(dp2Px(ctx, 75),
															  View.MeasureSpec.AT_MOST)
		dateLinearLayout.measure(dateWidthSpec, dateHeightSpec)
	}

//	fun setDate(date: Date) {
//		if (showLastUpdatedDate) {
//			dateView.text = "12/03/2014"
//			dateView.invalidate()
//		}
//	}

	fun setSubSubTitle(title: String) {
		subSubTitleView.text = title
		subSubTitleView.invalidate()
	}

	fun setSubTitle(title: String) {
		subTitleView.text = title
		subTitleView.invalidate()
	}

	fun setTitle(title: String) {
		titleView.text = title
		titleView.invalidate()
	}

	companion object {
		private val DATE_VIEW_TEXT_SIZE_SP = 12
		private val SUB_SUB_TITLE_TEXT_SIZE_SP = 12
		private val SUBTITLE_VIEW_TEXT_SIZE_SP = 14
		private val TITLE_VIEW_TEXT_SIZE_SP = 18
		private val TAG = HazardListItemView::class.java.simpleName
	}
}
