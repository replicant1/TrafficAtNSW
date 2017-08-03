package rod.bailey.trafficatnsw.cameras.data

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.util.Log
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.image.ITrafficCameraImageDisplayer
import rod.bailey.trafficatnsw.util.MLog

/**
 * Task to download a given traffic camera imageView from the Live Traffic web site.
 */
class DownloadImageTask(
	private val context: Context,
	private val displayer: ITrafficCameraImageDisplayer) : AsyncTask<String, Void, Bitmap>() {

	private var dialog: ProgressDialog? = null

	override fun onPreExecute() {
		super.onPreExecute()
		dialog = ProgressDialog(context)
		dialog?.setMessage(context.getString(R.string.camera_image_loading_msg))
		dialog?.setCancelable(false)
		dialog?.isIndeterminate = true
		dialog?.show()
	}

	override fun doInBackground(vararg urls: String): Bitmap? {
		Log.d(LOG_TAG, "Into doInBackground")
		val urlToLoad = urls[0]
		var result: Bitmap? = null

		try {
			val stream = java.net.URL(urlToLoad).openStream()
			result = BitmapFactory.decodeStream(stream)
		}
		catch (e: Exception) {
			MLog.w(LOG_TAG, e)
		}

		return result
	}

	override fun onPostExecute(result: Bitmap?) {
		dialog?.dismiss()

		if (result == null) {
			MLog.i(LOG_TAG, "Failed to load camera image - showing error dialog")
			val builder = AlertDialog.Builder(context)
			builder.setTitle(context.getString(R.string.camera_image_load_failure_dialog_title))
			builder.setMessage(context.getString(R.string.camera_image_load_failure_dialog_msg))
			builder.setPositiveButton(context.getString(R.string.camera_image_load_failure_dialog_ok), null)
			builder.create().show()
		} else {
			MLog.i(LOG_TAG, "Loaded camera image OK - displaying")
			displayer.displayImage(result)
		}
	}

	companion object {
		private val LOG_TAG: String = DownloadImageTask::class.java.simpleName
	}
}