package rod.bailey.trafficatnsw.cameras.details

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeResource
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.RelativeLayout.LayoutParams
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.FavouriteCameraDialogPresenter
import rod.bailey.trafficatnsw.cameras.TrafficCamera
import rod.bailey.trafficatnsw.cameras.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.util.MLog

class TrafficCameraImageActivity : Activity() {
	private var cameraIndex: Int = 0
	private var cameraStreet: String? = null
	private var cameraSuburb: String? = null
	private var cameraDescription: String? = null
	private var cameraUrl: String? = null

	private inner class DownloadImageTask(
		private val ctx: Context?) : AsyncTask<String, Void, Bitmap>() {
		private var dialog: ProgressDialog? = null

		override fun onPreExecute() {
			super.onPreExecute()
			val frameBitmap = decodeResource(resources,
											 R.drawable.photo_frame_filled)

			mainLayout!!.removeAllViews()
			captionedImage = CaptionedImageComponent(
				this@TrafficCameraImageActivity,
				frameBitmap,
				null as Bitmap?,
				cameraDescription)
			mainLayout!!.addView(captionedImage, captionedImageLLP)

			dialog = ProgressDialog(ctx)
			dialog!!.setMessage("Loading image...")
			dialog!!.setCancelable(false)
			dialog!!.isIndeterminate = true
			dialog!!.show()
		}

		override fun doInBackground(vararg urls: String): Bitmap? {
			MLog.i(TAG, "Up to doInBackground")
			val urlToLoad = urls[0]
			var result: Bitmap? = null

			try {
				val stream = java.net.URL(urlToLoad).openStream()
				result = BitmapFactory.decodeStream(stream)
			}
			catch (e: Exception) {
				MLog.w(TAG, e)
			}

			return result
		}

		override fun onPostExecute(result: Bitmap?) {
			if (dialog != null) {
				dialog!!.dismiss()
			}

			if (result == null) {
				// Failed to load camera image - bad connection or just not
				// available on server.
				MLog.i(TAG,
					   "Failed to load camera image - showing error dialog")
				val builder = AlertDialog.Builder(ctx)
				builder.setTitle("Could not load camera image")
				builder.setMessage("Tap the refresh icon at top right to try again.")
				builder.setPositiveButton("OK", null)
				val dialog = builder.create()
				dialog.show()
			} else {
				val frameBitmap = decodeResource(resources,
												 R.drawable.photo_frame_with_black_corners)
				mainLayout!!.removeAllViews()
				captionedImage = CaptionedImageComponent(
					this@TrafficCameraImageActivity, frameBitmap, result,
					cameraDescription)
				mainLayout!!.addView(captionedImage, captionedImageLLP)
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val extras = intent.extras
		val strIndex = extras.getString("index")
		val strStreet = extras.getString("street")
		val strSuburb = extras.getString("suburb")
		val strDescription = extras.getString("description")
		val strUrl = extras.getString("url")
		val strFavourite = extras.getString("favourite")

		MLog.d(TAG, "index=" + strIndex!!)
		MLog.d(TAG, "stree=" + strStreet!!)
		MLog.d(TAG, "suburb=" + strSuburb!!)
		MLog.d(TAG, "description=" + strDescription!!)
		MLog.d(TAG, "url=" + strUrl!!)
		MLog.d(TAG, "favourite=" + strFavourite!!)

		cameraIndex = Integer.parseInt(strIndex)
		cameraStreet = strStreet.trim { it <= ' ' }
		cameraSuburb = strSuburb.trim { it <= ' ' }
		cameraDescription = strDescription.trim { it <= ' ' }
		cameraUrl = strUrl.trim { it <= ' ' }
		cameraFavourite = java.lang.Boolean.parseBoolean(strFavourite)

		createUI()
	}

	// private final int IMAGE_VIEW_ID = 1000;
	//
	// private ImageView imageView;
	private var favouriteMenuItem: MenuItem? = null
	private var cameraFavourite: Boolean = false
	// private ImageView photoFrameImageView;
	private var mainLayout: LinearLayout? = null
	private var captionedImage: CaptionedImageComponent? = null
	private var captionedImageLLP: LinearLayout.LayoutParams? = null

	private fun createUI() {
		mainLayout = LinearLayout(this)
		mainLayout!!.orientation = LinearLayout.VERTICAL
		mainLayout!!.keepScreenOn = true
		mainLayout!!.setBackgroundColor(Color.BLACK)
		mainLayout!!.gravity = Gravity.CENTER

		captionedImage = CaptionedImageComponent(this)
		captionedImageLLP = LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
		mainLayout!!.addView(captionedImage, captionedImageLLP)

		setContentView(mainLayout)
		// Put camera title in action bar
		val actionBar = actionBar
		actionBar?.setDisplayShowCustomEnabled(true)
		actionBar?.setDisplayHomeAsUpEnabled(true)
		val customView = TrafficCameraTitleView(this,
												cameraStreet!!, cameraSuburb!!)
		val customViewALP = ActionBar.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
		actionBar?.setCustomView(customView, customViewALP)

		refresh()
	}

	fun refresh() {
		MLog.i(TAG, "Refreshing")
		val task = DownloadImageTask(this)
		task.execute(cameraUrl)
	}

	fun toggleFavourite() {
		MLog.i(TAG, "Toggle favourite")
		// Raise dialog
		val camera:TrafficCamera? = TrafficCameraCacheSingleton.instance.getCamera(cameraIndex)
		if (camera != null) {
			val pres = FavouriteCameraDialogPresenter(camera)
			val dialog = pres.build(this)
			dialog.setOnDismissListener {
				MLog.i(TAG,
					   "On dismiss listener, camera.isFavourite=" + camera.isFavourite)
				updateActionBarToReflectFavouriteStatus(camera.isFavourite)
			}
			dialog.show()
		}

		MLog.i(TAG, "After dialog.show")
	}

	private fun updateActionBarToReflectFavouriteStatus(isFavourite: Boolean) {
		favouriteMenuItem!!.setIcon(if (isFavourite)
										R.drawable.ic_action_important
									else
										R.drawable.ic_action_not_important)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		val inflater = MenuInflater(this)
		inflater.inflate(R.menu.traffic_camera_image_options_menu, menu)
		favouriteMenuItem = menu.findItem(R.id.toggle_camera_favourite)

		MLog.i(TAG, "Found favouriteMenuItem=" + favouriteMenuItem!!)
		updateActionBarToReflectFavouriteStatus(cameraFavourite)

		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item != null) {
			when (item.itemId) {
				R.id.refresh_camera_image -> refresh()
				R.id.toggle_camera_favourite -> toggleFavourite()
				android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
			}
		}
		return true
	}

	companion object {
		private val TAG = TrafficCameraImageActivity::class.java
			.simpleName
	}
}
