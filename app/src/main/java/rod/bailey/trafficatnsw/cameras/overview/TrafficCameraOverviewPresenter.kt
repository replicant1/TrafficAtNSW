package rod.bailey.trafficatnsw.cameras.overview

import android.content.Context
import io.reactivex.disposables.Disposable
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.command.CommandEngine
import rod.bailey.trafficatnsw.app.command.DefaultErrorHandler
import rod.bailey.trafficatnsw.app.command.DefaultProgressMonitor
import rod.bailey.trafficatnsw.app.command.ICommandSuccessHandler
import rod.bailey.trafficatnsw.cameras.data.DownloadCamerasCommand
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.cameras.data.XCameraCollection
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.service.IDataService
import timber.log.Timber
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.inject.Inject

/**
 * Presents an ITrafficCameraOverviewView
 */
class TrafficCameraOverviewPresenter : ITrafficCameraOverviewPresenter, PropertyChangeListener {

	private val cameraCacheSingleton: TrafficCameraCacheSingleton

	private val dataService: IDataService

	private val ctx: Context

	private lateinit var mode: TrafficCameraListMode

	private lateinit var view: ITrafficCameraOverviewView

	private var disposable: Disposable? = null

	@Inject
	constructor(ctx: Context,
				dataService: IDataService,
				cameraCacheSingleton: TrafficCameraCacheSingleton) {
		this.ctx = ctx
		this.dataService = dataService
		this.cameraCacheSingleton = cameraCacheSingleton
	}

	override fun getScreenTitleForMode(mode: TrafficCameraListMode): Int {
		return when (mode) {
			TrafficCameraListMode.FAVOURITES -> R.string.camera_list_screen_title_favourites
			TrafficCameraListMode.REGIONAL -> R.string.camera_list_screen_title_regional
			TrafficCameraListMode.SYDNEY -> R.string.camera_list_screen_title_sydney
		}
	}

	override fun getEmptyMessageForMode(mode: TrafficCameraListMode): Int {
		return when (mode) {
			TrafficCameraListMode.FAVOURITES -> R.string.camera_list_empty_favourites
			TrafficCameraListMode.REGIONAL -> R.string.camera_list_empty_regional
			TrafficCameraListMode.SYDNEY -> R.string.camera_list_empty_sydney
		}
	}

	override fun loadCamerasAsync(ctx: Context, listView: ListViewWithEmptyMessage) {
		disposable = CommandEngine.execute(
				DownloadCamerasCommand(dataService),
				DefaultProgressMonitor(ctx, ctx.getString(R.string.camera_image_loading_msg)),
				SuccessHandler(this),
				DefaultErrorHandler(ctx, ctx.getString(R.string.camera_image_load_failure_dialog_msg))
		)
	}

	override fun onIViewCreated(view: ITrafficCameraOverviewView, vararg initData: Any?) {
		this.view = view
		val modeKeyInt: Int = initData[0] as Int
		mode = TrafficCameraListMode.values().get(modeKeyInt)
	}

	override fun onIViewDestroyed() {
		disposable?.dispose()
	}

	override fun propertyChange(evt: PropertyChangeEvent?) {
		Timber.d("Into TCOverviewPresenter. About to call view.refreshCameraList()")
		view.refreshCameraList()
	}

	inner class SuccessHandler(val listener: PropertyChangeListener) : ICommandSuccessHandler {

		override fun onSuccess(result: Any?) {
			val allCameras: XCameraCollection = result as XCameraCollection
			if (allCameras.cameras != null) {
				cameraCacheSingleton.init(allCameras.cameras)
				cameraCacheSingleton.setPropertyChangeListener(listener)
				view.refreshCameraList()
			} else {
				Timber.w("allCameras.cameras was null")
			}
		}
	}
}