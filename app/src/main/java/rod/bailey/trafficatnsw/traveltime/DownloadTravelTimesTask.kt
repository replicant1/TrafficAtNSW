package rod.bailey.trafficatnsw.traveltime

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.traveltime.common.TravelTimesCacheSingleton
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.util.ConfigSingleton
import rod.bailey.trafficatnsw.util.MLog
import javax.inject.Inject

class DownloadTravelTimesTask(
	private val ctx: Context,
	private val ttFrag: TravelTimesFragment,
	private val travelTimeConfig: TravelTimeConfig?,
	private val mainLayout: ListViewWithEmptyMessage?) : AsyncTask<Void, Void, Boolean>() {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var travelTimesCache: TravelTimesCacheSingleton

	private var dialog: ProgressDialog? = null

	override fun onPreExecute() {
		super.onPreExecute()
		dialog = ProgressDialog(ctx)
		dialog?.setMessage(ctx.getString(R.string.tt_load_progress_msg, travelTimeConfig?.motorwayName))
		dialog?.setCancelable(false)
		dialog?.isIndeterminate = true
		dialog?.show()
	}

	override fun doInBackground(vararg params: Void): Boolean {
		var travelTimesLoadedOK: Boolean = java.lang.Boolean.TRUE
		ttFrag.db?.removePropertyChangeListener(ttFrag)

		if (ConfigSingleton.instance
			.loadTravelTimesFromLocalJSONFiles()) {
			ttFrag.db = travelTimesCache.loadTravelTimesFromLocalJSONFile(ctx, travelTimeConfig!!)
		} else {
			ttFrag.db = travelTimesCache.loadTravelTimesFromRemoteJSONFile(ctx, travelTimeConfig!!)
		}

		if (ttFrag.db == null) {
			travelTimesLoadedOK = java.lang.Boolean.FALSE
		} else {
			ttFrag.db?.addPropertyChangeListener(ttFrag)
		}

		return travelTimesLoadedOK
	}

	override fun onPostExecute(result: Boolean) {
		dialog?.dismiss()

		if (result) {
			mainLayout?.setAdapter(TravelTimesListAdapter(ttFrag.db))
		} else {
			// We don't all mainLayout.setAdapter, which means that the old (stale)
			// data will still remain visible.
			MLog.i(LOG_TAG, "Failed to load " + travelTimeConfig!!.motorwayName + " travel times")
			val builder = AlertDialog.Builder(ctx)
			builder.setTitle(ctx.getString(R.string.tt_download_dialog_title))
			builder.setMessage(ctx.getString(R.string.tt_download_dialog_msg))
			builder.setPositiveButton(ctx.getString(R.string.positive_dialog_button_alt), null)
			val dialog = builder.create()
			dialog.show()
		}
	}

	companion object {
		private val LOG_TAG: String = DownloadTravelTimesTask::class.java.simpleName
	}

}