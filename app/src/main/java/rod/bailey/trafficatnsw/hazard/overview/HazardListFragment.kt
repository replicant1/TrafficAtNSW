package rod.bailey.trafficatnsw.hazard.overview

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_list.view.*
import org.androidannotations.annotations.*
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.predicate.EmptyListEmptyMessagePredicate
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage
import rod.bailey.trafficatnsw.common.ui.ListViewWithEmptyMessage_
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import javax.inject.Inject

/**
 * Screen that displays a list of hazards for (a) All Sydney, or (b) Regional NSW.
 * List of hazards is divided into groups per XRegion. The 'mode' construction arg
 * determines which list is displayed. Construct using static create() method.
 */
@EFragment
@OptionsMenu(R.menu.menu_hazards_fragment_options)
open class HazardListFragment : Fragment(), IHazardOverviewView {

	init {
		TrafficAtNSWApplication.graph.inject(this)
	}

	private lateinit var mode: HazardOverviewMode

	private lateinit var hazardListView: ListViewWithEmptyMessage

	@FragmentArg(ARG_HAZARDS_FRAGMENT_MODE)
	@JvmField
	var modeKey: Int? = null

	@Inject
	lateinit var hazardCacheSingleton: HazardCacheSingleton

	@Inject
	lateinit var presenter: HazardOverviewPresenter;

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mode = HazardOverviewMode.values()[modeKey ?: 0]
		hazardCacheSingleton.filter = mode.filter
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		hazardListView = ListViewWithEmptyMessage_.build(activity,
														 getString(presenter.getEmptyMessageForMode(mode)),
														 EmptyListEmptyMessagePredicate())
		hazardListView.lv_list.divider =
			ContextCompat.getDrawable(activity, R.drawable.line_list_divider_partial)
		hazardListView.lv_list.dividerHeight = 2
		activity.title = getString(presenter.getScreenTitleForMode(mode))
		return hazardListView
	}

	override fun refreshHazardList() {
		hazardListView.setAdapter(HazardListAdapter())
	}

	override fun onResume() {
		super.onResume()
		presenter.onIViewCreated(this)
		loadHazardsAsync()
	}

	override fun onPause() {
		super.onPause()
		presenter.onIViewDestroyed()
	}

	@OptionsItem(R.id.menu_item_refresh_hazard_list)
	fun loadHazardsAsync() {
		presenter.loadHazardsAsync(activity, hazardListView)
	}

	companion object {
		private const val ARG_HAZARDS_FRAGMENT_MODE: String = "rod.bailey.trafficatnsw.hazards.fragment.mode"

		fun create(mode: HazardOverviewMode): HazardListFragment {
			return HazardListFragment_.builder().arg(
				ARG_HAZARDS_FRAGMENT_MODE, mode.ordinal).build()
		}
	}
}
