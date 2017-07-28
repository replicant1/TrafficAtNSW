package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import rod.bailey.trafficatnsw.R

class LineListItemView(ctx: Context, lineOfText: String) : FrameLayout(ctx) {

	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.list_item_hazard_detail_line, this)
		val textView: AppCompatTextView = content.findViewById(R.id.tv_hazard_detail_line) as AppCompatTextView

		textView.text = lineOfText
	}
}
