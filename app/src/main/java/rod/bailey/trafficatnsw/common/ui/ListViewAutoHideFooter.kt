package rod.bailey.trafficatnsw.common.ui

import android.content.Context
import android.widget.FrameLayout
import android.widget.ListAdapter
import android.widget.ListView
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R

/**
 * Custom ListView with a footer whose visibility is automatically toggled
 * whenever [setAdapter] is called, so that when the list is empty, the footer
 * is hidden. When the list is not empty, the footer is shown.
 */
@EViewGroup(R.layout.view_list)
open class ListViewAutoHideFooter(ctx: Context) : FrameLayout(ctx) {

	private var footerPresent: Boolean = false

	private val footerView: DataLicenceView = DataLicenceView_.build(ctx)

	@ViewById(R.id.lv_list)
	@JvmField
	var listView: ListView? = null

	fun setAdapter(adapter: ListAdapter) {
		if (adapter.count == 0) {
			// Remove the footer from an empty list, as it looks silly by itself.
			if (footerPresent) {
				footerPresent = false
				listView?.removeFooterView(footerView)
			}
		} else {
			// Add the footer to list if it is not empty
			if (!footerPresent) {
				footerPresent = true
				listView?.addFooterView(footerView)
			}
		}

		// Note for reasons unknown, footer view must be added *before* this
		// adapter is set, otherwise it doesn't appear.
		listView?.setAdapter(adapter)
	}
}
