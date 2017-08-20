package rod.bailey.trafficatnsw.cameras.image

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.data.XCamera
import rod.bailey.trafficatnsw.util.MLog

/**
 * Presents the confirmation dialog shown when the user elects to add or remove
 * the present camera from their set of extraFavourite cameras.
 */
class FavouriteCameraDialogPresenter(private val camera: XCamera) {

	/**
	 * Listens for button taps on the radio buttons
	 */
	private inner class AlertsDialogListener : DialogInterface.OnClickListener {
		override fun onClick(arg0: DialogInterface, index: Int) {
			when (index) {
				DialogInterface.BUTTON_POSITIVE -> {
					MLog.i(LOG_TAG, "Toggle extraFavourite state of camera " + camera.id)
					camera.isFavourite = !camera.isFavourite
				}
				DialogInterface.BUTTON_NEGATIVE -> {
					// Empty
				}
			}
		}
	}

	init {
		MLog.i(LOG_TAG, "IPresenter created for camera with extraFavourite status " + camera.isFavourite)
	}

	fun build(ctx: Context): AlertDialog {
		val listener = AlertsDialogListener()
		val builder = AlertDialog.Builder(ctx)
		builder.setTitle(ctx.getString(R.string.camera_favourite_confirmation_dialog_title))
		builder.setPositiveButton(
			if (camera.isFavourite)
				ctx.getString(R.string.camera_favourite_confirmation_dialog_remove_button)
			else
				ctx.getString(R.string.camera_favourite_confirmation_dialog_add_button), listener)
		builder.setNegativeButton(ctx.getString(R.string.negative_dialog_button), listener)
		builder.setMessage(
			if (camera.isFavourite)
				ctx.getString(R.string.camera_unfavourite_confirmation_dialog_msg)
			else
				ctx.getString(R.string.camera_favourite_confirmation_dialog_msg))
		val alert = builder.create()
		alert.setCanceledOnTouchOutside(true)

		return alert
	}

	companion object {
		private val LOG_TAG = FavouriteCameraDialogPresenter::class.java.simpleName
	}
}
