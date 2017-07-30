package rod.bailey.trafficatnsw.cameras.details

import android.content.Context
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.DownloadImageTask
import rod.bailey.trafficatnsw.cameras.FavouriteCameraDialogPresenter
import rod.bailey.trafficatnsw.cameras.TrafficCamera
import rod.bailey.trafficatnsw.cameras.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.util.MLog

/**
 * Screen containing a single Card that has a traffic camera image at top
 * and a description of the image underneath. Image may be refreshed or
 * marked as a favourite by the user.
 */
@EActivity(R.layout.activity_camera_image)
open class TrafficCameraImageActivity : AppCompatActivity() {

	@ViewById(R.id.iv_camera_image)
	@JvmField
	var image: ImageView? = null

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
	var cameraIndex: Int? = null

	@Extra("street")
	@JvmField
	var cameraStreet: String? = null

	@Extra("suburb")
	@JvmField
	var cameraSuburb: String? = null

	@Extra("description")
	@JvmField
	var cameraDescription: String? = null

	@Extra("url")
	@JvmField
	var cameraUrl: String? = null

	@Extra("favourite")
	@JvmField
	var favourite: Boolean? = null

	private var favouriteMenuItem: MenuItem? = null
	private var cameraFavourite: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
	}

	@AfterViews
	fun afterViews() {
		// TODO Put camera title in action bar
		val actionBar = actionBar
		actionBar?.setDisplayShowCustomEnabled(true)
		actionBar?.setDisplayHomeAsUpEnabled(true)

		titleTextView?.text = cameraStreet
		subtitleTextView?.text = cameraSuburb
		descriptionTextView?.text = cameraDescription

		refresh()
	}

	fun refresh() {
		DownloadImageTask(this).execute(cameraUrl)
	}

	fun toggleFavourite() {
		MLog.i(TAG, "Toggle favourite")
		val camera: TrafficCamera? = TrafficCameraCacheSingleton.instance.getCamera(cameraIndex ?: 0)
		if (camera != null) {
			val pres = FavouriteCameraDialogPresenter(camera)
			val dialog = pres.build(this)
			dialog.setOnDismissListener {
				MLog.i(TAG,
					   "On dismiss listener, camera.isFavourite=" + camera.isFavourite)
				updateActionBarPerFavouriteStatus(camera.isFavourite)
			}
			dialog.show()
		}
	}

	private fun updateActionBarPerFavouriteStatus(isFavourite: Boolean) {
		favouriteMenuItem?.setIcon(
			if (isFavourite)
				R.drawable.ic_action_important
			else
				R.drawable.ic_action_not_important)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		val inflater = MenuInflater(this)
		inflater.inflate(R.menu.traffic_camera_image_options_menu, menu)
		favouriteMenuItem = menu.findItem(R.id.toggle_camera_favourite)

		MLog.i(TAG, "Found favouriteMenuItem=" + favouriteMenuItem!!)
		updateActionBarPerFavouriteStatus(cameraFavourite)

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
		private val TAG = TrafficCameraImageActivity::class.java.simpleName

		fun start(ctx: Context, camera: TrafficCamera) {
			TrafficCameraImageActivity_.intent(ctx)
				.cameraIndex(camera.index)
				.cameraStreet(camera.street)
				.cameraSuburb(camera.suburb)
				.cameraDescription(camera.description)
				.cameraUrl(camera.url)
				.favourite(camera.isFavourite)
				.start()
		}
	}
}
