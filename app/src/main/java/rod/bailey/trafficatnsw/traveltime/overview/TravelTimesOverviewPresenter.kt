package rod.bailey.trafficatnsw.traveltime.overview

import android.content.Context
import android.util.Log
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.traveltime.data.*
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import javax.inject.Inject

/**
 * Presents an ITravelTimesOverviewView - a screen of travel times
 * pertaining to the one motorway.
 */
class TravelTimesOverviewPresenter : ITravelTimesOverviewPresenter {

	@Inject
	constructor(ctx: Context, motorwayRegistry: MotorwayConfigRegistry) {
		this.ctx = ctx
		this.motorwayRegistry = motorwayRegistry
	}

	val ctx: Context

	val motorwayRegistry: MotorwayConfigRegistry

	/** Travel times for the motorway currently being displayed  */
	lateinit var mwayStore: MotorwayTravelTimesStore

	/** Config for the motorway currently being display  */
	lateinit var motorwayConfig: MotorwayConfig

	/** Motorway currently being displayed */
	lateinit var motorway: Motorway

	/** View being presented */
	private lateinit var view: ITravelTimesOverviewView

	private fun lazyInitMotorwayConfig() {
		motorwayConfig = when (motorway) {
			Motorway.M1 -> motorwayRegistry.m1Config
			Motorway.M2 -> motorwayRegistry.m2Config
			Motorway.M4 -> motorwayRegistry.m4Config
			Motorway.M7 -> motorwayRegistry.m7Config
		}
		view.setScreenTitle(ctx.getString(R.string.tt_screen_title, motorwayConfig.motorwayName))
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		MLog.i(LOG_TAG, "TTOverview Presenter notified property ${event.propertyName} has changed")
		if (event.propertyName == MotorwayTravelTimesStore.PROPERTY_TOTAL_TRAVEL_TIME) {
			view.setMotorwayData(mwayStore)
		}
	}

	override fun onFreshMotorwayDataRequested(ctx: Context) {
		DownloadTravelTimesTask(ctx, this, motorwayConfig).execute()
	}

	override fun onIViewDestroyed() {
		// Empty
	}

	/**
	 * @param view The IView being presented
	 * @param initData Expects a single Int, being the ordinal value of a Motorway element
	 */
	override fun onIViewCreated(view: ITravelTimesOverviewView, vararg initData: Any?) {
		this.view = view
		val motorwayKey: Int = initData[0] as Int
		motorway = Motorway.values()[motorwayKey]
		lazyInitMotorwayConfig()
	}

	/**
	 * Invoked async when previous request to load motoway data has completed.
	 */
	override fun onMotorwayDataLoaded(store: MotorwayTravelTimesStore) {
		mwayStore = store
		notifyViewOfNewData(store)
	}

	/**
	 * Before passing through to view, add self as listener so will
	 * receive notification of changes in event that any segment is
	 * excluded and so total TT needs updating
	 */
	private fun notifyViewOfNewData(newData: MotorwayTravelTimesStore) {
		mwayStore.removePropertyChangeListener(this)
		mwayStore = newData
		mwayStore.addPropertyChangeListener(this)
		view.setMotorwayData(newData)
	}

	companion object {
		private val LOG_TAG: String = TravelTimesOverviewPresenter::class.java.simpleName
	}
}