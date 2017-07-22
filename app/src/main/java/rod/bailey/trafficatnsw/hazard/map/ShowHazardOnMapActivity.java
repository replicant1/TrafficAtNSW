package rod.bailey.trafficatnsw.hazard.map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.hazard.HazardDatabase;
import rod.bailey.trafficatnsw.json.hazard.XHazard;

import static android.view.ViewGroup.LayoutParams.*;
import static android.widget.RelativeLayout.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class ShowHazardOnMapActivity extends Activity {

	private static final int CONTENT_FRAME_ID = 4001;
	
	@SuppressWarnings("unused")
	private static final String TAG = ShowHazardOnMapActivity.class
			.getSimpleName();
	
	private XHazard hazard;
	
	private MapFragment fragment;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home){
			NavUtils.navigateUpFromSameTask(this);
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		int hazardId = extras.getInt("hazardId");
		hazard = HazardDatabase.getInstance().getHazard(hazardId);

		createUI();
	}

	private void createUI() {
		RelativeLayout mainLayout = new RelativeLayout(this);
		mainLayout.setId(R.id.content_frame);

		MarginLayoutParams mainLayoutMLP = new MarginLayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		int marginPx = dp2Px(this, 5);
		mainLayoutMLP.setMargins(marginPx, marginPx, marginPx, marginPx);
		RelativeLayout.LayoutParams mainLayoutRLP = new RelativeLayout.LayoutParams(
				mainLayoutMLP);
		mainLayoutRLP.addRule(ALIGN_PARENT_BOTTOM);
		mainLayoutRLP.addRule(ALIGN_PARENT_TOP);
		mainLayoutRLP.addRule(ALIGN_PARENT_LEFT);
		mainLayoutRLP.addRule(ALIGN_PARENT_RIGHT);
		mainLayout.setLayoutParams(mainLayoutRLP);

		fragment = new MapFragment();

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();

		setContentView(mainLayout);
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		GoogleMap googleMap = null; // fragment.getMap(); NOw getMzapAsync
		googleMap.clear();

		LatLng latlng = new LatLng(hazard.getLatlng().getLatitude(), hazard
				.getLatlng().getLongitude());
		MarkerOptions markerOptions = new MarkerOptions().position(latlng)
				.title(hazard.getHeadline()).draggable(false).flat(false);

		googleMap.addMarker(markerOptions);
		googleMap.setBuildingsEnabled(false);
		googleMap.setIndoorEnabled(false);
		googleMap.setTrafficEnabled(true);

		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 18);
		googleMap.moveCamera(update);
	}
}
