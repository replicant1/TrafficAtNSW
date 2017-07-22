package rod.bailey.trafficatnsw.hazard;

import android.content.Context;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter;
import rod.bailey.trafficatnsw.json.hazard.XHazard;
import rod.bailey.trafficatnsw.json.hazard.XRegion;

public class HazardDatabase {

	private static HazardDatabase singleton = new HazardDatabase();

	private static final String TAG = HazardDatabase.class.getSimpleName();

	public static HazardDatabase getInstance() {
		return singleton;
	}

	private IHazardFilter filter;

	private final Map<XRegion, List<XHazard>> unfilteredHazardsPerRegion = new HashMap<XRegion, List<XHazard>>();

	private final Map<XRegion, List<XHazard>> filteredHazardsPerRegion = new HashMap<XRegion, List<XHazard>>();

	private HazardDatabase() {
	}

	public synchronized void init(Context ctx, String hazardsJSON) {
		initUnfilteredHazards(ctx, hazardsJSON);

		if (filter != null) {
			refilter();
		}
	}

	public int unfilteredSize() {
		int result = 0;
		for (List<XHazard> unfilteredHazards : unfilteredHazardsPerRegion
				.values()) {
			result += unfilteredHazards.size();
		}
		return result;
	}

	public int filteredSize() {
		int result = 0;

		for (List<XHazard> filteredHazards : filteredHazardsPerRegion.values()) {
			result += filteredHazards.size();
		}

		return result;
	}

	private void initUnfilteredHazards(Context ctx, String hazardsJSON) {
		List<XHazard> allHazards = XHazard.Companion
				.createHazardsFromIncidentJsonContents(hazardsJSON);

		unfilteredHazardsPerRegion.clear();

		// Put hazards into unfiltered hazards map
		for (XHazard hazard : allHazards) {
			if (!hazard.isEnded()) {
				String regionStr = hazard.getRoads().get(0).getRegion();
				XRegion region = XRegion.valueOf(regionStr);

				// Add this hazard into the unfiltered map
				List<XHazard> hazardsPerRegion = null;

				if (unfilteredHazardsPerRegion.containsKey(region)) {
					hazardsPerRegion = unfilteredHazardsPerRegion.get(region);
				} else {
					hazardsPerRegion = new LinkedList<XHazard>();
					unfilteredHazardsPerRegion.put(region, hazardsPerRegion);
				}

				hazardsPerRegion.add(hazard);
			}
		}
	}

	public List<XHazard> getHazardsForRegion(XRegion region) {
		assert region != null;
		return filteredHazardsPerRegion.get(region);
	}

	public XHazard getHazard(int hazardId) {
		XHazard result = null;

		Collection<List<XHazard>> hazardCollections = unfilteredHazardsPerRegion
				.values();
		for (List<XHazard> hazardList : hazardCollections) {
			for (XHazard hazard : hazardList) {
				if (hazard.getHazardId() == hazardId) {
					result = hazard;
					break;
				}
			}
		}
		return result;
	}

	public void setFilter(IHazardFilter filter) {
		assert filter != null;
		this.filter = filter;
		refilter();
	}

	private void refilter() {
		filteredHazardsPerRegion.clear();

		for (XRegion region : unfilteredHazardsPerRegion.keySet()) {
			List<XHazard> unfilteredHazards = unfilteredHazardsPerRegion
					.get(region);

			if (!unfilteredHazards.isEmpty()) {
				List<XHazard> filteredHazards = new LinkedList<XHazard>();

				for (XHazard hazard : unfilteredHazards) {
					if (filter.admit(hazard)) {
						filteredHazards.add(hazard);
					}
				}

				if (!filteredHazards.isEmpty()) {
					filteredHazardsPerRegion.put(region, filteredHazards);
				}
			}
		}
	}

}
