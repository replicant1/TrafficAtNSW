package rod.bailey.trafficatnsw.hazard

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.ui.view.ListWithEmptyMessage
import rod.bailey.trafficatnsw.util.ConfigSingleton
import rod.bailey.trafficatnsw.util.MLog
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

/**
 * Async task for downloading the incident.json file from the Live Traffic web site
 */
class DownloadHazardFileTask(private val ctx: Context,
							 private val hazardList: ListWithEmptyMessage) : AsyncTask<Void, Void, Boolean>() {
	private var dialog: ProgressDialog? = null

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

	private fun loadIncidentsFromLocalJSONFile(): String? {
		MLog.d(LOG_TAG, "Loading incidents from local JSON file")
		val assetFileName = ConfigSingleton.instance.localIncidentsJSONFile()
		val input = ctx.assets.open(assetFileName)
		val size = input.available()
		val buffer = ByteArray(size)
		input.read(buffer)
		input.close()
		val text = String(buffer)
		return text
	}

	private fun loadIncidentsFromRemoteJSONFile(): String? {
		var bufferedReader: BufferedReader? = null
		var result: String? = null

		try {
			MLog.d(LOG_TAG, "Loading incidents from remote JSON file")
			val url = URL(ConfigSingleton.instance.remoteIncidentsJSONFile())
			val inStreamReader = InputStreamReader(url.openStream())
			bufferedReader = BufferedReader(inStreamReader)
			var line: String?
			val lineBuffer = StringBuffer()
			var eof: Boolean = false

			while (!eof) {
				line = bufferedReader.readLine()
				if (line == null) {
					eof = true
				} else {
					lineBuffer.append(line)
				}
			}

			bufferedReader.close()
			result = lineBuffer.toString()
		}
		catch (e: Throwable) {
			MLog.w(LOG_TAG, "Failed to load hazards JSON", e)
		}
		finally {
			try {
				bufferedReader?.close()
			}
			catch (e: IOException) {
				MLog.w(LOG_TAG, e)
			}
		}

		return result
	}

	override fun doInBackground(vararg params: Void): Boolean {
		val jsonText: String? =
			if (ConfigSingleton.instance.loadIncidentsFromLocalJSONFile()) {
				loadIncidentsFromLocalJSONFile()
			} else {
				loadIncidentsFromRemoteJSONFile()
			}

		if (jsonText != null) {
			HazardCacheSingleton.instance.init(jsonText)
			return true
		}
		return false
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
		private val LOG_TAG = DownloadHazardFileTask::class.java.simpleName
	}
}
