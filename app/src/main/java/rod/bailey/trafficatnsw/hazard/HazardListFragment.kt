package rod.bailey.trafficatnsw.hazard

import android.app.Fragment
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.EFragment
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.ui.view.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.ui.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.util.MLog

/**
 * Screen that displays a list of hazards for (a) All Sydney, or (b) Regional NSW.
 * List of hazards is divided into groups per XRegion. The 'mode' construction arg
 * determines which list is displayed. Construct using static create() method.
 */
@EFragment
open class HazardListFragment : Fragment() {
	private lateinit var mode: HazardListMode
	private lateinit var hazardListView: ListViewWithEmptyMessage

	/**
	 * @return The string to display in place of the hazard list when there are no hazards
	 */
	private fun emptyMessageForMode(mode: HazardListMode?): String {
		return if (mode === HazardListMode.SYDNEY)
			getString(R.string.hazards_list_screen_empty_sydney)
		else
			getString(R.string.hazards_list_screen_empty_regional_nsw)
	}

	/**
	 * @return The title for the screen, depending on mode
	 */
	private fun screenTitleForMode(mode: HazardListMode?): String {
		return if (mode === HazardListMode.SYDNEY)
			getString(R.string.hazards_list_screen_title_sydney)
		else
			getString(R.string.hazards_list_screen_title_regional_nsw)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		if ((arguments != null) && arguments.containsKey(ARG_HAZARDS_FRAGMENT_MODE)) {
			val modeKey: Int = arguments.getInt(ARG_HAZARDS_FRAGMENT_MODE)
			mode = HazardListMode.values()[modeKey]
			MLog.d(LOG_TAG, "Fragment constructed with mode: " + mode)
		} else {
			MLog.w(LOG_TAG, "Missing argument ARG_HAZARDS_FRAGMENT_MODE")
		}

		HazardCacheSingleton.instance.filter = mode.filter
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		hazardListView = ListViewWithEmptyMessage(activity,
												  emptyMessageForMode(mode),
												  EmptyListEmptyMessagePredicate())
		hazardListView.lv_list.divider = resources.getDrawable(R.drawable.line_list_divider_partial)
		hazardListView.lv_list.dividerHeight = 2

		setHasOptionsMenu(true)
		activity.title = screenTitleForMode(mode)
		refreshAsync()
		return hazardListView
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.hazards_fragment_options_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if ((item != null) && (item.itemId == R.id.menu_item_refresh_hazard_list)) {
			refreshAsync()
		}
		return true
	}

	private fun refreshAsync() {
		DownloadHazardFileTask(activity, hazardListView).execute()
	}

	companion object {
		private val ARG_HAZARDS_FRAGMENT_MODE: String = "rod.bailey.trafficatnsw.hazards.fragment.mode"
		private val LOG_TAG = HazardListFragment::class.java.simpleName

		fun create(mode: HazardListMode): HazardListFragment {
			val result = HazardListFragment_()
			val bundle = Bundle()
			bundle.putInt(ARG_HAZARDS_FRAGMENT_MODE, mode.ordinal)
			MLog.d(LOG_TAG, "Creating HazardListFragment,mode=${mode.ordinal}")
			result.arguments = bundle
			return result
		}
	}
}
