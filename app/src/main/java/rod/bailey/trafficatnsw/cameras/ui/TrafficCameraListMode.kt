package rod.bailey.trafficatnsw.cameras.ui

import rod.bailey.trafficatnsw.cameras.filter.AdmitFavouritesTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.AdmitRegionalTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.AdmitSydneyTrafficCameraFilter
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter

enum class TrafficCameraListMode constructor(val displayName: String,
											 val actionBarTitle: String,
											 val filter: ITrafficCameraFilter) {
	FAVOURITES("Favourites", "Favourite Cameras",
			   AdmitFavouritesTrafficCameraFilter()), //
	REGIONAL("Regional", "Cameras in Regional NSW",
			 AdmitRegionalTrafficCameraFilter()), //
	SYDNEY("Sydney", "Cameras in Sydney", AdmitSydneyTrafficCameraFilter())
}