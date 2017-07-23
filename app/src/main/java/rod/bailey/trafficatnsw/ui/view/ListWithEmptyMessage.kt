package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.widget.FrameLayout
import android.widget.ListAdapter
import android.widget.ListView
import rod.bailey.trafficatnsw.ui.predicate.IEmptyMessagePredicate

class ListWithEmptyMessage(ctx: Context,
						   emptyMessage: String,
						   private val predicate: IEmptyMessagePredicate) : FrameLayout(ctx) {
	private val listView: FooterCancellingListView
	private val messageView: PlaceholderMessageView

	init {
		listView = FooterCancellingListView(ctx)
		listView.choiceMode = ListView.CHOICE_MODE_SINGLE
		listView.isEnabled = true

		messageView = PlaceholderMessageView(ctx, emptyMessage)

		addView(listView)
		addView(messageView)
	}

	fun setEmptyMessage(message: String) {
		messageView.setMessage(message)
	}

	fun setAdapter(adapter: ListAdapter) {
		if (predicate.showEmptyMessage(adapter)) {
			bringChildToFront(messageView)
		} else {
			bringChildToFront(listView)
		}
		listView.adapter = adapter
	}

	companion object {
		private val TAG = ListWithEmptyMessage::class.java.simpleName
	}
}
