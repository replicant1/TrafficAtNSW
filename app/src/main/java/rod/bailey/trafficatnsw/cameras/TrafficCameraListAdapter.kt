package rod.bailey.trafficatnsw.cameras

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import java.util.ArrayList
import java.util.Collections
import java.util.LinkedList
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.json.hazard.XRegion
import rod.bailey.trafficatnsw.ui.view.ListHeadingView_
import javax.inject.Inject

class TrafficCameraListAdapter(filter: ITrafficCameraFilter) : BaseAdapter(), ListAdapter {

	@Inject
	lateinit var cameraCache: TrafficCameraCacheSingleton

	private val listData = ArrayList<Any>()

	init {
		TrafficAtNSWApplication.graph.inject(this)
		cameraCache.setFilter(filter)
		// Linearize the info in the TrafficCameraCacheSingleton. The resulting list
		// has two sorts of entries. A XRegion instance if for the beginning of a
		// region section, a TrafficCamera instance if a camera within a
		// region section.
		for (region in sortedCameraRegions()) {
			listData.add(region)
			val cameras = cameraCache.getCamerasForRegion(region)
			Collections.sort(cameras!!)
			listData.addAll(cameras)
		}
	}

	override fun isEnabled(position: Int): Boolean {
		val dataObj = listData[position]
		return dataObj is TrafficCamera
	}

	private fun createHeading(region: XRegion, parent: ViewGroup): View {
		val heading = ListHeadingView_.build(parent.context, region.description)
		return heading
	}

	private fun createTrafficCameraListItem(ctx: Context,
											camera: TrafficCamera): TrafficCameraListItemView {
		val item = TrafficCameraListItemView_.build(ctx, camera, camera.isFavourite)
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

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var result: View?
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
			if (cameraCache.getCamerasForRegion(region) != null && !cameraCache.getCamerasForRegion(
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
