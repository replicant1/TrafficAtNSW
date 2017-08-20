package rod.bailey.trafficatnsw.cameras.overview

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.cameras.data.XCamera
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.common.ui.ListHeadingView_
import rod.bailey.trafficatnsw.hazard.data.XRegion
import java.util.*
import javax.inject.Inject

class TrafficCameraListAdapter(filter: ITrafficCameraFilter) : BaseAdapter(), ListAdapter {

	@Inject
	lateinit var cameraCache: TrafficCameraCacheSingleton

	private val listData = ArrayList<Any>()

	init {
		TrafficAtNSWApplication.graph.inject(this)
		cameraCache.filter = filter
		// Linearize the info in the TrafficCameraCacheSingleton. The resulting list
		// has two sorts of entries. A XRegion instance if for the beginning of a
		// region section, a n XCamera instance if a camera within a region section.
		for (region in sortedCameraRegions()) {
			listData.add(region)
			val cameras = cameraCache.getCamerasForRegion(region)
			Collections.sort(cameras)
			listData.addAll(cameras)
		}
	}

	override fun isEnabled(position: Int): Boolean {
		val dataObj = listData[position]
		return dataObj is XCamera
	}

	private fun createHeadingListItem(region: XRegion, parent: ViewGroup): View {
		val heading = ListHeadingView_.build(parent.context, region.description)
		return heading
	}

	private fun createTrafficCameraListItem(ctx: Context,
											camera: XCamera): TrafficCameraListItemView {
		val item = TrafficCameraListItemView_.build(ctx, camera, camera.isFavourite)
		item.isFocusable = true
		return item
	}

	override fun getCount(): Int = listData.size
	override fun getItem(position: Int): Any = listData[position]
	override fun getItemId(position: Int): Long = position.toLong()
	override fun getItemViewType(position: Int): Int =
		if (listData[position] is XRegion) ITEM_VIEW_TYPE_HEADING else ITEM_VIEW_TYPE_TRAFFIC_CAMERA

	override fun getViewTypeCount(): Int = 2

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val listItem = listData[position]

		return if (convertView == null) {
			when (listItem) {
				is XRegion -> createHeadingListItem(listItem, parent)
				else -> createTrafficCameraListItem(parent.context, listItem as XCamera)
			}
		} else {
			when (listItem) {
				is XRegion -> convertHeadingListItem(convertView, listItem)
				else -> convertTrafficCameraListItem(convertView, listItem as XCamera)
			}
		}
	}

	private fun convertHeadingListItem(convertView: View, newHeadingData: XRegion): View {
		(convertView as ListHeadingView_).headingText = newHeadingData.description
		return convertView
	}

	private fun convertTrafficCameraListItem(convertView: View, newCameraData: XCamera): View {
		(convertView as TrafficCameraListItemView_).camera = newCameraData
		return convertView
	}

	private fun sortedCameraRegions(): List<XRegion> {
		val sortedRegions = LinkedList<XRegion>()

		for (region in XRegion.values()) {
			val camerasForRegion = cameraCache.getCamerasForRegion(region)
			if (!camerasForRegion.isEmpty()) {
				sortedRegions.add(region)
			}
		}

		return sortedRegions
	}

	companion object {
		private val LOG_TAG = TrafficCameraListAdapter::class.java.simpleName
		private const val ITEM_VIEW_TYPE_HEADING = 0
		private const val ITEM_VIEW_TYPE_TRAFFIC_CAMERA = 1
	}
}
