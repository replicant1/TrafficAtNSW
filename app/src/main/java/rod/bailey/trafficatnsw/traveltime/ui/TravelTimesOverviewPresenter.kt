package rod.bailey.trafficatnsw.traveltime.ui

import android.content.Context
import android.util.Log
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.EBean
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.traveltime.data.*
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import javax.inject.Inject

/**
 * Created by rodbailey on 10/8/17.
 */
open class TravelTimesOverviewPresenter : ITravelTimesOverviewPresenter {

	@Inject
	constructor(ctx: Context, motorwayRegistry: MotorwayConfigRegistry) {
		Log.d(LOG_TAG, "Into c'tor with ${ctx} and ${motorwayRegistry}")
		this.ctx = ctx
		this.motorwayRegistry = motorwayRegistry
	}

	val ctx: Context

	val motorwayRegistry: MotorwayConfigRegistry

	/** Travel times for the motorway currently being displayed  */
	var mwayStore: MotorwayTravelTimesStore? = null

	/** Config for the motorway currently being display  */
	lateinit var motorwayConfig: MotorwayConfig

	lateinit var motorway: Motorway

	private lateinit var view: ITravelTimesOverviewView

	fun lazyInitMotorwayConfig() {
		motorwayConfig = when (motorway) {
			Motorway.M1 -> motorwayRegistry.m1Config
			Motorway.M2 -> motorwayRegistry.m2Config
			Motorway.M4 -> motorwayRegistry.m4Config
			Motorway.M7 -> motorwayRegistry.m7Config
		}
		view.setScreenTitle(ctx.getString(R.string.tt_screen_title, motorwayConfig.motorwayName ?: ""))
		//onFreshMotorwayDataRequested(ctx)
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		MLog.i(LOG_TAG, "TTOverview Presenter notified property ${event.propertyName} has changed")
		if (event.propertyName == MotorwayTravelTimesStore.PROPERTY_TOTAL_TRAVEL_TIME) {
			val newStore: MotorwayTravelTimesStore = MotorwayTravelTimesStore(
				TrafficAtNSWApplication.context, motorwayConfig)
			notifyViewOfNewData(newStore)
		}
	}

	override fun onFreshMotorwayDataRequested(ctx: Context) {
		Log.d(LOG_TAG, "About to start download TT task")
		DownloadTravelTimesTask(ctx, this, motorwayConfig).execute()
	}

	override fun onIViewDestroyed() {
		// Empty
	}

	override fun onIViewCreated(view: ITravelTimesOverviewView, vararg initData: Any?) {
		this.view = view
		val motorwayKey: Int = initData[0] as Int
		motorway = Motorway.values()[motorwayKey]
		lazyInitMotorwayConfig()
	}

	override fun onMotorwayDataLoaded(store: MotorwayTravelTimesStore?) {
		notifyViewOfNewData(store)
	}

	/**
	 * Before passing through to view, add self as listener so will
	 * receive notification of changes in event that any segment is
	 * excluded and so total TT needs updating
	 */
	private fun notifyViewOfNewData(newData: MotorwayTravelTimesStore?) {
		mwayStore?.removePropertyChangeListener(this)
		mwayStore = newData
		mwayStore?.addPropertyChangeListener(this)
		view.setMotorwayData(mwayStore)
	}

	companion object {
		private val LOG_TAG: String = TravelTimesOverviewPresenter::class.java.simpleName
	}
}