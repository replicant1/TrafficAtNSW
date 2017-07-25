package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import rod.bailey.trafficatnsw.R

/**
 * Footer that appears at bottom of several lists in the app that contains
 * text of the licencsing conditions for the web site data.
 */
class DataLicenceView(ctx: Context) : FrameLayout(ctx) {
	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.list_item_data_licence, this)
		val textView: AppCompatTextView = content.findViewById(R.id.tv_list_item_data_licence) as AppCompatTextView
	}
}
