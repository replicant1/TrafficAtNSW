package rod.bailey.trafficatnsw.traveltime;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.traveltime.common.MotorwayTravelTimesDatabase;
import rod.bailey.trafficatnsw.traveltime.common.TravelTimesSingleton;
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig;
import rod.bailey.trafficatnsw.util.ConfigSingleton;
import rod.bailey.trafficatnsw.ui.predicate.InactiveTravelTimeEmptyMessagePredicate;
import rod.bailey.trafficatnsw.ui.view.ListWithEmptyMessage;
import rod.bailey.trafficatnsw.util.MLog;

public class TravelTimesFragment extends Fragment implements
		PropertyChangeListener {

	private static final String EMPTY_MESSAGE = "Travel Times for this motorway are unavailable at the moment.";

	public static final String ARG_MWAY_KEY = "MWAY";

	public static final String ARG_MWAY_VALUE_M1 = "M1";

	public static final String ARG_MWAY_VALUE_M2 = "M2";

	public static final String ARG_MWAY_VALUE_M4 = "M4";

	public static final String ARG_MWAY_VALUE_M7 = "M7";

	private static final String TAG = TravelTimesFragment.class.getSimpleName();

	private ListWithEmptyMessage mainLayout;

	/** Travel times for the motorway currently being displayed */
	private MotorwayTravelTimesDatabase db;

	/** Config for the motorway currently being display */
	private TravelTimeConfig travelTimeConfig;

	private void createUI() {
		MLog.INSTANCE.i(TAG, "Into TravelTimesFragment.createUI()");

		Context ctx = getActivity();
		mainLayout = new ListWithEmptyMessage(ctx, EMPTY_MESSAGE,
				new InactiveTravelTimeEmptyMessagePredicate());
		setHasOptionsMenu(true);
		
		getActivity().setTitle(travelTimeConfig.motorwayName + " Travel Times");
		
		refreshAsync();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();

		if (args != null) {
			if (args.containsKey(ARG_MWAY_KEY)) {
				String value = args.getString(ARG_MWAY_KEY);

				TravelTimesSingleton singleton = TravelTimesSingleton
						.getSingleton();
				singleton.init(getActivity());

				if (ARG_MWAY_VALUE_M1.equals(value)) {
					travelTimeConfig = singleton.getM1Config();
				} else if (ARG_MWAY_VALUE_M2.equals(value)) {
					travelTimeConfig = singleton.getM2Config();
				} else if (ARG_MWAY_VALUE_M4.equals(value)) {
					travelTimeConfig = singleton.getM4Config();
				} else if (ARG_MWAY_VALUE_M7.equals(value)) {
					travelTimeConfig = singleton.getM7Config();
				}
			}
		}
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
		inflater.inflate(R.menu.travel_times_options_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			if (item.getItemId() == R.id.refresh_travel_times) {
				refreshAsync();
			}
		}
		return true;
	}

	private void refreshAsync() {
		MLog.INSTANCE.i(TAG, "Refreshing travel times");

		DownloadTravelTimesTask task = new DownloadTravelTimesTask(
				getActivity());
		task.execute();
	}

	private class DownloadTravelTimesTask extends
			AsyncTask<Void, Void, Boolean> {
		private final Context ctx;
		private ProgressDialog dialog;

		public DownloadTravelTimesTask(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(ctx);
			dialog.setMessage("Loading " + travelTimeConfig.motorwayName
					+ " travel times...");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean travelTimesLoadedOK = Boolean.TRUE;

			// Download the JSON file - local or remote
			if (db != null) {
				db.removePropertyChangeListener(TravelTimesFragment.this);
			}

			if (ConfigSingleton.getInstance()
					.loadTravelTimesFromLocalJSONFiles()) {
				db = TravelTimesSingleton
						.getSingleton()
						.loadTravelTimesFromLocalJSONFile(ctx, travelTimeConfig);
			} else {
				db = TravelTimesSingleton.getSingleton()
						.loadTravelTimesFromRemoteJSONFile(ctx,
								travelTimeConfig);
			}

			if (db == null) {
				travelTimesLoadedOK = Boolean.FALSE;
			} else {
				db.addPropertyChangeListener(TravelTimesFragment.this);
			}

			MLog.INSTANCE.i(TAG, "Result of loading is database " + db);

			return travelTimesLoadedOK;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			if (result == Boolean.TRUE) {
				mainLayout.setAdapter(new TravelTimesListAdapter(db));
			} else {
				// We don't all mainLayout.setAdapter, which means that the old (stale)
				// data will still remain visible.
				MLog.INSTANCE.i(TAG, "Failed to load " + travelTimeConfig.motorwayName + " travel times - showing error dialog");
				AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
				builder.setTitle(String.format("No Travel Times for %s", travelTimeConfig.motorwayName));
				builder.setMessage("Tap the refresh icon at top right to try again.");
				builder.setPositiveButton("OK", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		MLog.INSTANCE.i(TAG,
				"TravelTimesFragment gets notice that property "
						+ event.getPropertyName() + " has changed");
		if (event.getPropertyName().equals(
				MotorwayTravelTimesDatabase.PROPERTY_TOTAL_TRAVEL_TIME)) {
			if (db != null) {
				mainLayout.setAdapter(new TravelTimesListAdapter(db));
			}
		}
	}
}