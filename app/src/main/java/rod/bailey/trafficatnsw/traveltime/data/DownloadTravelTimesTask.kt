package rod.bailey.trafficatnsw.traveltime.data

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.common.service.IDataService
import rod.bailey.trafficatnsw.common.ui.IndeterminateProgressDialog
import rod.bailey.trafficatnsw.traveltime.ui.TravelTimesFragment
import rod.bailey.trafficatnsw.traveltime.ui.TravelTimesListAdapter
import rod.bailey.trafficatnsw.util.MLog
import javax.inject.Inject

class DownloadTravelTimesTask(
	private val ctx: Context,
	private val ttFrag: TravelTimesFragment,
	private val travelTimeConfig: MotorwayConfig?,
	private val mainLayout: ListViewWithEmptyMessage?) : AsyncTask<Void, Void, Boolean>() {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	@Inject
	lateinit var config: ConfigSingleton

	@Inject
	lateinit var dataService: IDataService

	private lateinit var dialog: IndeterminateProgressDialog

	override fun onPreExecute() {
		super.onPreExecute()
		dialog = IndeterminateProgressDialog(
			ctx, ctx.getString(R.string.tt_load_progress_msg, travelTimeConfig?.motorwayName))
		dialog.show()
	}

	override fun doInBackground(vararg params: Void): Boolean {
		var travelTimesLoadedOK: Boolean = true
		ttFrag.db?.removePropertyChangeListener(ttFrag)

		if (travelTimeConfig != null) {
			ttFrag.db = dataService.getMotorwayTravelTimes(travelTimeConfig)
		}

		if (ttFrag.db == null) {
			travelTimesLoadedOK = false
		} else {
			ttFrag.db?.addPropertyChangeListener(ttFrag)
		}

		return travelTimesLoadedOK
	}

	override fun onPostExecute(result: Boolean) {
		dialog.dismiss()

		if (result) {
			val dbVal: MotorwayTravelTimesStore = ttFrag.db ?: MotorwayTravelTimesStore(ctx, MotorwayConfigRegistry().m1Config)
			mainLayout?.setAdapter(TravelTimesListAdapter(dbVal))
		} else {
			// We don't call mainLayout.setAdapter, which means that the old (stale)
			// data will still remain visible.
			MLog.i(LOG_TAG, "Failed to load " + travelTimeConfig?.motorwayName + " travel times")
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