package rod.bailey.trafficatnsw.traveltime.overview

import android.content.Context
import android.graphics.Color.BLACK
import android.graphics.Color.LTGRAY
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.common.ui.IRefreshableView
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment

@EViewGroup(R.layout.list_item_travel_time)
open class TravelTimesListItemView(val ctx: Context, var travelTime: XTravelTimeSegment) : FrameLayout(ctx), IRefreshableView {

	private inner class ItemOnClickListener : View.OnClickListener {
		override fun onClick(view: View) {
			// Clicking on a TT row should toggle it's 'includedInTotal' status.
			// But neither TOTAL rows or any row that is currently inactive can
			// be toggled.
			if ((travelTime.properties?.isActive == true) && !travelTime.isTotal) {
				travelTime.includedInTotal = !(travelTime.includedInTotal == true)
				updateAppearancePerExclusionState()
			}
		}
	}

	@ViewById(R.id.ll_item_travel_time)
	@JvmField
	var background: LinearLayout? = null

	@ViewById(R.id.tv_left_col_view)
	@JvmField
	var leftColView: AppCompatTextView? = null

	@ViewById(R.id.tv_right_col_view)
	@JvmField
	var rightColView: AppCompatTextView? = null

	@AfterViews
	fun afterViews() {
		leftColView?.text = "${travelTime.properties?.fromDisplayName} - ${travelTime.properties?.toDisplayName}"
		rightColView?.text = "${travelTime.properties?.travelTimeMinutes} mins"
		updateAppearancePerExclusionState()
		updateAppearancePerIsTotal()
		background?.setOnClickListener(ItemOnClickListener())
	}

	fun updateAppearancePerExclusionState() {
		if (!travelTime.isTotal) {
			if ((travelTime.properties?.isActive == false)|| (travelTime.includedInTotal == false)) {
				leftColView?.setTextColor(LTGRAY)
				rightColView?.setTextColor(LTGRAY)
			} else {
				leftColView?.setTextColor(BLACK)
				rightColView?.setTextColor(BLACK)
			}
		}
		leftColView?.invalidate()
		rightColView?.invalidate()
	}

	fun updateAppearancePerIsTotal() {
		leftColView?.typeface = if (travelTime.isTotal) {
			Typeface.DEFAULT_BOLD
		} else {
			Typeface.DEFAULT
		}

		rightColView?.typeface = if (travelTime.isTotal) {
			Typeface.DEFAULT_BOLD
		} else {
			Typeface.DEFAULT
		}

		leftColView?.invalidate()
		rightColView?.invalidate()
	}

	override fun refresh() {
		afterViews()
	}
}
