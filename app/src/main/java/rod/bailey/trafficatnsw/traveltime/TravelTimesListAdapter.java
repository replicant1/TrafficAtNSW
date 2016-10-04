package rod.bailey.trafficatnsw.traveltime;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.traveltime.common.MotorwayTravelTimesDatabase;
import rod.bailey.trafficatnsw.traveltime.common.TravelTime;
import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig;
import rod.bailey.trafficatnsw.util.ListHeadingView;

public class TravelTimesListAdapter extends BaseAdapter implements ListAdapter {

	/** An Item that is simply a heading e.g. "Eastbound" or "Westbound" */
	public class HeadingItem implements Item {
		private final String text;

		public HeadingItem(String text) {
			this.text = text;
		}
	}

	/**
	 * Every element in the list implements this interface. There are two
	 * implementations of it - one for Headings and another for travel time road
	 * segments
	 */
	public interface Item {
		
	}

	/** An Item that is not a heading but a travel time road segment */
	public class TravelTimeItem implements Item {
		private final TravelTime travelTime;

		public TravelTimeItem(TravelTime travelTime) {
			this.travelTime = travelTime;
		}
		
		public TravelTime getTravelTime() {
			return travelTime;
		}
	}

	@SuppressWarnings("unused")
	private static final String TAG = TravelTimesListAdapter.class
			.getSimpleName();

	private final List<Item> items;

	private List<TravelTime> travelTimes;

	private TravelTimeConfig config;

	public TravelTimesListAdapter(MotorwayTravelTimesDatabase db) {
		assert db != null;
		assert db.isPrimed();

		travelTimes = db.getTravelTimes();
		Collections.sort(travelTimes);
		config = db.getConfig();
		items = createItemList();

		updateTotalsItems();
	}

	/**
	 * Find the TT segments that represent total travel times for both forward
	 * and backward direction e.g. ETOTAL and WTOTAL then update their
	 * 'travelTimeInMinutes' attributes so tat they correctly reflect total
	 * travel time. Account for some segments having been excluded from the
	 * calculation by the user, and for some segments being inactive due to
	 * sensor faults etc.
	 */
	private void updateTotalsItems() {
		// Total up the times in the forward direction
		int topHeadingItemIndex = -1;
		int bottomHeadingItemIndex = -1;
		
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			
			if (item instanceof HeadingItem) {
				if (topHeadingItemIndex == -1) {
					topHeadingItemIndex = i;
				} else {
					bottomHeadingItemIndex = i;
				}
			}
		}
		
		int topTotal = 0;
		for (int j = topHeadingItemIndex + 1; j < bottomHeadingItemIndex; j++) {
			TravelTimeItem ttItem = (TravelTimeItem)items.get(j);
			TravelTime tt = ttItem.travelTime;
			
			if (tt.isTotal()) {
				tt.setTravelTimeMinutes(topTotal);
				break;
			} else {
				if ((tt.isIncludedInTotal()) && tt.isActive()) {
					topTotal += tt.getTravelTimeMinutes();
				}
			}
		}
		
		int bottomTotal = 0;
		for (int k = bottomHeadingItemIndex + 1; k < items.size(); k++) {
			TravelTimeItem ttItem = (TravelTimeItem) items.get(k);
			TravelTime tt = ttItem.travelTime;
			
			if (tt.isTotal()) {
				tt.setTravelTimeMinutes(bottomTotal);
				break;
			} else {
				if ((tt.isIncludedInTotal()) && tt.isActive()) {
					bottomTotal += tt.getTravelTimeMinutes();
				}
			}
		}
		
	}

	@Override
	public boolean isEnabled(int position) {
		Item item = items.get(position);
		boolean result = false;
		
		if (item instanceof TravelTimeItem) {
			TravelTimeItem travelTimeItem = (TravelTimeItem)item;
			TravelTime dataObj = travelTimeItem.travelTime;
			// The row in the travel time can only be selected by the user iff
			// it is not a TOTAL row AND if the corresponding segment is active.
			result = (!dataObj.isTotal()) && dataObj.isActive();
		}
		
		return result;
	}

	private List<Item> createItemList() {
		List<Item> result = new LinkedList<Item>();

		// The first heading identifies the direction of travel for the
		// first road segments
		TravelTime firstTT = travelTimes.get(0);
		String firstTTIdPrefix = firstTT.getSegmentId().substring(0, 1);

		String topHeadingText = null;
		String bottomHeadingText = null;

		if (firstTTIdPrefix.equals(config.forwardSegmentIdPrefix)) {
			topHeadingText = config.forwardName;
			bottomHeadingText = config.backwardName;
		} else {
			topHeadingText = config.backwardName;
			bottomHeadingText = config.forwardName;
		}

		// Insert the top heading at the beginning of the item list
		result.add(new HeadingItem(topHeadingText));

		// Insert the bottom heading after the last travel time with
		// the same segmentId prefix as the firstTTIdPrefix
		boolean bottomHeadingAdded = false;

		for (TravelTime travelTime : travelTimes) {
			String segmentIdPrefix = travelTime.getSegmentId().substring(0, 1);

			if ((!segmentIdPrefix.equals(firstTTIdPrefix))
					&& !bottomHeadingAdded) {
				result.add(new HeadingItem(bottomHeadingText));
				bottomHeadingAdded = true;
			}

			result.add(new TravelTimeItem(travelTime));
		}

		return result;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = null;

		Item item = items.get(position);

		if (item instanceof HeadingItem) {
			HeadingItem headingItem = (HeadingItem) item;
			ListHeadingView heading = new ListHeadingView(parent.getContext(),
					headingItem.text,false);
			result = heading;
		} else {
			TravelTimeItem ttItem = (TravelTimeItem) item;
			result = new TravelTimesListItemView(parent.getContext(),
					ttItem.travelTime);
		}

		return result;
	}

}
