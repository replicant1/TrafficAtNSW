package rod.bailey.trafficatnsw.hazard.overview

import android.content.Context
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsTask
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import javax.inject.Inject

class HazardOverviewPresenter: IHazardOverviewPresenter {

	private lateinit var view: IHazardOverviewView

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
		 DownloadHazardsTask(ctx, listView).execute()
	}

	override fun onIViewCreated(view: IHazardOverviewView, vararg initData: Any?) {
		this.view = view
	}

	override fun onIViewDestroyed() {
	}

	companion object {
		private val LOG_TAG: String = HazardOverviewPresenter::class.java.simpleName
	}
}