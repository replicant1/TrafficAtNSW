package rod.bailey.trafficatnsw.ui.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ListAdapter
import android.widget.ListView
import rod.bailey.trafficatnsw.R

/**
 * Custom ListView with a footer whose visibility is automatically toggled
 * whenever [setAdapter] is called, so that when the list is empty, the footer
 * is hidden. When the list is not empty, the footer is shown.
 */
class ListViewAutoHideFooter(ctx: Context) : FrameLayout(ctx) {

	private var footerPresent: Boolean = false

	private val footerView: DataLicenceView

	val listView: ListView

	init {
		footerView = DataLicenceView(ctx)

		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content = inflater.inflate(R.layout.view_list_hazard, this)
		listView = content.findViewById(R.id.lv_list_hazard) as ListView

//		listView.choiceMode = ListView.CHOICE_MODE_SINGLE
//		listView.isEnabled = true

//		setFooterDividersEnabled(false)
//		setHeaderDividersEnabled(false)
//		divider = ctx.resources.getDrawable(R.drawable.line_list_divider)

//		val colors = intArrayOf(0, 0xFFFF0000.toInt(), 0) // red for the example
//		val s:ShapeDrawable = ShapeDrawable()
//		setDivider(GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors))
//		setDividerHeight(1)

//		divider = ctx.resources.getDrawable(R.drawable.line_list_divider)
//		dividerHeight = 4

		setBackgroundColor(ContextCompat.getColor(ctx, R.color.White))
	}

	fun setAdapter(adapter: ListAdapter) {
		if (adapter.count == 0) {
			// Remove the footer from an empty list, as it looks silly by itself.
			if (footerPresent) {
				footerPresent = false
				listView.removeFooterView(footerView)
			}
		} else {
			// Add the footer to list if it is not empty
			if (!footerPresent) {
				footerPresent = true
				listView.addFooterView(footerView)
			}
		}

		// Note for reasons unknown, footer view must be added *before* this
		// adapter is set, otherwise it doesn't appear.
		listView.setAdapter(adapter)
	}
}
