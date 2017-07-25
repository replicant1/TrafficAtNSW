package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import rod.bailey.trafficatnsw.R

class PlaceholderMessageView(ctx: Context, message: String) : FrameLayout(ctx) {

	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.view_empty_list_placeholder, this)
		val textView: AppCompatTextView = content.findViewById(R.id.tv_empty_list_placeholder) as AppCompatTextView
		textView.text = message
	}

}
