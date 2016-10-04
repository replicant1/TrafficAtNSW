package rod.bailey.trafficatnsw.cameras.details;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.io.InputStream;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.cameras.FavouriteCameraDialogPresenter;
import rod.bailey.trafficatnsw.cameras.TrafficCamera;
import rod.bailey.trafficatnsw.cameras.TrafficCameraDatabase;
import rod.bailey.trafficatnsw.util.MLog;

import static android.graphics.BitmapFactory.*;

public class TrafficCameraImageActivity extends Activity {

	private static final String TAG = TrafficCameraImageActivity.class
			.getSimpleName();
	private int cameraIndex;
	private String cameraStreet;
	private String cameraSuburb;
	private String cameraDescription;
	private String cameraUrl;

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		private final Context ctx;
		private ProgressDialog dialog;

		public DownloadImageTask(Context ctx) {
			assert ctx != null;
			this.ctx = ctx;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Bitmap frameBitmap = decodeResource(getResources(),
					R.drawable.photo_frame_filled);

			mainLayout.removeAllViews();
			captionedImage = new CaptionedImageComponent(
					TrafficCameraImageActivity.this, frameBitmap,
					(Bitmap) null, cameraDescription);
			mainLayout.addView(captionedImage, captionedImageLLP);

			dialog = new ProgressDialog(ctx);
			dialog.setMessage("Loading image...");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		protected Bitmap doInBackground(String... urls) {
			assert urls != null;
			assert urls.length == 1;

			MLog.i(TAG, "Up to doInBackground");

			String urlToLoad = urls[0];
			Bitmap result = null;

			try {
				InputStream stream = new java.net.URL(urlToLoad).openStream();
				result = BitmapFactory.decodeStream(stream);
			} catch (Exception e) {
				MLog.e(TAG, e.getMessage());
			}

			return result;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			if (result == null) {
				// Failed to load camera image - bad connection or just not
				// available on server.
				MLog.i(TAG,
						"Failed to load camera image - showing error dialog");
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setTitle("Could not load camera image");
				builder.setMessage("Tap the refresh icon at top right to try again.");
				builder.setPositiveButton("OK", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				Bitmap frameBitmap = decodeResource(getResources(),
						R.drawable.photo_frame_with_black_corners);
				mainLayout.removeAllViews();
				captionedImage = new CaptionedImageComponent(
						TrafficCameraImageActivity.this, frameBitmap, result,
						cameraDescription);
				mainLayout.addView(captionedImage, captionedImageLLP);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String strIndex = extras.getString("index");
		String strStreet = extras.getString("street");
		String strSuburb = extras.getString("suburb");
		String strDescription = extras.getString("description");
		String strUrl = extras.getString("url");
		String strFavourite = extras.getString("favourite");

		MLog.d(TAG, "index=" + strIndex);
		MLog.d(TAG, "stree=" + strStreet);
		MLog.d(TAG, "suburb=" + strSuburb);
		MLog.d(TAG, "description=" + strDescription);
		MLog.d(TAG, "url=" + strUrl);
		MLog.d(TAG, "favourite=" + strFavourite);

		assert strIndex != null;
		assert strStreet != null;
		assert strSuburb != null;
		assert strDescription != null;
		assert strUrl != null;
		assert strFavourite != null;

		cameraIndex = Integer.parseInt(strIndex);
		cameraStreet = strStreet.trim();
		cameraSuburb = strSuburb.trim();
		cameraDescription = strDescription.trim();
		cameraUrl = strUrl.trim();
		cameraFavourite = Boolean.parseBoolean(strFavourite);

		createUI();
	}

	// private final int IMAGE_VIEW_ID = 1000;
	//
	// private ImageView imageView;
	private MenuItem favouriteMenuItem;
	private boolean cameraFavourite;
	// private ImageView photoFrameImageView;
	private LinearLayout mainLayout;
	private CaptionedImageComponent captionedImage;
	private LinearLayout.LayoutParams captionedImageLLP;

	private void createUI() {
		mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setKeepScreenOn(true);
		mainLayout.setBackgroundColor(Color.BLACK);
		mainLayout.setGravity(Gravity.CENTER);

		captionedImage = new CaptionedImageComponent(this);
		captionedImageLLP = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainLayout.addView(captionedImage, captionedImageLLP);

		setContentView(mainLayout);

		// Put camera title in action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		TrafficCameraTitleView customView = new TrafficCameraTitleView(this,
				cameraStreet, cameraSuburb);
		ActionBar.LayoutParams customViewALP = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(customView, customViewALP);

		refresh();
	}

	public void refresh() {
		MLog.i(TAG, "Refreshing");
		DownloadImageTask task = new DownloadImageTask(this);
		task.execute(cameraUrl);
	}

	public void toggleFavourite() {
		MLog.i(TAG, "Toggle favourite");

		// Raise dialog
		final TrafficCamera camera = TrafficCameraDatabase.getInstance()
				.getCamera(cameraIndex);
		FavouriteCameraDialogPresenter pres = new FavouriteCameraDialogPresenter(
				camera);

		AlertDialog dialog = pres.build(this);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				MLog.i(TAG,
						"On dismiss listener, camera.isFavourite="
								+ camera.isFavourite());
				updateActionBarToReflectFavouriteStatus(camera.isFavourite());
			}
		});
		dialog.show();

		MLog.i(TAG, "After dialog.show");
	}

	private void updateActionBarToReflectFavouriteStatus(boolean isFavourite) {
		favouriteMenuItem.setIcon(isFavourite ? R.drawable.ic_action_important
				: R.drawable.ic_action_not_important);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.traffic_camera_image_options_menu, menu);
		favouriteMenuItem = menu.findItem(R.id.toggle_camera_favourite);

		MLog.i(TAG, "Found favouriteMenuItem=" + favouriteMenuItem);
		updateActionBarToReflectFavouriteStatus(cameraFavourite);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			switch (item.getItemId()) {
			case R.id.refresh_camera_image:
				refresh();
				break;
			case R.id.toggle_camera_favourite:
				toggleFavourite();
				break;
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				break;
			}
		}
		return true;
	}
}
