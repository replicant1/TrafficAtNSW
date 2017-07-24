package rod.bailey.trafficatnsw.cameras

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import rod.bailey.trafficatnsw.util.MLog

class FavouriteCameraDialogPresenter(private val camera: TrafficCamera) {
	/** Listens for button taps on the radio buttons  */
	private inner class AlertsDialogListener : DialogInterface.OnClickListener {
		override fun onClick(arg0: DialogInterface, index: Int) {
			when (index) {
				DialogInterface.BUTTON_POSITIVE -> {
					MLog.i(TAG,
						   "*** About to toggle favourite state of camera " + camera.index)
					camera.isFavourite = !camera.isFavourite
				}
				DialogInterface.BUTTON_NEGATIVE -> {
				}
			}
		}
	}

	init {
		MLog.i(TAG,
			   "Presenter created for a camera whose favourite status is " + camera.isFavourite)
	}

	fun build(ctx: Context?): AlertDialog {
		val listener = AlertsDialogListener()
		val builder = AlertDialog.Builder(ctx)
		builder.setTitle("Favourite")
		builder.setPositiveButton(
			if (camera.isFavourite)
				"Remove from Favourites"
			else
				"Make a Favourite", listener)
		builder.setNegativeButton("No", listener)
		builder.setMessage(if (camera.isFavourite)
							   "Do you want to remove this camera from your list of favourite cameras?"
						   else
							   "Do you want to add this camera to your list of favourite cameras?")
		val alert = builder.create()
		alert.setCanceledOnTouchOutside(true)

		return alert
	}

	companion object {
		private val TAG = FavouriteCameraDialogPresenter::class.java
			.simpleName
	}
}
