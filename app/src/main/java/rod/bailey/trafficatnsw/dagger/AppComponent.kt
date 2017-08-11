package rod.bailey.trafficatnsw.dagger

import android.app.Application
import dagger.Component
import rod.bailey.trafficatnsw.app.MainActivity
import rod.bailey.trafficatnsw.cameras.data.DownloadImageTask
import rod.bailey.trafficatnsw.cameras.ui.TrafficCameraListAdapter
import rod.bailey.trafficatnsw.cameras.ui.TrafficCameraListFragment
import rod.bailey.trafficatnsw.cameras.image.TrafficCameraImageActivity
import rod.bailey.trafficatnsw.service.RemoteDataService
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsTask
import rod.bailey.trafficatnsw.hazard.overview.HazardListAdapter
import rod.bailey.trafficatnsw.hazard.overview.HazardListFragment
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivity
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsListAdapter
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity
import rod.bailey.trafficatnsw.traveltime.data.DownloadTravelTimesTask
import rod.bailey.trafficatnsw.traveltime.overview.TravelTimesFragment
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry
import rod.bailey.trafficatnsw.hazard.overview.HazardListItemView
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
	fun inject(service: RemoteDataService)
	fun inject(task: DownloadImageTask)
}