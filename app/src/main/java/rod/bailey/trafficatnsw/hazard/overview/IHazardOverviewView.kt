package rod.bailey.trafficatnsw.hazard.overview

import rod.bailey.trafficatnsw.app.mvp.IView

/**
 * Implemented by any party presenting the Hazard Overview (list of hazards for
 * a part of NSW).
 */
interface IHazardOverviewView : IView {

	/**
	 * Invoked when new data has been placed in the HazardCacheSingleton ready for
	 * this view to access and refresh its display.
	 */
	fun refreshHazardList()
}