package rod.bailey.trafficatnsw.cameras.overview

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.cameras.image.TrafficCameraImageActivity
import rod.bailey.trafficatnsw.cameras.data.XCamera
import rod.bailey.trafficatnsw.cameras.data.deriveSuburb
import rod.bailey.trafficatnsw.cameras.data.deriveTitle
import rod.bailey.trafficatnsw.common.ui.IRefreshableView

@EViewGroup(R.layout.list_item_camera)
open class TrafficCameraListItemView(private val ctx: Context,
									 var camera: XCamera,
									 private var favourite: Boolean) : FrameLayout(ctx), IRefreshableView {

	private inner class ItemClickListener : View.OnClickListener {
		override fun onClick(v: View?) {
			TrafficCameraImageActivity.start(ctx, camera.id ?: "")
		}
	}

	@ViewById(R.id.ll_item_camera)
	@JvmField
	var mainLayout: LinearLayout? = null

	@ViewById(R.id.iv_camera_favourite_icon)
	@JvmField
	var imageView: ImageView? = null

	@ViewById(R.id.tv_camera_list_item_line_1)
	@JvmField
	var titleTextView: TextView? = null

	@ViewById(R.id.tv_camera_list_item_line_2)
	@JvmField
	var subtitleTextView: TextView? = null

	@AfterViews
	fun afterViews() {
		titleTextView?.text = camera.properties?.deriveTitle()
		subtitleTextView?.text = camera.properties?.deriveSuburb()
		setFavourite(favourite)
		mainLayout?.setOnClickListener(ItemClickListener())
	}

	fun setFavourite(favourite: Boolean) {
		this.favourite = favourite
		imageView?.setImageResource(
			if (favourite)
				R.drawable.ic_star_black_24dp
			else
				R.drawable.ic_star_border_black_24dp)
		invalidate()
	}

	override fun refresh() {
		afterViews()
		setFavourite(favourite)
	}

	companion object {
		private val LOG_TAG: String = TrafficCameraListItemView::class.java.simpleName
	}
}
