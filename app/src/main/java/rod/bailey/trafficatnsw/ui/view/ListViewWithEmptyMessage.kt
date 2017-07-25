package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.widget.FrameLayout
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.ui.predicate.IEmptyMessagePredicate

/**
 * A ListView that is replaced with a placeholder message whenever the
 * given [predicate] determines that the list is effectively empty.
 */
class ListViewWithEmptyMessage(ctx: Context,
							   emptyMessage: String,
							   private val predicate: IEmptyMessagePredicate) : FrameLayout(ctx) {
	private val listViewAutoHideFooter: ListViewAutoHideFooter
	private val messageView: PlaceholderMessageView

	init {
		listViewAutoHideFooter = ListViewAutoHideFooter(ctx)
		messageView = PlaceholderMessageView(ctx, emptyMessage)

		addView(listViewAutoHideFooter)
		addView(messageView)
	}

	fun setAdapter(adapter: ListAdapter) {
		if (predicate.showEmptyMessage(adapter)) {
			bringChildToFront(messageView)
		} else {
			bringChildToFront(listViewAutoHideFooter)
		}
		listViewAutoHideFooter.setAdapter(adapter)
	}
}
