package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.widget.FrameLayout
import android.widget.ListAdapter
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import rod.bailey.trafficatnsw.ui.predicate.IEmptyMessagePredicate

/**
 * A ListView that is replaced with a placeholder message whenever the
 * given [predicate] determines that the list is effectively empty.
 */
@EViewGroup
open class ListViewWithEmptyMessage(ctx: Context,
									emptyMessage: String,
									private val predicate: IEmptyMessagePredicate) : FrameLayout(ctx) {
	private val listViewAutoHideFooter = ListViewAutoHideFooter_.build(ctx)
	private val messageView = PlaceholderMessageView_.build(ctx, emptyMessage)

	@AfterViews
	fun afterViews() {
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
