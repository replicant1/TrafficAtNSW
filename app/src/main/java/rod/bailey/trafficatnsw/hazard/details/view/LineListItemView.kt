package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R

@EViewGroup(R.layout.list_item_hazard_detail_line)
open class LineListItemView(ctx: Context,  val lineOfText: String) : FrameLayout(ctx) {

	@ViewById(R.id.tv_hazard_detail_line)
	@JvmField
	var textView: AppCompatTextView? = null

	@AfterViews
	fun afterViews() {
		textView?.text = lineOfText
	}
}
