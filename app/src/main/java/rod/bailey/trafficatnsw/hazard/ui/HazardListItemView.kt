package rod.bailey.trafficatnsw.hazard.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.ui.ListItemCircularIcon
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivity
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * An ordinary (non-header) list item in a Hazards list. Displays summary of properties
 * of a single hazard.
 *
 * @property ctx Application context
 * @property hazard The hazard displayed in this list item
 * @param showLastUpdatedDate True if date/time of last update appears at top right
 * @param clickable True if you can click on this to start a HazardDetailsActivity
 */
@EViewGroup(R.layout.list_item_hazard)
open class HazardListItemView(val ctx: Context,
							  val hazard: XHazard,
							  val showLastUpdatedDate: Boolean,
							  val clickable: Boolean) : FrameLayout(ctx) {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var config: ConfigSingleton

	@ViewById(R.id.rl_item_hazard)
	@JvmField
	var rl: RelativeLayout? = null

	@ViewById(R.id.tv_hazard_list_item_circle)
	@JvmField
	var circleIcon: ListItemCircularIcon? = null

	@ViewById(R.id.tv_hazard_list_item_line_1)
	@JvmField
	var line1TextView: AppCompatTextView? = null

	@ViewById(R.id.tv_hazard_list_item_line_2)
	@JvmField
	var line2TextView: AppCompatTextView? = null

	@ViewById(R.id.tv_hazard_list_item_line_3)
	@JvmField
	var line3TextView: AppCompatTextView? = null

	@ViewById(R.id.tv_hazard_list_item_date)
	@JvmField
	var dateTextView: AppCompatTextView? = null

	private inner class HazardListItemClickListener : View.OnClickListener {
		override fun onClick(v: View) {
			if (hazard.hazardId != null) {
				HazardDetailsActivity.start(ctx, hazard.hazardId)
			}
		}
	}

	@AfterViews
	fun afterViews() {
		if (!hazard.roads.isEmpty()) {
			val road = hazard.roads[0]

			line1TextView?.text = road.suburb
			line2TextView?.text = road.mainStreet

			val trimmedSuburb: String = road.suburb?.trim() ?: ""
			val letter: Char = if (trimmedSuburb.isEmpty()) ' ' else trimmedSuburb[0]

			circleIcon?.circleLetter = letter.toString()
			circleIcon?.circleColor = ContextCompat.getColor(
				ctx, LetterColorMapSingleton.instance.getColorForLetter(letter))
		}

		line3TextView?.text = hazard.displayName
		dateTextView?.visibility = if (showLastUpdatedDate) View.VISIBLE else View.GONE

		if (hazard.lastUpdated != null) {
			val dateText: String = when {
				DateUtils.isYesterday(hazard.lastUpdated) -> ctx.getString(R.string.hazard_list_item_date_previous)
				DateUtils.isToday(hazard.lastUpdated) -> SimpleDateFormat(
					config.hazardTimeFormat(), Locale.ENGLISH).format(
					hazard.lastUpdated).toLowerCase()
				else -> SimpleDateFormat(config.hazardDateFormat(), Locale.ENGLISH).format(
					hazard.lastUpdated)
			}

			dateTextView?.text = dateText
		}

		if (clickable) {
			rl?.setOnClickListener(HazardListItemClickListener())
		}
	}
}
