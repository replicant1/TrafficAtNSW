package rod.bailey.trafficatnsw.hazard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter;
import rod.bailey.trafficatnsw.json.hazard.Hazard;
import rod.bailey.trafficatnsw.json.hazard.Region;
import rod.bailey.trafficatnsw.util.ListHeadingView;
import rod.bailey.trafficatnsw.util.MLog;

public class HazardListAdapter extends BaseAdapter implements ListAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = HazardListAdapter.class.getSimpleName();

	private HazardDatabase db;

	private final ArrayList<Object> listData = new ArrayList<Object>();

	private IHazardFilter filter;

	public HazardListAdapter(Context ctx, IHazardFilter filter) {
		assert ctx != null;
		assert filter != null;

		MLog.d(TAG, "Into HazardListAapter c'tor with filter=" + filter);

		this.filter = filter;

		db = HazardDatabase.getInstance();
		primeListDataFromHazardDatabase();
	}

	public void primeListDataFromHazardDatabase() {
		MLog.d(TAG, "About to collate the listData from HazardDatase contents");

		for (Region region : sortedHazardRegions()) {
			listData.add(region);
			List<Hazard> hazards = db.getHazardsForRegion(region);
			Collections.sort(hazards);
			listData.addAll(hazards);
		}
	}

	private View createHazardListItem(Context ctx, Hazard hazard) {
		HazardListItemView view = new HazardListItemView(ctx, hazard, true, true);
		return view;
	}

	private View createHeading(Context ctx, Region region) {
		ListHeadingView heading = new ListHeadingView(ctx,
				region.getDescription(), false);
		return heading;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = null;

		Object listItem = listData.get(position);

		if (listItem instanceof Region) {
			result = createHeading(parent.getContext(), (Region) listItem);
		} else {
			result = createHazardListItem(parent.getContext(),
					(Hazard) listItem);
		}

		return result;
	}

	@Override
	public boolean isEnabled(int position) {
		Object dataObj = listData.get(position);
		return (dataObj instanceof Hazard);
	}

	private List<Region> sortedHazardRegions() {

		List<Region> sortedRegions = new LinkedList<Region>();

		for (Region region : Region.values()) {
			List<Hazard> hazardsForRegion = db.getHazardsForRegion(region);

			if ((hazardsForRegion != null) && (!hazardsForRegion.isEmpty())) {
				sortedRegions.add(region);
			}
		}

		return sortedRegions;
	}

}
