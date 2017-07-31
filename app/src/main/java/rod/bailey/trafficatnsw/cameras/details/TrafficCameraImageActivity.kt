package rod.bailey.trafficatnsw.cameras.details

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.*

/**
 * Screen containing a single Card that has a traffic camera imageView at top
 * and a description of the imageView underneath. Image may be refreshed or
 * marked as a extraFavourite by the user.
 */
@EActivity(R.layout.activity_camera_image)
open class TrafficCameraImageActivity : AppCompatActivity(), ITrafficCameraImageDisplayer {

	@ViewById(R.id.iv_camera_image)
	@JvmField
	var imageView: ImageView? = null

	@ViewById(R.id.tv_camera_title)
	@JvmField
	var titleTextView: AppCompatTextView? = null

	@ViewById(R.id.tv_camera_subtitle)
	@JvmField
	var subtitleTextView: AppCompatTextView? = null

	@ViewById(R.id.tv_camera_description)
	@JvmField
	var descriptionTextView: AppCompatTextView? = null

	@Extra("index")
	@JvmField
	var extraIndex: Int? = null

	@Extra("street")
	@JvmField
	var extraStreet: String? = null

	@Extra("suburb")
	@JvmField
	var extraSuburb: String? = null

	@Extra("description")
	@JvmField
	var extraDescription: String? = null

	@Extra("url")
	@JvmField
	var extraUrl: String? = null

	@Extra("extraFavourite")
	@JvmField
	var extraFavourite: Boolean? = null

	private var favouriteMenuItem: MenuItem? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
	}

	override fun displayImage(image: Bitmap) {
		imageView?.setImageBitmap(image)
	}

	@AfterViews
	fun afterViews() {
		val actionBar = supportActionBar
		actionBar?.title = getString(R.string.camera_image_screen_title)
		actionBar?.setDisplayShowCustomEnabled(true)
		actionBar?.setDisplayHomeAsUpEnabled(true)

		titleTextView?.text = extraStreet
		subtitleTextView?.text = extraSuburb
		descriptionTextView?.text = extraDescription

		updateActionBarPerFavouriteStatus(extraFavourite ?: false)

		refresh()
	}

	fun refresh() {
		DownloadImageTask(context = this, displayer = this).execute(extraUrl)
	}

	fun toggleFavourite() {
		val camera: TrafficCamera? = TrafficCameraCacheSingleton.instance.getCamera(extraIndex ?: 0)
		if (camera != null) {
			val pres = FavouriteCameraDialogPresenter(camera)
			val dialog = pres.build(this)
			dialog.setOnDismissListener {
				updateActionBarPerFavouriteStatus(camera.isFavourite)
			}
			dialog.show()
		}
	}

	private fun updateActionBarPerFavouriteStatus(isFavourite: Boolean) {
		favouriteMenuItem?.setIcon(
			if (isFavourite)
				R.drawable.ic_star_white_24dp
			else
				R.drawable.ic_star_border_white_24dp)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menu_traffic_camera_image_options, menu)
		favouriteMenuItem = menu.findItem(R.id.toggle_camera_favourite)
		updateActionBarPerFavouriteStatus(extraFavourite ?: false)
		return true
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
		private val LOG_TAG: String = TrafficCameraImageActivity::class.java.simpleName

		fun start(ctx: Context, camera: TrafficCamera) {
			Log.d(LOG_TAG, "******* At start time, favourite=${camera.isFavourite}")
			TrafficCameraImageActivity_.intent(ctx)
				.extraIndex(camera.index)
				.extraStreet(camera.street)
				.extraSuburb(camera.suburb)
				.extraDescription(camera.description)
				.extraUrl(camera.url)
				.extraFavourite(camera.isFavourite)
				.start()
		}
	}
}
