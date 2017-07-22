package rod.bailey.trafficatnsw.util

import android.widget.ListAdapter

class EmptyListEmptyMessagePredicate : IEmptyMessagePredicate {
	override fun showEmptyMessage(adapter: ListAdapter): Boolean {
		return adapter.count == 0
	}
}
