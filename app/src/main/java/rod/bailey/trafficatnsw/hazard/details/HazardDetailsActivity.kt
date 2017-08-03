package rod.bailey.trafficatnsw.hazard.details

import android.content.Context
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import org.androidannotations.annotations.*
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.R.anim
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.ui.ListViewAutoHideFooter_
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.util.MLog
import javax.inject.Inject

/**
 * Details list has the following sections:
 *
 *  1. Title
 *  2. When
 *  3. General
 *  4. Other Advice
 */
@EActivity
@OptionsMenu(R.menu.menu_hazard_details_options)
open class HazardDetailsActivity : AppCompatActivity() {
	private lateinit var hazard: XHazard

	@Extra(HazardDetailsActivity.EXTRA_HAZARD_ID_INT)
	@JvmField
	var hazardId: Int? = null

	@Inject
	lateinit var hazardCacheSingleton: HazardCacheSingleton

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@AfterExtras
	fun afterExtras() {
		MLog.i(LOG_TAG, "Showing details of hazard id " + hazardId)
		val tmpHazard: XHazard? = hazardCacheSingleton.getUnfilteredHazard(hazardId ?: 0)

		if (tmpHazard != null) {
			hazard = tmpHazard
			val listView = ListViewAutoHideFooter_.build(this)
			listView.setAdapter(HazardDetailsListAdapter(this, hazard))
			listView.listView?.divider = ContextCompat.getDrawable(this, R.drawable.line_list_divider_full)
			listView.listView?.dividerHeight = 2
			setContentView(listView)
		}

		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		overridePendingTransition(anim.slide_in_from_right, R.anim.slide_out_to_left)
	}

	@OptionsItem(R.id.show_on_map)
	fun menuItemShowOnMap() {
		ShowHazardOnMapActivity.start(this, hazard.hazardId)
	}

	@OptionsItem(android.R.id.home)
	fun menuItemHome() {
		NavUtils.navigateUpFromSameTask(this)
	}

	companion object {
		private val LOG_TAG = HazardDetailsActivity::class.java.simpleName
		private const val EXTRA_HAZARD_ID_INT: String = "rod.bailey.trafficatnsw.hazard.id"

		fun start(ctx: Context, hazardId: Int) {
			HazardDetailsActivity_.intent(ctx).extra(EXTRA_HAZARD_ID_INT, hazardId).start()
		}
	}
}
