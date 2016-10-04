package rod.bailey.trafficatnsw.util;

import android.widget.ListAdapter;

public class EmptyListEmptyMessagePredicate implements IEmptyMessagePredicate {

	@Override
	public boolean showEmptyMessage(ListAdapter adapter) {
		return (adapter.getCount() == 0);
	}

}
