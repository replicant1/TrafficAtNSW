package rod.bailey.trafficatnsw.hazard

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.filter.AdmitAllHazardFilter
import rod.bailey.trafficatnsw.ui.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.ui.ListWithEmptyMessage
import rod.bailey.trafficatnsw.util.MLog

/**
 * Screen that displays a list of hazards for (a) All Sydney, or (b) Regional NSW.
 * List of hazards is divided into groups per XRegion.
 */
class HazardListFragment : Fragment() {
	private var mode: HazardListMode? = null
	private var mainLayout: ListWithEmptyMessage? = null

	private fun emptyMessageForMode(mode: HazardListMode?): String {
		return if (mode === HazardListMode.SYDNEY)
			getString(R.string.hazards_list_screen_empty_sydney)
		else
			getString(R.string.hazards_list_screen_empty_regional_nsw)
	}

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

		HazardDatabase.instance.filter = mode?.filter ?: AdmitAllHazardFilter()
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		mainLayout = ListWithEmptyMessage(activity,
																	 emptyMessageForMode(mode),
										  EmptyListEmptyMessagePredicate())
		setHasOptionsMenu(true)
		activity.title = screenTitleForMode(mode)
		refreshAsync()
		return mainLayout
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
		DownloadHazardFileTask(activity, mainLayout, mode).execute()
	}

	companion object {
		val ARG_HAZARDS_FRAGMENT_MODE: String = "rod.bailey.trafficatnsw.hazards.fragment.mode"
		private val LOG_TAG = HazardListFragment::class.java.simpleName

		fun create(mode: HazardListMode) : HazardListFragment {
			val result = HazardListFragment()
			val bundle = Bundle()
			bundle.putInt(ARG_HAZARDS_FRAGMENT_MODE, mode.ordinal)
			MLog.d(LOG_TAG, "Creating HazardListFragment with mode arg value " + mode.ordinal)
			result.arguments = bundle
			return result
		}
	}
}
