package rod.bailey.trafficatnsw.util

import android.widget.ListAdapter

interface IEmptyMessagePredicate {
	fun showEmptyMessage(adapter: ListAdapter): Boolean
}
