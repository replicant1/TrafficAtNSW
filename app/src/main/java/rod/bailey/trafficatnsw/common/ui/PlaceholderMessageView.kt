package rod.bailey.trafficatnsw.common.ui

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.widget.FrameLayout
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R

@EViewGroup(R.layout.view_empty_list_placeholder)
open class PlaceholderMessageView(ctx: Context, val message: String) : FrameLayout(ctx) {

	@ViewById(R.id.tv_empty_list_placeholder)
	@JvmField
	var textView: AppCompatTextView? = null

	fun afterViews() {
		textView?.text = message
	}
}
