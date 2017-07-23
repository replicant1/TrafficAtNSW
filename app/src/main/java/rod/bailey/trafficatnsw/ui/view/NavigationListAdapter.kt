package rod.bailey.trafficatnsw.ui.view

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import java.util.LinkedList
import rod.bailey.trafficatnsw.MainActivity
import rod.bailey.trafficatnsw.R

class NavigationListAdapter(private val mainActivity: MainActivity) : BaseAdapter(), ListAdapter {
	open inner class Item(val text: String)

	inner class PrimaryItem(text: String, val drawableId: Int) : Item(text)

	inner class SecondaryItem(text: String) : Item(text)

	private val items = LinkedList<Item>()

	init {
		items.add(PrimaryItem("Incidents", R.drawable.ic_action_warning)) // [0]
		items.add(SecondaryItem("Sydney")) // [1]
		items.add(SecondaryItem("Regional NSW")) // [2]
		items.add(PrimaryItem("Cameras", R.drawable.ic_action_camera)) // [3]
		items.add(SecondaryItem("Favourites")) // [4]
		items.add(SecondaryItem("Sydney")) // [5]
		items.add(SecondaryItem("Regional NSW")) // [6]
		items.add(PrimaryItem("Travel Times", R.drawable.ic_action_time)) // [7]
		items.add(SecondaryItem("M1")) // [8]
		items.add(SecondaryItem("M2"))// [9]
		items.add(SecondaryItem("M4")) // [10]
		items.add(SecondaryItem("M7")) // [11]
	}

	override fun getCount(): Int {
		return items.size
	}

	override fun getItem(position: Int): Any {
		return items[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun isEnabled(position: Int): Boolean {
		val item = items[position]
		return item is SecondaryItem
	}

	override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
		val item = items[position]
		var result: View?

		if (item is PrimaryItem) {
			result = NavigationListHeadingView(mainActivity,
																			   item.text,
																			   item.drawableId,
																			   position > 0)
		} else {
			result = NavigationListSubheadingView(mainActivity,
																				  item.text)
		}

		return result
	}
}