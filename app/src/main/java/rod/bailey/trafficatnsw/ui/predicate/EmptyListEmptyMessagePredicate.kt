package rod.bailey.trafficatnsw.ui.predicate

import android.widget.ListAdapter

/**
 * Basic definition of IEmptyMessagePredicate that returns true iff the
 * list is devoid of items.
 */
class EmptyListEmptyMessagePredicate : IEmptyMessagePredicate {
	override fun showEmptyMessage(adapter: ListAdapter): Boolean {
		return adapter.count == 0
	}
}
