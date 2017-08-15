package rod.bailey.trafficatnsw.traveltime.overview

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.common.ui.ListHeadingView_
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment
import rod.bailey.trafficatnsw.traveltime.item.HeadingTTListItem
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem
import rod.bailey.trafficatnsw.traveltime.item.SimpleTTListItem
import rod.bailey.trafficatnsw.traveltime.item.TTListItemFactory
import java.util.*

class TravelTimesListAdapter(db: MotorwayTravelTimesStore) : BaseAdapter(), ListAdapter {

	private val items: List<ITTListItem>
	private val travelTimes: LinkedList<XTravelTimeSegment> = LinkedList<XTravelTimeSegment>()
	private val config: MotorwayConfig?

	init {
		travelTimes.addAll(db.getTravelTimes())
		config = db.config
		items = TTListItemFactory(db).createTTListItems()
	}

	override fun isEnabled(position: Int): Boolean {
		val item = items[position]
		var result = false

		if (item is SimpleTTListItem) {
			val dataObj = item.travelTime
			// The row in the travel time can only be selected by the user iff
			// it is not a TOTAL row AND if the corresponding segment is active.
			result = !dataObj.isTotal && (dataObj.properties?.isActive == true)
		}

		return result
	}

	override fun getCount(): Int = items.size
	override fun getItem(position: Int): Any = items[position]
	override fun getItemId(position: Int): Long = position.toLong()
	override fun getViewTypeCount(): Int = 2
	override fun getItemViewType(position: Int): Int =
		if (items[position] is HeadingTTListItem) ITEM_VIEW_TYPE_HEADING else ITEM_VIEW_TYPE_TRAVEL_TIME_SEGMENT

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val item = items[position]

		return if (convertView == null) {
			when (item) {
				is HeadingTTListItem -> ListHeadingView_.build(parent.context, item.text)
				else -> TravelTimesListItemView_.build(parent.context, (item as SimpleTTListItem).travelTime)
			}
		} else {
			when (item) {
				is HeadingTTListItem -> convertHeadingListItem(convertView, item.text)
				else -> convertTravelTimeListItem(convertView, (item as SimpleTTListItem).travelTime)
			}
		}
	}

	private fun convertHeadingListItem(convertView: View, newHeading: String): View {
		(convertView as ListHeadingView_).headingText = newHeading
		return convertView
	}

	private fun convertTravelTimeListItem(convertView: View, newSegment: XTravelTimeSegment): View {
		(convertView as TravelTimesListItemView_).travelTime = newSegment
		return convertView
	}

	companion object {
		private const val ITEM_VIEW_TYPE_HEADING = 0
		private const val ITEM_VIEW_TYPE_TRAVEL_TIME_SEGMENT = 1
	}
}
