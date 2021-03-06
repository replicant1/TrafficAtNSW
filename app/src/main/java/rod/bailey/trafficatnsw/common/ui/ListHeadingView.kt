package rod.bailey.trafficatnsw.common.ui

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.widget.FrameLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R

@EViewGroup(R.layout.list_item_heading)
open class ListHeadingView(ctx: Context, var headingText: String?): FrameLayout(ctx), IRefreshableView {

	// Without the @JvmField, the generated field is private in Java
	@ViewById(R.id.tv_list_item)
	@JvmField
	var textView: AppCompatTextView? = null

	@AfterViews
	fun afterViews() {
		textView?.text = headingText ?: ""
	}

	override fun refresh() {
		afterViews()
	}
}
