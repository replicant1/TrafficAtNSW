package rod.bailey.trafficatnsw.traveltime.overview

import android.content.Context
import io.reactivex.disposables.Disposable
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.command.CommandEngine
import rod.bailey.trafficatnsw.app.command.DefaultErrorHandler
import rod.bailey.trafficatnsw.app.command.DefaultProgressMonitor
import rod.bailey.trafficatnsw.app.command.ICommandSuccessHandler
import rod.bailey.trafficatnsw.service.IDataService
import rod.bailey.trafficatnsw.traveltime.data.*
import timber.log.Timber
import java.beans.PropertyChangeEvent
import javax.inject.Inject

/**
 * Presents an ITravelTimesOverviewView - a screen of travel times
 * pertaining to the one motorway.
 */
class TravelTimesOverviewPresenter : ITravelTimesOverviewPresenter {

	@Inject
	constructor(ctx: Context,
				motorwayRegistry: MotorwayConfigRegistry,
				dataService: IDataService) {
		this.ctx = ctx
		this.motorwayRegistry = motorwayRegistry
		this.dataService = dataService
	}

	val dataService: IDataService

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

	var disposable: Disposable? = null

	private fun lazyInitMotorwayConfig() {
		motorwayConfig = when (motorway) {
			Motorway.M1 -> motorwayRegistry.m1Config
			Motorway.M4 -> motorwayRegistry.m4Config
			Motorway.M7 -> motorwayRegistry.m7Config
		}
		view.setScreenTitle(ctx.getString(R.string.tt_screen_title, motorwayConfig.motorwayName))
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		Timber.i("TTOverview Presenter notified property ${event.propertyName} has changed")
		if (event.propertyName == MotorwayTravelTimesStore.PROPERTY_TOTAL_TRAVEL_TIME) {
			view.setMotorwayData(mwayStore)
		}
	}

	override fun onFreshMotorwayDataRequested(ctx: Context) {
		disposable = CommandEngine.execute(
				DownloadTravelTimesCommand(ctx, motorwayConfig, dataService),
				DefaultProgressMonitor(ctx, ctx.getString(R.string.tt_load_progress_msg, motorwayConfig.motorwayName)),
				SuccessHandler(),
				DefaultErrorHandler(ctx, ctx.getString(R.string.tt_download_dialog_msg)))
	}

	inner class SuccessHandler : ICommandSuccessHandler {
		override fun onSuccess(result: Any?) {
			val store: MotorwayTravelTimesStore = result as MotorwayTravelTimesStore
			mwayStore = store
			notifyViewOfNewData(store)
		}
	}

	override fun onDetachView() {
		disposable?.dispose()
	}

	/**
	 * @param view The IView being presented
	 * @param initData Expects a single Int, being the ordinal value of a Motorway element
	 */
	override fun onAttachView(view: ITravelTimesOverviewView, vararg initData: Any?) {
		this.view = view
		val motorwayKey: Int = initData[0] as Int
		motorway = Motorway.values()[motorwayKey]
		lazyInitMotorwayConfig()
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
}