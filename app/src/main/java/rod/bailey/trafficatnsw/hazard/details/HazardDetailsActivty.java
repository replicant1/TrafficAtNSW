package rod.bailey.trafficatnsw.hazard.details;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.hazard.HazardDatabase;
import rod.bailey.trafficatnsw.hazard.map.ShowHazardOnMapActivity;
import rod.bailey.trafficatnsw.json.hazard.XHazard;
import rod.bailey.trafficatnsw.ui.FooterCancellingListView;
import rod.bailey.trafficatnsw.util.MLog;

/**
 * List has the following sections:
 * <ol>
 * <li>Title
 * <li>WHEN
 * <li>GENERAL
 * <li>OTHER ADVICE
 * </ol>
 * 
 * @author rodbailey
 * 
 */
public class HazardDetailsActivty extends Activity {
	
	private static final String TAG = HazardDetailsActivty.class
			.getSimpleName();
	
	private XHazard hazard;

	private void createUI() {
		FooterCancellingListView listView = new FooterCancellingListView(this);
		listView.setAdapter(new HazardDetailsListAdapter(hazard));
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEnabled(true);
		
		setContentView(listView);
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MLog.i("HI", "HazardDetailsActivity is being created ");
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		int hazardId = extras.getInt("hazardId");

		MLog.i(TAG, "Showing details of hazard id " + hazardId);

		hazard = HazardDatabase.Companion.getInstance().getUnfilteredHazard(hazardId);
		createUI();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.hazard_details_options_menu, menu);
		MenuItem showOnMapMenuItem = menu.findItem(R.id.show_on_map);
		return super.onCreateOptionsMenu(menu);
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			if (item.getItemId() == R.id.show_on_map) {
				// Create intent then launch an activity with it.
				// One arg = hazard id
				Context ctx = this;
				
				MLog.i(TAG, "Launching ShowHazardOnMapActivity");
				
				Intent intent = new Intent(ctx,
						ShowHazardOnMapActivity.class);
				intent.putExtra("hazardId", hazard.getHazardId());
				ctx.startActivity(intent);
			} else if (item.getItemId() == android.R.id.home){
				NavUtils.navigateUpFromSameTask(this);
			}
		}
		return true;
	}

}
