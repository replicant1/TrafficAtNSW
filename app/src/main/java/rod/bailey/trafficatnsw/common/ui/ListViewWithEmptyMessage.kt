package rod.bailey.trafficatnsw.common.ui

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import android.widget.ListAdapter
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import rod.bailey.trafficatnsw.common.predicate.IEmptyMessagePredicate

/**
 * A ListView that is replaced with a placeholder message whenever the
 * given [predicate] determines that the list is effectively empty.
 */
@EViewGroup
open class ListViewWithEmptyMessage(ctx: Context,
									emptyMessage: String,
									private val predicate: IEmptyMessagePredicate) : FrameLayout(ctx) {
	val listViewAutoHideFooter = ListViewAutoHideFooter_.build(ctx)
	val messageView = PlaceholderMessageView_.build(ctx, emptyMessage)

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

	companion object {
		private val LOG_TAG: String = ListViewWithEmptyMessage::class.java.simpleName
	}
}
