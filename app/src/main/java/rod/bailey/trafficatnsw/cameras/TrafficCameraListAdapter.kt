package rod.bailey.trafficatnsw.cameras

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import java.util.ArrayList
import java.util.Collections
import java.util.LinkedList
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.json.hazard.XRegion
import rod.bailey.trafficatnsw.ui.view.ListHeadingView

class TrafficCameraListAdapter(filter: ITrafficCameraFilter?) : BaseAdapter(), ListAdapter {
	private val db: TrafficCameraCacheSingleton
	private val listData = ArrayList<Any>()

	init {
		db = TrafficCameraCacheSingleton.instance
		db.setFilter(filter!!)
		// Linearize the info in the TrafficCameraCacheSingleton. The resulting list
		// has two sorts of entries. A XRegion instance if for the beginning of a
		// region section, a TrafficCamera instance if a camera within a
		// region section.
		for (region in sortedCameraRegions()) {
			listData.add(region)
			val cameras = db.getCamerasForRegion(region)
			Collections.sort(cameras!!)
			listData.addAll(cameras)
		}
	}

	override fun isEnabled(position: Int): Boolean {
		val dataObj = listData[position]
		return dataObj is TrafficCamera
	}

	private fun createHeading(region: XRegion, parent: ViewGroup): View {
		val heading = ListHeadingView(parent.context,
									  region.description, false)
		return heading
	}

	private fun createTrafficCameraListItem(ctx: Context,
											camera: TrafficCamera): TrafficCameraListItemView {
		val item = TrafficCameraListItemView(ctx,
											 camera, camera.isFavourite)
		item.isFocusable = true
		return item
	}

	override fun getCount(): Int {
		return listData.size
	}

	override fun getItem(position: Int): Any {
		return listData[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
		var result: View? = null
		val listItem = listData[position]

		if (listItem is XRegion) {
			result = createHeading(listItem, parent)
		} else {
			result = createTrafficCameraListItem(parent.context,
												 listItem as TrafficCamera)
		}

		return result
	}

	private fun sortedCameraRegions(): List<XRegion> {
		val sortedRegions = LinkedList<XRegion>()

		for (region in XRegion.values()) {
			if (db.getCamerasForRegion(region) != null && !db.getCamerasForRegion(
				region)!!.isEmpty()) {
				sortedRegions.add(region)
			}
		}

		return sortedRegions
	}

	companion object {
		private val TAG = TrafficCameraListAdapter::class.java.simpleName
	}
}
