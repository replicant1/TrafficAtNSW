package rod.bailey.trafficatnsw.traveltime.overview

import rod.bailey.trafficatnsw.app.mvp.IView
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore

/**
 * a view of the Travel Times overview of all segments
 * for a single motorway.
 */
interface ITravelTimesOverviewView: IView {

	/**
	 * @param db Null means no data available at this time
	 */
	fun setMotorwayData(db: MotorwayTravelTimesStore)

	/**
	 * @param Adopt this as screen title in action bar
	 */
	fun setScreenTitle(title: String)
}