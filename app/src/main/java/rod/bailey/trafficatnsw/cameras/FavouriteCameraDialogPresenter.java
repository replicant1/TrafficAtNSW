package rod.bailey.trafficatnsw.cameras;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import rod.bailey.trafficatnsw.util.MLog;

public class FavouriteCameraDialogPresenter {

	private static final String TAG = FavouriteCameraDialogPresenter.class
			.getSimpleName();

	/** Listens for button taps on the radio buttons */
	private final class AlertsDialogListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int index) {

			switch (index) {
			case DialogInterface.BUTTON_POSITIVE:
				MLog.INSTANCE.i(TAG, "*** About to toggle favourite state of camera " + camera.getIndex());
				camera.setFavourite(!camera.isFavourite());
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	}

	private final TrafficCamera camera;

	public FavouriteCameraDialogPresenter(TrafficCamera camera) {
		assert camera != null;
		this.camera = camera;
		MLog.INSTANCE.i(TAG, "Presenter created for a camera whose favourite status is "
				+ camera.isFavourite());
	}

	public AlertDialog build(Context ctx) {
		assert ctx != null;

		AlertsDialogListener listener = new AlertsDialogListener();

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Favourite");
		builder.setPositiveButton(
				camera.isFavourite() ? "Remove from Favourites"
						: "Make a Favourite", listener);
		builder.setNegativeButton("No", listener);
		builder.setMessage(camera.isFavourite() ? "Do you want to remove this camera from your list of favourite cameras?"
				: "Do you want to add this camera to your list of favourite cameras?");

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(true);

		return alert;
	}
}
