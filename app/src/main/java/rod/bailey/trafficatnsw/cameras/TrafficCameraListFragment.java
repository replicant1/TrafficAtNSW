package rod.bailey.trafficatnsw.cameras;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import rod.bailey.trafficatnsw.cameras.filter.AdmitFavouritesTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitRegionalTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitSydneyTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter;
import rod.bailey.trafficatnsw.ui.predicate.EmptyListEmptyMessagePredicate;
import rod.bailey.trafficatnsw.ui.ListWithEmptyMessage;
import rod.bailey.trafficatnsw.util.MLog;

public class TrafficCameraListFragment extends Fragment implements
		PropertyChangeListener {

	private static final String EMPTY_MESSAGE = "You have no favourite cameras.\n\n"
			+ "To make a camera one of your favourites, view the camera image and "
			+ "tap the star at the top right of the screen.";

	public enum TrafficCameraListMode {
		FAVOURITES("Favourites", "Favourite Cameras", new AdmitFavouritesTrafficCameraFilter()), //
		REGIONAL("Regional", "Cameras in Regional NSW", new AdmitRegionalTrafficCameraFilter()), //
		SYDNEY("Sydney", "Cameras in Sydney", new AdmitSydneyTrafficCameraFilter());

		private final String displayName;
		private final ITrafficCameraFilter filter;
		private final String actionBarTitle;
		

		private TrafficCameraListMode(String displayname,
				String actionBarTitle, ITrafficCameraFilter filter) {
			this.displayName = displayname;
			this.filter = filter;
			this.actionBarTitle = actionBarTitle;
		}

		public ITrafficCameraFilter getFilter() {
			return filter;
		}

		public String getName() {
			return displayName;
		}
		
		public String getActionBarTitle() {
			return actionBarTitle;
		}
	}

	/** Key for the sole argument passed to this fragment. */
	public static final String ARG_MODE_KEY = "MODE";

	/**
	 * Value for ARG_HAZARDS_FRAGMENT_MODE that indicates the list should contain ALL traffic
	 * cameras
	 */
	public static final int ARG_MODE_VALUE_ALL = 0;

	public static final int ARG_MODE_VALUE_SYDNEY = 1;

	public static final int ARG_MODE_VALUE_REGIONAL = 2;

	/**
	 * Value for ARG_HAZARDS_FRAGMENT_MODE that indicates the list should contain only those
	 * traffic cameras marked as favourites.
	 */
	public static final int ARG_MODE_VALUE_FAVOURITES = 3;

	/** Tag for logging */
	private static final String TAG = TrafficCameraListFragment.class
			.getSimpleName();

	/** List of traffic cameras, divided into sections by XRegion */
	// private ListView listView;

	/** Top level layout has list on top, DataLicenceView at bottom */
	private ListWithEmptyMessage mainLayout;

	/**
	 * Mode of display = what cameras appear in list. Derived from the value
	 * passed into this fragment for the ARG_HAZARDS_FRAGMENT_MODE argument.
	 */
	private TrafficCameraListMode mode = TrafficCameraListMode.SYDNEY;

	private void createUI() {
		Log.i(TAG, "Into TrafficCameraListFragment.createUI");

		Context ctx = getActivity();

		mainLayout = new ListWithEmptyMessage(ctx, EMPTY_MESSAGE,
				new EmptyListEmptyMessagePredicate());
		TrafficCameraListAdapter adapter = new TrafficCameraListAdapter(
				mode.getFilter());
		mainLayout.setAdapter(adapter);
		
		getActivity().setTitle(mode.getActionBarTitle());
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		Bundle args = getArguments();
		TrafficCameraListMode newmode = TrafficCameraListMode.SYDNEY;
		if (args != null) {
			if (args.containsKey(ARG_MODE_KEY)) {
				int modeKey = args.getInt(ARG_MODE_KEY);

				switch (modeKey) {
				case ARG_MODE_VALUE_FAVOURITES:
					newmode = TrafficCameraListMode.FAVOURITES;
					break;
				case ARG_MODE_VALUE_REGIONAL:
					newmode = TrafficCameraListMode.REGIONAL;
					break;
				case ARG_MODE_VALUE_SYDNEY:
					newmode = TrafficCameraListMode.SYDNEY;
					break;
				}
			}
		} else {
			MLog.INSTANCE.i(TAG, "args was null");
		}

		MLog.INSTANCE.i(TAG, "Setting camera mode to " + newmode.name());
		mode = newmode;

		// Initialize TrafficCameraDatabase
		TrafficCameraDatabase db = TrafficCameraDatabase.getInstance();
		db.init(getActivity());

		MLog.INSTANCE.i(TAG, "TCLFragment add self as listener to TCDb " + db.hashCode());
		db.addPropertyChangeListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		createUI();
		return mainLayout;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (TrafficCameraDatabase.PROPERTY_FAVOURITE_SET.equals(event
				.getPropertyName())) {
			if ((mainLayout != null) && (mode != null)) {
				// TODO: WOuld be nice to save list's scroll pos here and them
				// restore after setting the new adapter.
				TrafficCameraListAdapter adapter = new TrafficCameraListAdapter(
						mode.filter);
				mainLayout.setAdapter(adapter);
			}
		}
	}

}
