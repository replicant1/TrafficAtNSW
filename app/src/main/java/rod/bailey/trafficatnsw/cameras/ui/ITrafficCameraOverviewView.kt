package rod.bailey.trafficatnsw.cameras.ui

import rod.bailey.trafficatnsw.app.mvp.IView

/**
 * Created by rodbailey on 11/8/17.
 */
interface ITrafficCameraOverviewView : IView {

	fun setAdapter(adapter: TrafficCameraListAdapter)

	fun setScreenTitle(title: String)
}