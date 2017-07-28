package rod.bailey.trafficatnsw.hazard.details

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import kotlinx.android.synthetic.main.view_list.view.*
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.R.*
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.ui.view.ListViewAutoHideFooter
import rod.bailey.trafficatnsw.util.MLog

/**
 * Details list has the following sections:
 *
 *  1. Title
 *  2. When
 *  3. General
 *  4. Other Advice
 *
 * @author rodbailey
 */
class HazardDetailsActivity : AppCompatActivity() {
	private lateinit var hazard: XHazard

	private fun createUI() {
		val listView = ListViewAutoHideFooter(this)
		listView.setAdapter(HazardDetailsListAdapter(this, hazard))

		listView.lv_list.divider = resources.getDrawable(R.drawable.line_list_divider_full)
		listView.lv_list.dividerHeight = 2

		setContentView(listView)
		val actionBar = actionBar
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		MLog.i(LOG_TAG, "HazardDetailsActivity is being created ")
		super.onCreate(savedInstanceState)

		overridePendingTransition(anim.slide_in_from_right, R.anim.slide_out_to_left)

		val extras = intent.extras
		val hazardId = extras.getInt(EXTRA_HAZARD_ID_INT)

		MLog.i(LOG_TAG, "Showing details of hazard id " + hazardId)
		hazard = HazardCacheSingleton.instance.getUnfilteredHazard(hazardId)!!
		createUI()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		val inflater = MenuInflater(this)
		inflater.inflate(R.menu.hazard_details_options_menu, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null) {
			if (item.itemId == id.show_on_map) {
				MLog.i(LOG_TAG, "Launching ShowHazardOnMapActivity")
				val intent = Intent(this, ShowHazardOnMapActivity::class.java)
				intent.putExtra("hazardId", hazard.hazardId)
				this.startActivity(intent)
			} else if (item.itemId == android.R.id.home) {
				NavUtils.navigateUpFromSameTask(this)
			}
		}
		return true
	}

	companion object {
		private val LOG_TAG = HazardDetailsActivity::class.java.simpleName
		private val EXTRA_HAZARD_ID_INT: String = "rod.bailey.trafficatnsw.hazard.id"

		fun start(ctx: Context, hazardId: Int) {
			val hazardIntent = Intent(ctx, HazardDetailsActivity::class.java)
			hazardIntent.putExtra(EXTRA_HAZARD_ID_INT, hazardId)
			ctx.startActivity(hazardIntent)
		}
	}
}
