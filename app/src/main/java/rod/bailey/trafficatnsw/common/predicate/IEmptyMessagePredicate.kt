package rod.bailey.trafficatnsw.common.predicate

import android.widget.ListAdapter

interface IEmptyMessagePredicate {

	/**
	 * @return True if an analysis of the data in the [adapter] suggests that
	 * a "no data to display" message should be shown because the list is "empty"
	 * by some context-specific definition of "empty".
	 */
	fun showEmptyMessage(adapter: ListAdapter): Boolean
}
