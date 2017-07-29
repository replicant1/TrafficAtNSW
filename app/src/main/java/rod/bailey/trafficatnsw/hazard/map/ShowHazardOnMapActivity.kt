package rod.bailey.trafficatnsw.hazard.map

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RelativeLayout
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.json.hazard.XHazard
import android.view.ViewGroup.LayoutParams.*
import android.widget.RelativeLayout.*
import com.google.android.gms.maps.*
import rod.bailey.trafficatnsw.util.DisplayUtils
import com.google.android.gms.maps.CameraUpdateFactory



class ShowHazardOnMapActivity : AppCompatActivity(), OnMapReadyCallback {
	private var hazard: XHazard? = null
	private lateinit var fragment: SupportMapFragment

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
		Log.d(LOG_TAG, "createUI: Into")

		val mainLayout = RelativeLayout(this)
		mainLayout.id = R.id.content_main
		val mainLayoutMLP = MarginLayoutParams(MATCH_PARENT, MATCH_PARENT)
		val marginPx = DisplayUtils.dp2Px(this, 5)
		mainLayoutMLP.setMargins(marginPx, marginPx, marginPx, marginPx)
		val mainLayoutRLP = RelativeLayout.LayoutParams(mainLayoutMLP)
		mainLayoutRLP.addRule(ALIGN_PARENT_BOTTOM)
		mainLayoutRLP.addRule(ALIGN_PARENT_TOP)
		mainLayoutRLP.addRule(ALIGN_PARENT_LEFT)
		mainLayoutRLP.addRule(ALIGN_PARENT_RIGHT)
		mainLayout.layoutParams = mainLayoutRLP

		fragment = SupportMapFragment()
		
		// Insert the fragment by replacing any existing fragment
		val fragmentManager = supportFragmentManager
		fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()

		setContentView(mainLayout)
		val actionBar = actionBar
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onResume() {
		super.onResume()
		fragment.getMapAsync(this)
	}

	override fun onMapReady(googleMap: GoogleMap) {
		googleMap.clear()

		val latlng = LatLng(hazard?.latlng?.latitude ?: 0.0,
							hazard?.latlng?.longitude ?: 0.0)
		val markerOptions = MarkerOptions()
			.position(latlng)
			.title(hazard?.headline ?: "Incident")
			.draggable(false)
			.flat(false)

		googleMap.addMarker(markerOptions)
		googleMap.isBuildingsEnabled = false
		googleMap.isIndoorEnabled = false
		googleMap.isTrafficEnabled = true

		val update = CameraUpdateFactory.newLatLngZoom(latlng, 18f)
		googleMap.moveCamera(update)
	}

	companion object {
		private val LOG_TAG = ShowHazardOnMapActivity::class.java.simpleName
	}
}
