package rod.bailey.trafficatnsw.cameras.overview

import android.content.Context
import io.reactivex.disposables.Disposable
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

		// TODO: Prime cameraCache with loaded response.
//		cameraCache.init(ctx)
//		cameraCache.addPropertyChangeListener(this)
//
//		view.setScreenTitle(mode.actionBarTitle)
//		view.setAdapter(TrafficCameraListAdapter((mode.filter)))
	}

	override fun onIViewDestroyed() {
		disposable?.dispose()
	}

	override fun propertyChange(evt: PropertyChangeEvent?) {
		val adapter = TrafficCameraListAdapter(mode.filter)
		view.setAdapter(adapter);
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
			val adapter = TrafficCameraListAdapter(mode.filter)
			view.setAdapter(adapter)
		}
	}
}