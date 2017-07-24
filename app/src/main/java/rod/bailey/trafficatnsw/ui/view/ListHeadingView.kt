package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import rod.bailey.trafficatnsw.R

class ListHeadingView(ctx: Context, headingText: String?, addDivider: Boolean) : FrameLayout(ctx) {

	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.list_item_heading, this)
		val textView: AppCompatTextView = content.findViewById(
			R.id.tv_list_item) as AppCompatTextView

		textView.text = headingText ?: ""
	}
}
