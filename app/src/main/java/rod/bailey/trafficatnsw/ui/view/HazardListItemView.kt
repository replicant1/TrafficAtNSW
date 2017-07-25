package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivty
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.util.ConfigSingleton
import rod.bailey.trafficatnsw.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * An ordinary (non-header) list item in a Hazards list. Displays summary of properties
 * of a single hazard.
 *
 * @property ctx Application context
 * @property hazard The hazard displayed in this list item
 * @param showLastUpdatedDate True if date/time of last update appears at top right
 * @param clickable True if you can click on this to start a HazardDetailsActivty
 */
class HazardListItemView(val ctx: Context,
						 val hazard: XHazard,
						 showLastUpdatedDate: Boolean,
						 clickable: Boolean) : FrameLayout(ctx) {

	private inner class HazardListItemClickListener : View.OnClickListener {
		override fun onClick(v: View) {
			if (hazard.hazardId != null) {
				HazardDetailsActivty.start(ctx, hazard.hazardId)
			}
		}
	}

	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.list_item_hazard, this)
		val rl: RelativeLayout = content.findViewById(R.id.rl_item_hazard) as RelativeLayout
		val imageView: ImageView = content.findViewById(R.id.iv_hazard_list_item) as ImageView
		val line1TextView: AppCompatTextView = content.findViewById(
			R.id.tv_hazard_list_item_line_1) as AppCompatTextView
		val line2TextView: AppCompatTextView = content.findViewById(
			R.id.tv_hazard_list_item_line_2) as AppCompatTextView
		val line3TextView: AppCompatTextView = content.findViewById(
			R.id.tv_hazard_list_item_line_3) as AppCompatTextView
		val dateTextView: AppCompatTextView = content.findViewById(R.id.tv_hazard_list_item_date) as AppCompatTextView

		if (!hazard.roads.isEmpty()) {
			val road = hazard.roads[0]
			val resId = if ((hazard.isInitialReport != null) && hazard.isInitialReport)
				R.drawable.incident_initial_report
			else
				R.drawable.incident

			imageView.setImageResource(resId)
			line1TextView.text = road.suburb
			line2TextView.text = road.mainStreet
		}

		line3TextView.text = hazard.displayName
		dateTextView.visibility = if (showLastUpdatedDate) View.VISIBLE else View.GONE

		if (hazard.lastUpdated != null) {
			var dateText: String = when {
				DateUtils.isYesterday(hazard.lastUpdated) -> ctx.getString(R.string.hazard_list_item_date_previous)
				DateUtils.isToday(hazard.lastUpdated) -> SimpleDateFormat(
					ConfigSingleton.instance.hazardTimeFormat(), Locale.ENGLISH).format(
					hazard.lastUpdated).toLowerCase()
				else -> SimpleDateFormat(ConfigSingleton.instance.hazardDateFormat(), Locale.ENGLISH).format(
					hazard.lastUpdated)
			}

			dateTextView.text = dateText
		}

		if (clickable) {
			rl.setOnClickListener(HazardListItemClickListener())
		}
	}

	companion object {
		private val LOG_TAG = HazardListItemView::class.java.simpleName
	}
}
