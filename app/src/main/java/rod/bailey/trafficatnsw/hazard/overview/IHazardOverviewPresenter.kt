package rod.bailey.trafficatnsw.hazard.overview

import android.content.Context
import android.support.annotation.StringRes
import rod.bailey.trafficatnsw.app.mvp.IPresenter
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage

/**
 * Implemented by any party presenting a Hazard Overview screen.
 */
interface IHazardOverviewPresenter : IPresenter<IHazardOverviewView> {

	/**
	 * Async loads all hazards from the server and populates the given list view with them.
	 * List view should already have been setup with the appropriate filter.
	 */
	fun loadHazardsAsync(ctx: Context, listView: ListViewWithEmptyMessage);

	/**
	 * @return The title for the screen, depending on mode
	 */
	@StringRes
	fun getScreenTitleForMode(mode: HazardOverviewMode?): Int

	/**
	 * @return The string to display in place of the hazard list when there are no hazards
	 */
	@StringRes
	fun getEmptyMessageForMode(mode: HazardOverviewMode?): Int
}