package rod.bailey.trafficatnsw.hazard;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.hazard.filter.AdmitRegionalHazardFilter;
import rod.bailey.trafficatnsw.hazard.filter.AdmitSydneyHazardFilter;
import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter;
import rod.bailey.trafficatnsw.util.ConfigSingleton;
import rod.bailey.trafficatnsw.util.EmptyListEmptyMessagePredicate;
import rod.bailey.trafficatnsw.util.ListWithEmptyMessage;
import rod.bailey.trafficatnsw.util.MLog;

public class HazardsFragment extends Fragment {

	private static final String TITLE_REGIONAL_NSW = "Incidents in Regional NSW";

	private static final String TITLE_SYDNEY = "Incidents in Sydney";

	public enum HazardsListMode {
		REGIONAL("Regional Incidents", new AdmitRegionalHazardFilter()), //
		SYDNEY("Sydney Incidents", new AdmitSydneyHazardFilter());

		private final String displayName;
		private final IHazardFilter filter;

		private HazardsListMode(String displayName, IHazardFilter filter) {
			this.displayName = displayName;
			this.filter = filter;
		}

		public String getDisplayName() {
			return displayName;
		}

		public IHazardFilter getFilter() {
			return filter;
		}
	}

	/** Key for the sole argument passed to this argument. */
	public static final String ARG_MODE_KEY = "MODE";

	/**
	 * Value for ARG_MODE_KEY that indicates the list should contain any hazards
	 * in the Regional regions (REGIONAL_NORTH, REGIONAL_SOUTH, REGIONAL_WEST).
	 */
	public static final int ARG_MODE_VALUE_REGIONAL = 1;

	/**
	 * Value for ARG_MODE_KEY that indictes the list should contain ony hazards
	 * in the Sydney regions.(SYD_NORTH,SYD_SOUTH,SYD_METRO)
	 */
	public static final int ARG_MODE_VALUE_SYDNEY = 0;

	private static final String TAG = HazardsFragment.class.getSimpleName();

	private HazardsListMode mode;

	private ListWithEmptyMessage mainLayout;

	private static final String SYDNEY_EMPTY_MESSAGE = "There are no incidents in Sydney.\n\nTap the refresh button above to check again.";

	private static final String REGIONAL_NSW_EMPTY_MESSAGE = "There are no incidents in Regional NSW.\n\nTap the refresh button above\nto check again.";

	private void createUI() {
		MLog.i(TAG,
				"Into HazardsFragment.createUI. At this time, mode="
						+ mode.name());

		assert mode != null;

		Context ctx = getActivity();
		mainLayout = new ListWithEmptyMessage(ctx,
				mode == HazardsListMode.SYDNEY ? SYDNEY_EMPTY_MESSAGE
						: REGIONAL_NSW_EMPTY_MESSAGE,
				new EmptyListEmptyMessagePredicate());
		setHasOptionsMenu(true);

		// Set title that appears in action bar
		getActivity().setTitle(
				mode == HazardsListMode.SYDNEY ? TITLE_SYDNEY
						: TITLE_REGIONAL_NSW);
		refreshAsync();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MLog.d(TAG, "Into HazardsFragment.onCreate");

		Bundle args = getArguments();
		HazardsListMode newmode = HazardsListMode.SYDNEY;

		if (args != null) {
			if (args.containsKey(ARG_MODE_KEY)) {
				int modeKey = args.getInt(ARG_MODE_KEY);

				switch (modeKey) {
				case ARG_MODE_VALUE_REGIONAL:
					newmode = HazardsListMode.REGIONAL;
					break;

				case ARG_MODE_VALUE_SYDNEY:
					newmode = HazardsListMode.SYDNEY;
				}
			}
		} else {
			MLog.i(TAG, "args was null");
		}

		MLog.i(TAG, "Setting hazard list mode to " + newmode.name());
		HazardDatabase.getInstance().setFilter(newmode.getFilter());
		mode = newmode;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		createUI();
		refreshAsync();
		return mainLayout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.hazard_list_options_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			if (item.getItemId() == R.id.refresh_hazard_list) {
				refreshAsync();
			}
		}
		return true;
	}

	private void refreshAsync() {
		MLog.i(TAG, "Refresh hazard list");

		DownloadHazardFileTask task = new DownloadHazardFileTask(getActivity());
		task.execute();
	}

	private class DownloadHazardFileTask extends AsyncTask<Void, Void, Boolean> {
		private final Context ctx;
		private ProgressDialog dialog;

		public DownloadHazardFileTask(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(ctx);
			dialog.setMessage("Loading incidents...");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// Download the JSON file. The URL is in params[0].
			BufferedReader bufferedReader = null;
			Boolean hazardsLoadedOK = Boolean.TRUE;

			try {
				if (ConfigSingleton.getInstance()
						.loadIncidentsFromLocalJSONFile()) {
					String assetFileName = ConfigSingleton.getInstance()
							.localIncidentsJSONFile();
					InputStream input = ctx.getAssets().open(assetFileName);
					int size = input.available();
					byte[] buffer = new byte[size];
					input.read(buffer);
					input.close();
					String text = new String(buffer);

					HazardDatabase.getInstance().init(ctx, text);
				} else {
					URL url = new URL(ConfigSingleton.getInstance()
							.remoteIncidentsJSONFile());
					InputStreamReader instreamReader = new InputStreamReader(
							url.openStream());
					bufferedReader = new BufferedReader(instreamReader);

					String line = null;
					StringBuffer lineBuffer = new StringBuffer();

					while ((line = bufferedReader.readLine()) != null) {
						lineBuffer.append(line);
					}

					HazardDatabase.getInstance().init(ctx,
							lineBuffer.toString());
					bufferedReader.close();
				}
			} catch (Exception e) {
				MLog.w(TAG, "Failed to load hazards JSON", e);
				hazardsLoadedOK = Boolean.FALSE;
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
					}
				}
			}

			return hazardsLoadedOK;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// Force the listView to refresh its data

			if (dialog != null) {
				dialog.dismiss();
			}

			if (result == Boolean.TRUE) {
				mainLayout.setAdapter(new HazardListAdapter(ctx, mode
						.getFilter()));
			} else {
				// We don't all mainLayout.setAdapter, which means that the old
				// (stale)
				// data will still remain visible.
				MLog.i(TAG,
						"Failed to load camera image - showing error dialog");
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setTitle("No Incident Data");
				builder.setCancelable(true);
				builder.setMessage("Tap the refresh icon at top right to try again.");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
}
