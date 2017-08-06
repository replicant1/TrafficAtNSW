package rod.bailey.trafficatnsw.hazard.data

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.service.IDataService
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.hazard.ui.HazardListAdapter
import rod.bailey.trafficatnsw.util.MLog
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Inject

/**
 * Async task for downloading the incident.json file from the Live Traffic web site.
 * If  ConfigSingleton.instance.loadIncidentsFromLocalJSONFile is true, the data is
 * loaded from a JSON file in the app's /asset directory instead.
 */
class DownloadHazardsTask(private val ctx: Context,
						  private val hazardList: ListViewWithEmptyMessage) : AsyncTask<Void, Void, Boolean>() {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	private var dialog: ProgressDialog? = null

	@Inject
	lateinit var hazardCacheSingleton: HazardCacheSingleton

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var dataService: IDataService

	/**
	 * Show a "loading incidents..." progress dialog
	 */
	override fun onPreExecute() {
		super.onPreExecute()

		dialog = ProgressDialog(ctx)
		dialog?.setMessage(ctx.getString(R.string.hazards_list_load_progress_msg))
		dialog?.setCancelable(true)
		dialog?.isIndeterminate = true
		dialog?.show()
	}

	override fun doInBackground(vararg params: Void): Boolean {
		var result: Boolean = false
		val hazards: List<XHazard>? = dataService.getHazards()

		if (hazards != null) {
			hazardCacheSingleton.init(hazards)
			result = true
		}

		return result
	}

	override fun onPostExecute(result: Boolean) {
		// Hide the progress dialog
		dialog?.dismiss()

		if (result) {
			hazardList.setAdapter(HazardListAdapter())
		} else {
			// We haven't called mainLayout.setAdapter, which means that the old
			// (stale) data will still remain visible under this error dialog
			MLog.i(LOG_TAG, "Failed to load hazard JSON - showing error dialog")
			val builder = AlertDialog.Builder(ctx)
			builder.setTitle(ctx.getString(R.string.hazards_list_load_failure_dialog_title))
			builder.setCancelable(true)
			builder.setMessage(ctx.getString(R.string.hazards_list_load_failure_dialog_msg))
			builder.setPositiveButton(
				ctx.getString(R.string.hazards_list_load_failure_dialog_positive_button),
				{ dialog, _ -> dialog.cancel() })
			builder.create().show()
		}
	}

	companion object {
		private val LOG_TAG = DownloadHazardsTask::class.java.simpleName
	}
}