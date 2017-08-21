package rod.bailey.trafficatnsw.app.command

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log

/**
 * Handles an error by producing a simple alert dialog containing a standard error message.
 */
class DefaultErrorHandler(val ctx: Context, val message: String) : ICommandErrorHandler {

	override fun onError(ex: Throwable) {
		Log.w(LOG_TAG, ex)

		AlertDialog.Builder(ctx)
			.setTitle("Error")
			.setMessage(message)
			.setCancelable(true)
			.setPositiveButton("OK", { dialog, _ -> dialog.cancel()})
			.create()
			.show()
	}

	companion object {
		private val LOG_TAG = DefaultErrorHandler::class.java.simpleName
	}
}