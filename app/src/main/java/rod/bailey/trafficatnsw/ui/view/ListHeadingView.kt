package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import org.androidannotations.annotations.*
import rod.bailey.trafficatnsw.R

@EViewGroup(R.layout.list_item_heading)
open class ListHeadingView : FrameLayout {

	@ViewById(R.id.tv_list_item)
	@JvmField
	var textView:AppCompatTextView? = null

	private var headingText: String?

	constructor(ctx: Context, headingText: String?) : super(ctx) {
		Log.d(LOG_TAG, "constructor: ${headingText}, textView=${textView}")
		this.headingText = headingText
	}

//	init {
//		val inflater: LayoutInflater = LayoutInflater.from(ctx)
//		val content: View = inflater.inflate(R.layout.list_item_heading, this)
//		val textView: AppCompatTextView = content.findViewById(
//			R.id.tv_list_item) as AppCompatTextView


//	}

	@AfterViews
	fun afterViews() {
		Log.d(LOG_TAG, "**** Into afterViews ****");
		textView?.text = headingText ?: ""
	}

	companion object {
		private val LOG_TAG: String = ListHeadingView::javaClass.name
	}
}
