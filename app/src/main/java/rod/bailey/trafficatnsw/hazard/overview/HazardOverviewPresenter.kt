package rod.bailey.trafficatnsw.hazard.overview

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.Trace
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.command.CommandEngine
import rod.bailey.trafficatnsw.app.command.DefaultErrorHandler
import rod.bailey.trafficatnsw.app.command.DefaultProgressMonitor
import rod.bailey.trafficatnsw.app.command.ICommandSuccessHandler
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsCommand
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection
import rod.bailey.trafficatnsw.service.IDataService
import java.util.*
import java.util.concurrent.Callable
import javax.inject.Inject

class HazardOverviewPresenter : IHazardOverviewPresenter {

	private lateinit var view: IHazardOverviewView

	private val dataService: IDataService

	private val config: ConfigSingleton

	private val hazardCacheSingleton: HazardCacheSingleton

	private var disposable: Disposable? = null

	@Inject
	constructor(hazardCacheSingleton: HazardCacheSingleton,
				dataService: IDataService,
				config: ConfigSingleton) {
		this.hazardCacheSingleton = hazardCacheSingleton
		this.dataService = dataService
		this.config = config
	}

	override fun getScreenTitleForMode(mode: HazardOverviewMode?): Int {
		return if (mode === HazardOverviewMode.SYDNEY)
			R.string.hazards_list_screen_title_sydney
		else
			R.string.hazards_list_screen_title_regional_nsw
	}

	override fun getEmptyMessageForMode(mode: HazardOverviewMode?): Int {
		return if (mode === HazardOverviewMode.SYDNEY)
			R.string.hazards_list_screen_empty_sydney
		else
			R.string.hazards_list_screen_empty_regional_nsw
	}

	@Trace
	override fun loadHazardsAsync(ctx: Context, listView: ListViewWithEmptyMessage) {
		Log.d(LOG_TAG, "*** Into loadHazardsAsync ***")

		CommandEngine.execute(
			DownloadHazardsCommand(dataService),
			DefaultProgressMonitor(),
			DownloadHazardsSuccessHandler(),
			DefaultErrorHandler())
	}

	override fun onIViewCreated(view: IHazardOverviewView, vararg initData: Any?) {
		this.view = view
	}

	override fun onIViewDestroyed() {
		disposable?.dispose()
	}

	companion object {
		private val LOG_TAG: String = HazardOverviewPresenter::class.java.simpleName
	}
}