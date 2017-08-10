package rod.bailey.trafficatnsw.traveltime.data

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.service.IDataService
import rod.bailey.trafficatnsw.common.ui.IndeterminateProgressDialog
import rod.bailey.trafficatnsw.traveltime.overview.ITravelTimesOverviewPresenter
import rod.bailey.trafficatnsw.util.MLog
import javax.inject.Inject

class DownloadTravelTimesTask(
	private val ctx: Context,
	private val overviewPresenter: ITravelTimesOverviewPresenter,
	private val motorwayConfig: MotorwayConfig) : AsyncTask<Void, Void, Boolean>() {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var dataService: IDataService

	private lateinit var dialog: IndeterminateProgressDialog

	private lateinit var loadedData: MotorwayTravelTimesStore

	override fun onPreExecute() {
		super.onPreExecute()
		dialog = IndeterminateProgressDialog(
			ctx, ctx.getString(R.string.tt_load_progress_msg, motorwayConfig?.motorwayName))
		dialog.show()
	}

	override fun doInBackground(vararg params: Void): Boolean {
		var dataLoadedOK: Boolean = true
		val newdb: MotorwayTravelTimesStore? = dataService.getMotorwayTravelTimes(motorwayConfig)

		if (newdb == null) {
			loadedData = MotorwayTravelTimesStore(ctx, motorwayConfig);
			dataLoadedOK = false
		} else {
			loadedData = newdb
		}

		return dataLoadedOK
	}

	override fun onPostExecute(result: Boolean) {
		Log.d(LOG_TAG, "Into onPostExecute with result ${result}")
		dialog.dismiss()

		if (result) {
			Log.d(LOG_TAG, "loadedData.size=${loadedData.getTravelTimes().size}")
			overviewPresenter.onMotorwayDataLoaded(loadedData)
		} else {
			// We don't refresh the list, which means that the old (stale)
			// data will still remain visible.
			MLog.i(LOG_TAG, "Failed to load " + motorwayConfig.motorwayName + " travel times")
			val builder = AlertDialog.Builder(ctx)
			builder.setTitle(ctx.getString(R.string.tt_download_dialog_title))
			builder.setMessage(ctx.getString(R.string.tt_download_dialog_msg))
			builder.setPositiveButton(ctx.getString(R.string.positive_dialog_button_alt), null)
			val dialog = builder.create()
			dialog.show()
		}
	}

	companion object {
		private val LOG_TAG: String = DownloadTravelTimesTask::class.java.simpleName
	}

}