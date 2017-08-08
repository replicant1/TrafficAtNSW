package rod.bailey.trafficatnsw.app

import android.app.Application
import dagger.Component
import rod.bailey.trafficatnsw.cameras.data.DownloadImageTask
import rod.bailey.trafficatnsw.cameras.ui.TrafficCameraListAdapter
import rod.bailey.trafficatnsw.cameras.ui.TrafficCameraListFragment
import rod.bailey.trafficatnsw.cameras.image.TrafficCameraImageActivity
import rod.bailey.trafficatnsw.common.service.RemoteDataService
import rod.bailey.trafficatnsw.common.service.TestDataService
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsTask
import rod.bailey.trafficatnsw.hazard.ui.HazardListAdapter
import rod.bailey.trafficatnsw.hazard.ui.HazardListFragment
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivity
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsListAdapter
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.traveltime.data.DownloadTravelTimesTask
import rod.bailey.trafficatnsw.traveltime.ui.TravelTimesFragment
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry
import rod.bailey.trafficatnsw.hazard.ui.HazardListItemView
import javax.inject.Singleton

/**
 * Dagger application component. Any class that wants to have objects dependency injected
 * into it needs to register with this class:
 * <pre>
 *     (application as TrafficAtNSWApplication).component.inject(this)
 * </pre>
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, RemoteDataServiceModule::class))
interface AppComponent {
	fun inject(app: Application)
	fun inject(activity: MainActivity)
	fun inject(task: DownloadHazardsTask)
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
	fun inject(singleton: MotorwayConfigRegistry)
	fun inject(view: HazardListItemView)
	fun inject(service: TestDataService)
	fun inject(service: RemoteDataService)
	fun inject(task: DownloadImageTask)
}