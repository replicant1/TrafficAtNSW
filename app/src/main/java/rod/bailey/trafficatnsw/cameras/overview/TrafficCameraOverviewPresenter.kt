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
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.inject.Inject

/**
 * Presents an ITrafficCameraOverviewView
 */
class TrafficCameraOverviewPresenter : ITrafficCameraOverviewPresenter, PropertyChangeListener {

	@Inject
	constructor(ctx: Context,
				dataService: IDataService,
				cameraCacheSingleton: TrafficCameraCacheSingleton) {
		this.ctx = ctx
		this.dataService = dataService
		this.cameraCacheSingleton = cameraCacheSingleton
	}

	private val cameraCacheSingleton: TrafficCameraCacheSingleton

	private val dataService: IDataService

	private val ctx: Context

	private lateinit var view: ITrafficCameraOverviewView

	private lateinit var mode: TrafficCameraListMode

	private var disposable: Disposable? = null

	override fun onIViewCreated(view: ITrafficCameraOverviewView, vararg initData: Any?) {
		this.view = view

		val modeKeyInt: Int = initData[0] as Int
		mode = TrafficCameraListMode.values().get(modeKeyInt)
	}

	override fun getScreenTitleForMode(mode: TrafficCameraListMode?): Int {
		return when (mode) {
			TrafficCameraListMode.FAVOURITES -> R.string.hazards_list_screen_title_sydney
			TrafficCameraListMode.REGIONAL -> R.string.hazards_list_screen_title_sydney
			TrafficCameraListMode.SYDNEY -> R.string.hazards_list_screen_title_sydney
			null -> R.string.hazards_list_screen_title_sydney
		}
	}

	override fun getEmptyMessageForMode(mode: TrafficCameraListMode?): Int {
		return when (mode) {
			TrafficCameraListMode.FAVOURITES -> R.string.camera_no_favourites
			TrafficCameraListMode.REGIONAL -> R.string.camera_no_favourites
			TrafficCameraListMode.SYDNEY -> R.string.camera_no_favourites
			null -> R.string.camera_no_favourites
		}
	}

	override fun onIViewDestroyed() {
		disposable?.dispose()
	}

	override fun propertyChange(evt: PropertyChangeEvent?) {
		view.refreshCameraList()
	}

	override fun loadCamerasAsync(ctx: Context, listView: ListViewWithEmptyMessage) {
		disposable = CommandEngine.execute(
			DownloadCamerasCommand(dataService),
			DefaultProgressMonitor(ctx, "Loading cameras&#8230;"),
			SuccessHandler(this),
			DefaultErrorHandler(ctx, "Failed to load cameras")
		)
	}

	inner class SuccessHandler(val listener: PropertyChangeListener): ICommandSuccessHandler {

		override fun onSuccess(result: Any?) {
			val allCameras: XCameraCollection = result as XCameraCollection
			cameraCacheSingleton.init(ctx, allCameras)
			cameraCacheSingleton.addPropertyChangeListener(listener)
//			val adapter = TrafficCameraListAdapter(mode.filter)
//			view.setAdapter(adapter)
			view.refreshCameraList()
		}
	}
}