package rod.bailey.trafficatnsw.util;

import android.widget.ListAdapter;

import rod.bailey.trafficatnsw.traveltime.TravelTimesListAdapter;
import rod.bailey.trafficatnsw.traveltime.TravelTimesListAdapter.Item;
import rod.bailey.trafficatnsw.traveltime.TravelTimesListAdapter.TravelTimeItem;
import rod.bailey.trafficatnsw.traveltime.common.TravelTime;

/**
 * Determines that the 'empty' message should only be shown if the given
 * TravelTimesListAdapter contains ONE OR MORE TravelTimes that is inactive.
 */
public class InactiveTravelTimeEmptyMessagePredicate implements
		IEmptyMessagePredicate {
	
	@SuppressWarnings("unused")
	private final String TAG = InactiveTravelTimeEmptyMessagePredicate.class.getSimpleName();

	@Override
	public boolean showEmptyMessage(ListAdapter adapter) {
		TravelTimesListAdapter ttadapter = (TravelTimesListAdapter) adapter;
		boolean foundAnInctiveSegment = false;
		
		for (int i = 0; i < ttadapter.getCount(); i++) {
			
			Item item = (Item) ttadapter.getItem(i);
			if (item instanceof TravelTimeItem) {
				TravelTimeItem ttItem = (TravelTimeItem) item;
				TravelTime travelTime = ttItem.getTravelTime();
				
				if ((!travelTime.isTotal()) && (!travelTime.isActive())) {
					foundAnInctiveSegment = true;
					break;
				}
			} 
		}
		
		return foundAnInctiveSegment;
	}

}
