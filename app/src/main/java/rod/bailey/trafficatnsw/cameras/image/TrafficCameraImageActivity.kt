package rod.bailey.trafficatnsw.cameras.image

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import io.reactivex.disposables.Disposable
import org.androidannotations.annotations.*
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.app.command.CommandEngine
import rod.bailey.trafficatnsw.app.command.DefaultErrorHandler
import rod.bailey.trafficatnsw.app.command.DefaultProgressMonitor
import rod.bailey.trafficatnsw.app.command.ICommandSuccessHandler
import rod.bailey.trafficatnsw.cameras.data.DownloadCameraImageCommand
import rod.bailey.trafficatnsw.cameras.data.TrafficCamera
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.cameras.data.XCamera
import rod.bailey.trafficatnsw.service.IDataService
import javax.inject.Inject

/**
 * Screen containing a single Card that has a traffic camera imageView at top
 * and a description of the imageView underneath. Image may be refreshed or
 * marked as a extraFavourite by the user.
 */
@EActivity(R.layout.activity_camera_image)
@OptionsMenu(R.menu.menu_traffic_camera_image_options)
open class TrafficCameraImageActivity : AppCompatActivity() {

	init {
		// Enable field injection
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var dataService: IDataService

	@Inject
	lateinit var cameraCache: TrafficCameraCacheSingleton

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

	@Extra("cameraId")
	@JvmField
	var extraCameraId: String? = null

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

	@OptionsMenuItem(R.id.toggle_camera_favourite)
	@JvmField
	var favouriteMenuItem: MenuItem? = null

	private var disposable: Disposable? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
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

	@OptionsItem(R.id.refresh_camera_image)
	fun refresh() {
		loadCameraImageAsync()
	}

	private fun loadCameraImageAsync() {
		disposable = CommandEngine.execute(
			DownloadCameraImageCommand(dataService, extraCameraId ?: ""),
			DefaultProgressMonitor(this, getString(R.string.camera_image_loading_msg)),
			SuccessHandler(),
			DefaultErrorHandler(this, getString(R.string.camera_image_load_failure_dialog_msg))
		)
	}

	inner class SuccessHandler : ICommandSuccessHandler {
		override fun onSuccess(result: Any?) {
			result?.let {
				val image: Bitmap = result as Bitmap
				imageView?.setImageBitmap(image)
			}
		}
	}

	@OptionsItem(R.id.toggle_camera_favourite)
	fun toggleFavourite() {
		val camera: XCamera? = cameraCache.getCamera(extraCameraId ?: "")
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
		updateActionBarPerFavouriteStatus(extraFavourite ?: false)
		return true
	}

	override fun onPause() {
		super.onPause()
		disposable?.dispose()
	}

	@OptionsItem(android.R.id.home)
	fun actionBarHome() {
		NavUtils.navigateUpFromSameTask(this)
	}

	companion object {
		private val LOG_TAG: String = TrafficCameraImageActivity::class.java.simpleName

		fun start(ctx: Context, camera: TrafficCamera) {
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
