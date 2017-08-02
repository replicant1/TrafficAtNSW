package rod.bailey.trafficatnsw.app

import android.app.Application
import dagger.Component
import rod.bailey.trafficatnsw.MainActivity
import rod.bailey.trafficatnsw.cameras.TrafficCameraListAdapter
import rod.bailey.trafficatnsw.cameras.TrafficCameraListFragment
import rod.bailey.trafficatnsw.cameras.details.TrafficCameraImageActivity
import rod.bailey.trafficatnsw.hazard.DownloadHazardFileTask
import rod.bailey.trafficatnsw.hazard.HazardListAdapter
import rod.bailey.trafficatnsw.hazard.HazardListFragment
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivity
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsListAdapter
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.traveltime.DownloadTravelTimesTask
import rod.bailey.trafficatnsw.traveltime.TravelTimesFragment
import rod.bailey.trafficatnsw.traveltime.common.TravelTimesCacheSingleton
import rod.bailey.trafficatnsw.ui.view.HazardListItemView
import javax.inject.Singleton

/**
 * Dagger application component. Any class that wants to have objects dependency injected
 * into it needs to register with this class:
 * <pre>
 *     (application as TrafficAtNSWApplication).component.inject(this)
 * </pre>
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
	fun inject(app: Application)
	fun inject(activity: MainActivity)
	fun inject(task: DownloadHazardFileTask)
	fun inject(adapter: HazardListAdapter)
	fun inject(fragment: HazardListFragment)
	fun inject(activity: HazardDetailsActivity)
	fun inject(activity: ShowHazardOnMapActivity)
	fun inject(adapter: TrafficCameraListAdapter)
	fun inject(fragment: TrafficCameraListFragment)
	fun inject(activity: TrafficCameraImageActivity)
	fun inject(task: DownloadTravelTimesTask)
	fun inject(fragment: TravelTimesFragment)
	fun inject(adapter: HazardDetailsListAdapter)
	fun inject(singleton: TravelTimesCacheSingleton)
	fun inject(view: HazardListItemView)
}