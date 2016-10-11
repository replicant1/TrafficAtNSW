package rod.bailey.trafficatnsw.cameras;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter;
import rod.bailey.trafficatnsw.json.hazard.XRegion;
import rod.bailey.trafficatnsw.util.ListHeadingView;

public class TrafficCameraListAdapter extends BaseAdapter implements
		ListAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = TrafficCameraListAdapter.class
			.getSimpleName();

	private final TrafficCameraDatabase db;

	private final ArrayList<Object> listData = new ArrayList<Object>();

	public TrafficCameraListAdapter(ITrafficCameraFilter filter) {
		assert filter != null;

		db = TrafficCameraDatabase.getInstance();
		db.setFilter(filter);

		// Linearize the info in the TrafficCameraDatabase. The resulting list
		// has two sorts of entries. A XRegion instance if for the beginning of a
		// region section, a TrafficCamera instance if a camera within a
		// region section.
		for (XRegion region : sortedCameraRegions()) {
			listData.add(region);
			List<TrafficCamera> cameras = db.getCamerasForRegion(region);
			Collections.sort(cameras);
			listData.addAll(cameras);
		}
	}

	@Override
	public boolean isEnabled(int position) {
		Object dataObj = listData.get(position);
		return dataObj instanceof TrafficCamera;
	}

	private View createHeading(XRegion region, ViewGroup parent) {
		ListHeadingView heading = new ListHeadingView(parent.getContext(),
				region.getDescription(), false);
		return heading;
	}

	private TrafficCameraListItemView createTrafficCameraListItem(Context ctx,
			TrafficCamera camera) {
		TrafficCameraListItemView item = new TrafficCameraListItemView(ctx,
				camera, camera.isFavourite());
		item.setFocusable(true);
		return item;
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

		if (listItem instanceof XRegion) {
			result = createHeading((XRegion) listItem, parent);
		} else {
			result = createTrafficCameraListItem(parent.getContext(),
					(TrafficCamera) listItem);
		}

		return result;
	}

	private List<XRegion> sortedCameraRegions() {
		List<XRegion> sortedRegions = new LinkedList<XRegion>();

		for (XRegion region : XRegion.values()) {
			if ((db.getCamerasForRegion(region) != null)
					&& (!db.getCamerasForRegion(region).isEmpty())) {
				sortedRegions.add(region);
			}
		}

		return sortedRegions;
	}
}
