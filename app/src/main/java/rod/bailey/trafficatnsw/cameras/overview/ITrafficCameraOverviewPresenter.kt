package rod.bailey.trafficatnsw.cameras.overview

import android.content.Context
import android.support.annotation.StringRes
import rod.bailey.trafficatnsw.app.mvp.IPresenter
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage

/**
 * Created by rodbailey on 11/8/17.
 */
interface ITrafficCameraOverviewPresenter: IPresenter<ITrafficCameraOverviewView> {

	fun loadCamerasAsync(ctx: Context, listView: ListViewWithEmptyMessage)

	@StringRes
	fun getScreenTitleForMode(mode: TrafficCameraListMode?): Int

	@StringRes
	fun getEmptyMessageForMode(mode: TrafficCameraListMode?): Int
}