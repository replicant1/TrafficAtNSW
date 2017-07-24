package rod.bailey.trafficatnsw.traveltime

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import rod.bailey.trafficatnsw.traveltime.common.TravelTime
import rod.bailey.trafficatnsw.util.DisplayUtils
import rod.bailey.trafficatnsw.util.MLog
import android.graphics.Color.*
import android.graphics.Typeface.*
import android.view.Gravity.*
import android.view.ViewGroup.LayoutParams.*

class TravelTimesListItemView(private val ctx: Context, travelTime: TravelTime) : LinearLayout(
	ctx) {
	private inner class ItemTouchListener : View.OnTouchListener {
		override fun onTouch(view: View, event: MotionEvent): Boolean {
			MLog.i(TAG,
				   "onTouch event for " + travelTime!!.fromDisplayName + " - " + travelTime!!.toDisplayName + ":" + event)

			if (event.action == MotionEvent.ACTION_DOWN) {
				setBackgroundColor(Color.LTGRAY)
			} else if (event.action == MotionEvent.ACTION_UP) {
				setBackgroundColor(Color.WHITE)
				val ttView = view as TravelTimesListItemView
				val travelTime:TravelTime? = ttView.travelTime
				// Clicking on a TT row should toggle it's 'includedInTotal' status.
				// But neither TOTAL rows or any row that is currently inactive can
				// be toggled.
				if (travelTime != null) {
					if (travelTime.isActive && !travelTime.isTotal) {
						travelTime.isIncludedInTotal = !travelTime.isIncludedInTotal
						updateAppearancePerExclusionState()
					}
				}
			} else if (event.action == MotionEvent.ACTION_CANCEL) {
				setBackgroundColor(Color.WHITE)
			}

			return true
		}
	}

	private val leftColView: TextView
	private val rightColView: TextView
	var travelTime: TravelTime? = null
		private set

	init {
		this.travelTime = travelTime
		leftColView = TextView(ctx)
		rightColView = TextView(ctx)

		addView(leftColView)
		addView(rightColView)
		val m = DisplayUtils.dp2Px(ctx, 5)
		// Left column contains a string of form "<from> - <to>"
		leftColView.setSingleLine()
		leftColView.setBackgroundColor(TRANSPARENT)
		leftColView.textSize = TEXT_SIZE_SP.toFloat()
		leftColView.invalidate()
		leftColView.setPadding(m, m, 0, m)
		val leftLLP = LinearLayout.LayoutParams(
			WRAP_CONTENT, WRAP_CONTENT)
		leftColView.layoutParams = leftLLP
		// Right column contains a string of form "<x> mins"
		rightColView.setSingleLine()
		rightColView.setBackgroundColor(TRANSPARENT)
		rightColView.textSize = TEXT_SIZE_SP.toFloat()
		rightColView.invalidate()
		rightColView.setPadding(0, m, m, m)
		val rightLLP = LinearLayout.LayoutParams(
			WRAP_CONTENT, WRAP_CONTENT)
		rightLLP.weight = 1f
		rightLLP.gravity = RIGHT
		rightColView.layoutParams = rightLLP
		rightColView.gravity = RIGHT

		setBackgroundColor(Color.WHITE)
		setOnTouchListener(ItemTouchListener())
		convert(travelTime)
		orientation = LinearLayout.HORIZONTAL
		updateAppearancePerExclusionState()
		// TOTAL rows are not enabled - all other rows ARE enabled
		isEnabled = !travelTime.isTotal
	}

	private fun convert(travelTime: TravelTime) {
		this.travelTime = travelTime
		if (travelTime.isTotal) {
			leftColView.typeface = DEFAULT_BOLD
			leftColView.text = "Total"
		} else {
			leftColView.typeface = DEFAULT
			leftColView.text = String.format("%s - %s",
											 travelTime.fromDisplayName,
											 travelTime.toDisplayName)
		}
		leftColView.invalidate()

		if (travelTime.isTotal) {
			rightColView.typeface = DEFAULT_BOLD
		} else {
			rightColView.typeface = DEFAULT
		}

		rightColView.text = String.format("%d mins",
										  travelTime.getTravelTimeMinutes())
		rightColView.invalidate()
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		setMeasuredDimension(DisplayUtils.getDisplaySizePx(ctx).x,
							 DisplayUtils.dp2Px(ctx, 35))
	}

	fun updateAppearancePerExclusionState() {
		if (!travelTime!!.isTotal) {
			if (!travelTime!!.isActive || !travelTime!!.isIncludedInTotal) {
				leftColView.setTextColor(LTGRAY)
				rightColView.setTextColor(LTGRAY)
			} else {
				leftColView.setTextColor(BLACK)
				rightColView.setTextColor(BLACK)
			}
		}
		leftColView.invalidate()
		rightColView.invalidate()
	}

	companion object {
		private val TEXT_SIZE_SP = 14
		private val TAG = TravelTimesListItemView::class.java
			.simpleName
	}
}
