package rod.bailey.trafficatnsw.hazard;

import android.content.Context;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rod.bailey.trafficatnsw.hazard.filter.IHazardFilter;
import rod.bailey.trafficatnsw.json.hazard.Hazard;
import rod.bailey.trafficatnsw.json.hazard.Region;

public class HazardDatabase {

	private static HazardDatabase singleton = new HazardDatabase();

	private static final String TAG = HazardDatabase.class.getSimpleName();

	public static HazardDatabase getInstance() {
		return singleton;
	}

	private IHazardFilter filter;

	private final Map<Region, List<Hazard>> unfilteredHazardsPerRegion = new HashMap<Region, List<Hazard>>();

	private final Map<Region, List<Hazard>> filteredHazardsPerRegion = new HashMap<Region, List<Hazard>>();

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
		for (List<Hazard> unfilteredHazards : unfilteredHazardsPerRegion
				.values()) {
			result += unfilteredHazards.size();
		}
		return result;
	}

	public int filteredSize() {
		int result = 0;

		for (List<Hazard> filteredHazards : filteredHazardsPerRegion.values()) {
			result += filteredHazards.size();
		}

		return result;
	}

	private void initUnfilteredHazards(Context ctx, String hazardsJSON) {
		List<Hazard> allHazards = Hazard
				.createHazardsFromIncidentJsonContents(hazardsJSON);

		unfilteredHazardsPerRegion.clear();

		// Put hazards into unfiltered hazards map
		for (Hazard hazard : allHazards) {
			if (!hazard.isEnded()) {
				String regionStr = hazard.getRoads().get(0).getRegion();
				Region region = Region.valueOf(regionStr);

				// Add this hazard into the unfiltered map
				List<Hazard> hazardsPerRegion = null;

				if (unfilteredHazardsPerRegion.containsKey(region)) {
					hazardsPerRegion = unfilteredHazardsPerRegion.get(region);
				} else {
					hazardsPerRegion = new LinkedList<Hazard>();
					unfilteredHazardsPerRegion.put(region, hazardsPerRegion);
				}

				hazardsPerRegion.add(hazard);
			}
		}
	}

	public List<Hazard> getHazardsForRegion(Region region) {
		assert region != null;
		return filteredHazardsPerRegion.get(region);
	}

	public Hazard getHazard(int hazardId) {
		Hazard result = null;

		Collection<List<Hazard>> hazardCollections = unfilteredHazardsPerRegion
				.values();
		for (List<Hazard> hazardList : hazardCollections) {
			for (Hazard hazard : hazardList) {
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

		for (Region region : unfilteredHazardsPerRegion.keySet()) {
			List<Hazard> unfilteredHazards = unfilteredHazardsPerRegion
					.get(region);

			if (!unfilteredHazards.isEmpty()) {
				List<Hazard> filteredHazards = new LinkedList<Hazard>();

				for (Hazard hazard : unfilteredHazards) {
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
