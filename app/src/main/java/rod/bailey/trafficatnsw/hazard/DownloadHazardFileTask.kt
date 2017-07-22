package rod.bailey.trafficatnsw.hazard

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.json.hazard.Hazard
import rod.bailey.trafficatnsw.ui.ListWithEmptyMessage
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
							 private val listWithEmptyMessage: ListWithEmptyMessage,
							 private val mode: HazardListMode) : AsyncTask<Void, Void, Boolean>() {
	private var dialog: ProgressDialog? = null

	override fun onPreExecute() {
		super.onPreExecute()

		dialog = ProgressDialog(ctx)
		dialog?.setMessage(ctx.getString(R.string.hazards_list_load_progress_msg))
		dialog?.setCancelable(false)
		dialog?.isIndeterminate = true
		dialog?.show()
	}

	override fun doInBackground(vararg params: Void): Boolean {
		// Download the JSON file. The URL is in params[0].
		var bufferedReader: BufferedReader? = null
		var hazardsLoadedOK: Boolean = true

		try {
			if (ConfigSingleton.getInstance().loadIncidentsFromLocalJSONFile()) {
				MLog.d(LOG_TAG, "Loading incidents from local JSON file")
				val assetFileName = ConfigSingleton.getInstance().localIncidentsJSONFile()
				val input = ctx.assets.open(assetFileName)
				val size = input.available()
				val buffer = ByteArray(size)
				input.read(buffer)
				input.close()
				val text = String(buffer)

				MLog.d(LOG_TAG, "text=" + text)

				HazardCacheSingleton.instance.init(text)
				MLog.d(LOG_TAG, "Just passed text into singleton. hazardsLoadedOK")
			} else {
				MLog.d(LOG_TAG, "Loading incidents from remote JSON file")
				val url = URL(ConfigSingleton.getInstance().remoteIncidentsJSONFile())
				val instreamReader = InputStreamReader(
					url.openStream())
				bufferedReader = BufferedReader(instreamReader)
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

				HazardCacheSingleton.instance.init(lineBuffer.toString())
				bufferedReader.close()
			}
		}
		catch (e: Throwable) {
			MLog.w(LOG_TAG, "Failed to load hazards JSON", e)
			hazardsLoadedOK = false
		}
		finally {
			try {
				MLog.d(LOG_TAG, "Pre-close in finally")
				bufferedReader?.close()
				MLog.d(LOG_TAG, "Post-close in finally")
			}
			catch (e: IOException) {
				MLog.w(LOG_TAG, e)
			}
		}

		MLog.d(LOG_TAG, "Returning hazardsLoadedOK=" + hazardsLoadedOK)
		return hazardsLoadedOK
	}

	override fun onPostExecute(result: Boolean) {
		MLog.d(LOG_TAG, "result=" + result)

		// Force the listView to refresh its data
		dialog?.dismiss()

		if (result) {
			listWithEmptyMessage.setAdapter(HazardListAdapter())
		} else {
			// We don't call mainLayout.setAdapter, which means that the old
			// (stale) data will still remain visible under this dialog
			MLog.i(LOG_TAG, "Failed to load hazard JSON - showing error dialog")
			val builder = AlertDialog.Builder(ctx)
			builder.setTitle(ctx.getString(R.string.hazards_list_load_failure_dialog_title))
			builder.setCancelable(true)
			builder.setMessage(ctx.getString(R.string.hazards_list_load_failure_dialog_msg))
			builder.setPositiveButton(
				ctx.getString(R.string.hazards_list_load_failure_dialog_positive_button),
				{ dialog, _->dialog.cancel() })
			builder.create().show()
		}
	}

	companion object {
		private val LOG_TAG = DownloadHazardFileTask::class.java.simpleName
	}
}
