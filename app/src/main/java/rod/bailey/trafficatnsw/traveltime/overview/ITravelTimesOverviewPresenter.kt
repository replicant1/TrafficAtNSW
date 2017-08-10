package rod.bailey.trafficatnsw.traveltime.overview

import android.content.Context
import rod.bailey.trafficatnsw.app.mvp.IPresenter
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
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

	/**
	 * Invoked by an external party that has loaded motorway data and wants
	 * to communicate it to this presenter.
	 *
	 * @param Null means we couldn't load the motorway data e.g. outside of operating hours
	 */
	fun onMotorwayDataLoaded(store: MotorwayTravelTimesStore?)
}