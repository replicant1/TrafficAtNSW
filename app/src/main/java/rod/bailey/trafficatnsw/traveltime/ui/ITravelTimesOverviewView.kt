package rod.bailey.trafficatnsw.traveltime.ui

import android.support.annotation.StringRes
import rod.bailey.trafficatnsw.app.mvp.IView
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore

/**
 * Created by rodbailey on 10/8/17.
 */
interface ITravelTimesOverviewView: IView {

	/**
	 * @param db Null means no data available at this time
	 */
	fun setMotorwayData(db: MotorwayTravelTimesStore?)

	fun setScreenTitle(title: String)
}