package rod.bailey.trafficatnsw.hazard.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.json.hazard.XHazard

@EActivity(R.layout.activity_show_hazard_on_map)
open class ShowHazardOnMapActivity : AppCompatActivity(), OnMapReadyCallback {

	private var hazard: XHazard? = null

	private var fragment: SupportMapFragment? = null

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this)
		}
		return true
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val hazardId: Int? = intent?.extras?.getInt(EXTRA_HAZARD_ID_INT)
		if (hazardId != null) {
			hazard = HazardCacheSingleton.instance.getUnfilteredHazard(hazardId)
		}
	}

	@AfterViews
	fun afterViews() {
		fragment = supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
		val actionBar = actionBar
		actionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onResume() {
		super.onResume()
		fragment?.getMapAsync(this)
	}

	/**
	 * When map is ready, center map on hazard and put marker at hazard
	 */
	override fun onMapReady(googleMap: GoogleMap) {
		googleMap.clear()

		val latlng = LatLng(hazard?.latlng?.latitude ?: 0.0,
							hazard?.latlng?.longitude ?: 0.0)
		val markerOptions = MarkerOptions()
			.position(latlng)
			.title(hazard?.headline ?: getString(R.string.hazard_map_screen_title))
			.draggable(false)
			.flat(false)

		googleMap.addMarker(markerOptions)
		googleMap.isBuildingsEnabled = false
		googleMap.isIndoorEnabled = false
		googleMap.isTrafficEnabled = true

		val update = CameraUpdateFactory.newLatLngZoom(latlng, MAP_ZOOM)
		googleMap.moveCamera(update)
	}

	companion object {
		private val LOG_TAG = ShowHazardOnMapActivity::class.java.simpleName
		private val MAP_ZOOM: Float = 18f
		private val EXTRA_HAZARD_ID_INT: String = "rod.bailey.trafficatnsw.hazard.id.map"

		fun start(ctx:Context, hazardId: Int?) {
			val hazardIntent = Intent(ctx, ShowHazardOnMapActivity_::class.java)
			hazardIntent.putExtra(EXTRA_HAZARD_ID_INT, hazardId)
			ctx.startActivity(hazardIntent)
		}
	}
}
