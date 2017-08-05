package rod.bailey.trafficatnsw.hazard.ui

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.FragmentArg
import org.androidannotations.annotations.OptionsItem
import org.androidannotations.annotations.OptionsMenu
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.hazard.data.DownloadHazardsTask
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import javax.inject.Inject

/**
 * Screen that displays a list of hazards for (a) All Sydney, or (b) Regional NSW.
 * List of hazards is divided into groups per XRegion. The 'mode' construction arg
 * determines which list is displayed. Construct using static create() method.
 */
@EFragment
@OptionsMenu(R.menu.menu_hazards_fragment_options)
open class HazardListFragment : Fragment() {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	private lateinit var mode: HazardListMode
	private lateinit var hazardListView: ListViewWithEmptyMessage

	@FragmentArg(ARG_HAZARDS_FRAGMENT_MODE)
	@JvmField
	var modeKey: Int? = null

	@Inject
	lateinit var hazardCacheSingleton: HazardCacheSingleton

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
		mode = HazardListMode.values()[modeKey ?: 0]
		hazardCacheSingleton.filter = mode.filter
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		hazardListView = ListViewWithEmptyMessage_.build(activity,
														 emptyMessageForMode(mode),
														 EmptyListEmptyMessagePredicate())
		hazardListView.lv_list.divider =
			ContextCompat.getDrawable(activity, R.drawable.line_list_divider_partial)
		hazardListView.lv_list.dividerHeight = 2

		setHasOptionsMenu(true)
		activity.title = screenTitleForMode(mode)
		refreshAsync()
		return hazardListView
	}

	@OptionsItem(R.id.menu_item_refresh_hazard_list)
	fun refreshAsync() {
		DownloadHazardsTask(activity, hazardListView).execute()
	}

	companion object {
		private const val ARG_HAZARDS_FRAGMENT_MODE: String = "rod.bailey.trafficatnsw.hazards.fragment.mode"

		fun create(mode: HazardListMode): HazardListFragment {
			return HazardListFragment_.builder().arg(
				ARG_HAZARDS_FRAGMENT_MODE, mode.ordinal).build()
		}
	}
}
