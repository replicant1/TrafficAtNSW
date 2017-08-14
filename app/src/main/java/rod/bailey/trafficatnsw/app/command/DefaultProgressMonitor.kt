package rod.bailey.trafficatnsw.app.command

import android.content.Context
import rod.bailey.trafficatnsw.common.ui.IndeterminateProgressDialog

/**
 * An indeterminate progress monitor
 */
class DefaultProgressMonitor(val ctx: Context, val message: String): ICommandProgressMonitor {

	private var dialog: IndeterminateProgressDialog? = null

	override fun startProgress() {
		System.out.println("** DefaultProgressMonitor.startProgress **")
		dialog = IndeterminateProgressDialog(ctx, message)
		dialog?.show()
	}

	override fun stopProgress() {
		System.out.println("** DefaultProgressMonitor.stopProgress **")
		dialog?.dismiss()
	}
}