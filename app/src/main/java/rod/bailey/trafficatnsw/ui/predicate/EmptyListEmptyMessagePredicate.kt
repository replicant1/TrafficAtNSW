package rod.bailey.trafficatnsw.ui.predicate

import android.widget.ListAdapter

class EmptyListEmptyMessagePredicate : IEmptyMessagePredicate {
	override fun showEmptyMessage(adapter: ListAdapter): Boolean {
		return adapter.count == 0
	}
}
