package rod.bailey.trafficatnsw.hazard.details

import android.content.Context
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.AfterExtras
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.R.anim
import rod.bailey.trafficatnsw.R.id
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.ui.view.ListViewAutoHideFooter_
import rod.bailey.trafficatnsw.util.MLog

/**
 * Details list has the following sections:
 *
 *  1. Title
 *  2. When
 *  3. General
 *  4. Other Advice
 */
@EActivity
open class HazardDetailsActivity : AppCompatActivity() {
	private lateinit var hazard: XHazard

	@Extra(HazardDetailsActivity.EXTRA_HAZARD_ID_INT)
	@JvmField
	var hazardId: Int? = null

	@AfterExtras
	fun afterExtras() {
		MLog.i(LOG_TAG, "Showing details of hazard id " + hazardId)
		val tmpHazard: XHazard? = HazardCacheSingleton.instance.getUnfilteredHazard(hazardId ?: 0)

		if (tmpHazard != null) {
			hazard = tmpHazard
			val listView = ListViewAutoHideFooter_.build(this)
			listView.setAdapter(HazardDetailsListAdapter(this, hazard))
			listView.lv_list.divider = ContextCompat.getDrawable(this, R.drawable.line_list_divider_full)
			listView.lv_list.dividerHeight = 2
			setContentView(listView)
		}

		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		overridePendingTransition(anim.slide_in_from_right, R.anim.slide_out_to_left)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		MenuInflater(this).inflate(R.menu.menu_hazard_details_options, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null) {
			if (item.itemId == id.show_on_map) {
				ShowHazardOnMapActivity.start(this, hazard.hazardId)
			} else if (item.itemId == android.R.id.home) {
				NavUtils.navigateUpFromSameTask(this)
			}
		}
		return true
	}

	companion object {
		private val LOG_TAG = HazardDetailsActivity::class.java.simpleName
		private const val EXTRA_HAZARD_ID_INT: String = "rod.bailey.trafficatnsw.hazard.id"

		fun start(ctx: Context, hazardId: Int) {
			HazardDetailsActivity_.intent(ctx).extra(EXTRA_HAZARD_ID_INT, hazardId).start()
		}
	}
}
