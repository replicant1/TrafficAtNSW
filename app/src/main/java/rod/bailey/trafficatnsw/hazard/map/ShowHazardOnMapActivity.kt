package rod.bailey.trafficatnsw.hazard.map

import android.app.ActionBar
import android.app.Activity
import android.app.FragmentManager
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RelativeLayout
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.json.hazard.XHazard
import android.view.ViewGroup.LayoutParams.*
import android.widget.RelativeLayout.*
import rod.bailey.trafficatnsw.util.DisplayUtils

class ShowHazardOnMapActivity : Activity() {
	private var hazard: XHazard? = null
	private lateinit var fragment: MapFragment

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this)
		}
		return true
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val extras = intent.extras
		val hazardId = extras.getInt("hazardId")
		hazard = HazardCacheSingleton.instance.getUnfilteredHazard(hazardId)

		createUI()
	}

	private fun createUI() {
		val mainLayout = RelativeLayout(this)
		mainLayout.id = R.id.content_frame
		val mainLayoutMLP = MarginLayoutParams(
			MATCH_PARENT, MATCH_PARENT)
		val marginPx = DisplayUtils.dp2Px(this, 5)
		mainLayoutMLP.setMargins(marginPx, marginPx, marginPx, marginPx)
		val mainLayoutRLP = RelativeLayout.LayoutParams(
			mainLayoutMLP)
		mainLayoutRLP.addRule(ALIGN_PARENT_BOTTOM)
		mainLayoutRLP.addRule(ALIGN_PARENT_TOP)
		mainLayoutRLP.addRule(ALIGN_PARENT_LEFT)
		mainLayoutRLP.addRule(ALIGN_PARENT_RIGHT)
		mainLayout.layoutParams = mainLayoutRLP

		fragment = MapFragment()
		// Insert the fragment by replacing any existing fragment
		val fragmentManager = fragmentManager
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
			.commit()

		setContentView(mainLayout)
		val actionBar = actionBar
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onResume() {
		super.onResume()
		val googleMap: GoogleMap? = null // fragment.getMap(); NOw getMzapAsync
		googleMap!!.clear()
		val latlng = LatLng(hazard?.latlng?.latitude ?: 0.0,
							hazard?.latlng?.longitude ?: 0.0)
		val markerOptions = MarkerOptions().position(latlng)
			.title(hazard?.headline).draggable(false).flat(false)

		googleMap.addMarker(markerOptions)
		googleMap.isBuildingsEnabled = false
		googleMap.isIndoorEnabled = false
		googleMap.isTrafficEnabled = true
		val update = CameraUpdateFactory.newLatLngZoom(latlng, 18f)
		googleMap.moveCamera(update)
	}

	companion object {
		private val TAG = ShowHazardOnMapActivity::class.java.simpleName
	}
}
