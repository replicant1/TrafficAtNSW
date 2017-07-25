package rod.bailey.trafficatnsw.hazard.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.ui.view.FooterCancellingListView
import rod.bailey.trafficatnsw.util.MLog

/**
 * List has the following sections:
 *
 *  1. Title
 *  1. WHEN
 *  1. GENERAL
 *  1. OTHER ADVICE
 *

 * @author rodbailey
 */
class HazardDetailsActivty : Activity() {
	private lateinit var hazard: XHazard

	private fun createUI() {
		val listView = FooterCancellingListView(this)
		listView.adapter = HazardDetailsListAdapter(hazard)
		listView.choiceMode = ListView.CHOICE_MODE_SINGLE
		listView.isEnabled = true

		setContentView(listView)
		val actionBar = actionBar
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		MLog.i(LOG_TAG, "HazardDetailsActivity is being created ")
		super.onCreate(savedInstanceState)

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
			if (item.itemId == R.id.show_on_map) {
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
		private val LOG_TAG = HazardDetailsActivty::class.java.simpleName
		private val EXTRA_HAZARD_ID_INT: String = "rod.bailey.trafficatnsw.hazard.id"

		fun start(ctx: Context, hazardId: Int) {
			val hazardIntent = Intent(ctx, HazardDetailsActivty::class.java)
			hazardIntent.putExtra(EXTRA_HAZARD_ID_INT, hazardId)
			ctx.startActivity(hazardIntent)
		}
	}
}
