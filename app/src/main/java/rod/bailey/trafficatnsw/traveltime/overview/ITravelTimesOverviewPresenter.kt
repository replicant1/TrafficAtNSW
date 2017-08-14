package rod.bailey.trafficatnsw.traveltime.overview

import android.content.Context
import rod.bailey.trafficatnsw.app.mvp.IPresenter
import java.beans.PropertyChangeListener

/**
 * Implemented by any party presented an ITravelTimesOverviewView.
 */
interface ITravelTimesOverviewPresenter : IPresenter<ITravelTimesOverviewView>, PropertyChangeListener {

	/**
	 * When eventually loaded, this.onMotorwayDataLoaded() will be called with the
	 * async loaded data.
	 *
	 * @param ctx Context in which progress dialog is to be displayed
	 */
	fun onFreshMotorwayDataRequested(ctx: Context)
}