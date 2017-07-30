package rod.bailey.trafficatnsw.cameras

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.details.CaptionedImageComponent
import rod.bailey.trafficatnsw.cameras.details.TrafficCameraImageActivity
import rod.bailey.trafficatnsw.util.MLog

/**
 * Created by rodbailey on 30/7/17.
 */
class DownloadImageTask(
	private val ctx: Context?) : AsyncTask<String, Void, Bitmap>() {
	private var dialog: ProgressDialog? = null

	override fun onPreExecute() {
		super.onPreExecute()
//		val frameBitmap = BitmapFactory.decodeResource(resources, R.drawable.photo_frame_filled)

//		mainLayout!!.removeAllViews()
//		captionedImage = CaptionedImageComponent(
//			this@TrafficCameraImageActivity,
//			frameBitmap,
//			null as Bitmap?,
//			cameraDescription)
//		mainLayout!!.addView(captionedImage, captionedImageLLP)

		dialog = ProgressDialog(ctx)
		dialog?.setMessage("Loading image...")
		dialog?.setCancelable(false)
		dialog?.isIndeterminate = true
		dialog?.show()
	}

	override fun doInBackground(vararg urls: String): Bitmap? {
//		MLog.i(TrafficCameraImageActivity.Companion.LOG_TAG, "Up to doInBackground")
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
			// Failed to load camera image - bad connection or just not
			// available on server.
			MLog.i(LOG_TAG, "Failed to load camera image - showing error dialog")
			val builder = AlertDialog.Builder(ctx)
			builder.setTitle("Could not load camera image")
			builder.setMessage("Tap the refresh icon at top right to try again.")
			builder.setPositiveButton("OK", null)
			val dialog = builder.create()
			dialog.show()
		} else {
//			val frameBitmap = BitmapFactory.decodeResource(resources,
//														   R.drawable.photo_frame_with_black_corners)
//			mainLayout!!.removeAllViews()
//			captionedImage = CaptionedImageComponent(
//				this@TrafficCameraImageActivity, frameBitmap, result,
//				cameraDescription)
//			mainLayout!!.addView(captionedImage, captionedImageLLP)
		}
	}

	companion object {
		private val LOG_TAG:String = DownloadImageTask::class.java.simpleName
	}
}