package rod.bailey.trafficatnsw.cameras.ui

import android.content.Context
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.inject.Inject

/**
 * Presents an ITrafficCameraOverviewView
 */
class TrafficCameraOverviewPresenter : ITrafficCameraOverviewPresenter, PropertyChangeListener {

	@Inject
	constructor(ctx: Context) {
		this.ctx = ctx
	}

	val ctx: Context

	@Inject
	lateinit var cameraCache: TrafficCameraCacheSingleton

	private lateinit var view: ITrafficCameraOverviewView

	private lateinit var mode: TrafficCameraListMode

	override fun onIViewCreated(view: ITrafficCameraOverviewView, vararg initData: Any?) {
		this.view = view

		val modeKeyInt: Int = initData[0] as Int
		mode = TrafficCameraListMode.values().get(modeKeyInt)

		cameraCache.init(ctx)
		cameraCache.addPropertyChangeListener(this)

		view.setScreenTitle(mode.actionBarTitle)
		view.setAdapter(TrafficCameraListAdapter((mode.filter)))
	}

	override fun onIViewDestroyed() {
		// Empty
	}

	override fun propertyChange(evt: PropertyChangeEvent?) {
		val adapter = TrafficCameraListAdapter(mode.filter)
		view.setAdapter(adapter);
	}
}