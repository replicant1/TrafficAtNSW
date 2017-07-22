package rod.bailey.trafficatnsw.ui.predicate

import android.widget.ListAdapter

interface IEmptyMessagePredicate {
	fun showEmptyMessage(adapter: ListAdapter): Boolean
}
