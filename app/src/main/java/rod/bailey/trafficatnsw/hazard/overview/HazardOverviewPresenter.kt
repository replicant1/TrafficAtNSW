package rod.bailey.trafficatnsw.hazard.overview

import android.content.Context
import io.reactivex.disposables.Disposable
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

	override fun loadHazardsAsync(ctx: Context, listView: ListViewWithEmptyMessage) {
		disposable = CommandEngine.execute(
			DownloadHazardsCommand(dataService),
			DefaultProgressMonitor(ctx, ctx.getString(R.string.hazards_list_load_progress_msg)),
			SuccessHandler(),
			DefaultErrorHandler(ctx, ctx.getString(R.string.hazards_list_load_progress_msg)))
	}

	inner class SuccessHandler : ICommandSuccessHandler {
		override fun onSuccess(result: Any?) {
			val allHazards: XHazardCollection = result as XHazardCollection
			hazardCacheSingleton.init(allHazards.hazards as List<XHazard>)
			view.refreshHazardList()
		}
	}

	override fun onAttachView(view: IHazardOverviewView, vararg initData: Any?) {
		this.view = view
	}

	override fun onDetachView() {
		disposable?.dispose()
	}

	companion object {
		private val LOG_TAG: String = HazardOverviewPresenter::class.java.simpleName
	}
}