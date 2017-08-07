package rod.bailey.trafficatnsw.cameras.data

import android.content.Context
import android.content.SharedPreferences
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter
import rod.bailey.trafficatnsw.hazard.data.XRegion
import rod.bailey.trafficatnsw.util.MLog
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*

/**
 * Normally we would read this in from a JSON file, but the contents are hardcoded
 * here for expediency. Always inject this so it remains a singleton.
 */
class TrafficCameraCacheSingleton : PropertyChangeListener {

	companion object {
		const val PROPERTY_FAVOURITE_SET = "rod.bailey.trafficatnsw.favouriteSet"
		private val FAVOURITE_CAMERAS_FILE_NAME = "favourite_cameras"
		private val FAVOURITE_STATE_PREF_KEY = "FAVOURITE"
		private val TAG = TrafficCameraCacheSingleton::class.java.simpleName
	}

	private val support = PropertyChangeSupport(this)
	private var filter: ITrafficCameraFilter? = null
	private val filteredCamerasPerRegion = HashMap<XRegion, List<TrafficCamera>>()
	private val unfilteredCamerasPerRegion = HashMap<XRegion, List<TrafficCamera>>()
	private lateinit var ctx: Context

	init {
		initUnfiltered()
	}

	fun addPropertyChangeListener(listener: PropertyChangeListener) {
		support.addPropertyChangeListener(listener)
	}

	fun removePropertyChangeListener(listener: PropertyChangeListener) {
		support.removePropertyChangeListener(listener)
	}

	private fun fireFavouritePropertyChangeEvent() {
		support.firePropertyChange(PROPERTY_FAVOURITE_SET, null, this)
	}

	fun init(ctx: Context) {
		this.ctx = ctx
		loadFavourites()
	}

	private fun createRegNorthData(): List<TrafficCamera> {
		val data = LinkedList<TrafficCamera>()
		val hexham = TrafficCamera(
			"New England Highway",
			"Hexham",
			"New England Highway near Hexham Bridge looking south towards Newcastle.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/newenglandhwy_hexham.jpg",
			XRegion.REG_NORTH, 100)
		hexham.isFavourite = false
		data.add(hexham)
		val newcastle_rd = TrafficCamera(
			"Newcastle XRoad",
			"Newcastle",
			"Newcastle XRoad at Croudace Street looking west towards M1 Pacific Motorway.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/newcastlelinkrd_newcastle.jpg",
			XRegion.REG_NORTH, 101)
		data.add(newcastle_rd)
		// [108] = M1 Pacific Mwy (John Renshaw Dr)
		val renshaw_dr = TrafficCamera(
			"M1 Pacific Motorway",
			"John Renshaw Drive",
			"M1 Pacific Motorway at John Renshaw Drive looking south towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_johnrenshawdr.jpg",
			XRegion.REG_NORTH, 108)
		data.add(renshaw_dr)
		// [109] = M1 Pacific Mwy (Sparks road)
		val sparks_rd = TrafficCamera(
			"M1 Pacific Motorway",
			"Sparks XRoad",
			"M1 Pacific Motorway at Sparks XRoad looking north towards Newcastle.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_sparkesrd.jpg",
			XRegion.REG_NORTH, 109)
		data.add(sparks_rd)
		// [26] = M1 Pacific Mwy (Kariong)
		val f3_kariong = TrafficCamera(
			"M1 Pacific Motorway",
			"Kariong",
			"M1 Pacific Motorway at the Kariong on ramp looking south towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_kariong.jpg",
			XRegion.REG_NORTH, 26)
		data.add(f3_kariong)
		// [27] = M1 (hawkesbuy river)
		val f3_hawkesbury = TrafficCamera(
			"M1 Pacific Motorway",
			"Hawkesbury River",
			"M1 Pacific Motorway crossing the Hawkesbury River Bridge looking south towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_mooney.jpg",
			XRegion.REG_NORTH, 27)
		data.add(f3_hawkesbury)
		// [28] = m1 pacific mwy (mount white)
		val f3_mt_white = TrafficCamera(
			"M1 Pacific Motorway",
			"Mount White",
			"M1 Pacific Motorway at the Mount White heavy vehicle checking station looking north towards Calga.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_mountwhite.jpg",
			XRegion.REG_NORTH, 28)
		data.add(f3_mt_white)
		// [29] = m1 pacific mwy (ourimbah)
		val f3_ourimbah = TrafficCamera(
			"M1 Pacific Motorway",
			"Ourimbah",
			"M1 Pacific Motorway at Ourimbah on ramp looking north towards Wyong.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_ourimbah.jpg",
			XRegion.REG_NORTH, 29)
		data.add(f3_ourimbah)
		// [30] = m1 pacific mwy (wahroonga)
		val f3_wahroonga = TrafficCamera(
			"M1 Pacific Motorway",
			"Wahroonga",
			"The Pacific Highway on ramp to the M1 Pacific Motorway looking north towards the Central Coast.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_wahroonga.jpg",
			XRegion.REG_NORTH, 30)
		data.add(f3_wahroonga)
		// [31] = m1 pacific mwy (windy banks)
		val f3_windy = TrafficCamera(
			"M1 Pacific Motorway",
			"Windy Banks",
			"M1 Pacific Motorway at the Windy Banks on ramp looking south towards Hornsby.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_windybanks.jpg",
			XRegion.REG_NORTH, 31)
		data.add(f3_windy)

		Collections.sort(data)

		return data
	}

	private fun createRegSouthData(): List<TrafficCamera> {
		val data = LinkedList<TrafficCamera>()
		val kosciuszko_rd = TrafficCamera(
			"Kosciuszko XRoad",
			"Wilson's Valley",
			"Wilson's Valley chain bay looking south-west towards Perisher Valley.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/kosciuszkord_wilsonsvalley.jpg",
			XRegion.REG_SOUTH, 65)
		data.add(kosciuszko_rd)
		val mt_ousley = TrafficCamera(
			"M1 Princes Motorway",
			"Mt Ousley",
			"M1 Princes Motorway at Mt Ousley XRoad looking north towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f6_mtousley.jpg",
			XRegion.REG_SOUTH, 200)
		data.add(mt_ousley)
		// [201] = Princes Hwy (Albion park rail)
		val albion = TrafficCamera(
			"Princes Highway",
			"Albion Park Rail",
			"Princes Highway at Airport XRoad looking north towards Wollongong.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princeshwy_albionparkrail.jpg",
			XRegion.REG_SOUTH, 201)
		data.add(albion)
		// [202] = Princes Hwy (Wollongong)
		val wollongong = TrafficCamera(
			"Princes Highway",
			"Wollongong",
			"Princes Highway near Bourke Street looking north towards Bulli.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princeshwy_wollongong.jpg",
			XRegion.REG_SOUTH, 202)
		data.add(wollongong)

		Collections.sort(data)

		return data
	}

	private fun createSydMetData(): List<TrafficCamera> {
		val data = LinkedList<TrafficCamera>()
		val king_georges_rd = TrafficCamera(
			"King Georges XRoad",
			"Beverly Hills",
			"Corner of King Georges XRoad and Stoney Creek XRoad looking north towards Beverly Hills.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/knggrd_beverlyhills.jpg",
			XRegion.SYD_MET, 1)
		king_georges_rd.isFavourite = false
		data.add(king_georges_rd)
		val general_holmes_dr = TrafficCamera(
			"General Holmes Drive",
			"Botany",
			"General Holmes Drive at the east ended of the Airport Tunnel looking east towards Botany.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/ghd_airport.jpg",
			XRegion.SYD_MET, 2)
		general_holmes_dr.isFavourite = false
		data.add(general_holmes_dr)
		val william_st_east_syd = TrafficCamera(
			"William Street",
			"East Sydney",
			"Corner of William Street and College Street near Hyde Park looking east towards Kings Cross.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/williamst.jpg",
			XRegion.SYD_MET, 3)
		data.add(william_st_east_syd)
		val gladesville_bridge = TrafficCamera(
			"Gladesville Bridge",
			"Drummoyne",
			"Gladesville Bridge looking west towards Gladesville.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/gladesvillebr.jpg",
			XRegion.SYD_MET, 4)
		data.add(gladesville_bridge)
		val hume_hwy_ashfield = TrafficCamera(
			"Hume Highway",
			"Ashfield",
			"Corner of Hume Highway and Roberts XRoad looking west towards Strathfield.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_ashfield.jpg",
			XRegion.SYD_MET, 5)
		data.add(hume_hwy_ashfield)
		val hume_hwy_strathfield = TrafficCamera(
			"Hume Highway",
			"Strathfield",
			"Corner of Hume Highway and Roberts XRoad looking west towards Yagoona.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_strathfield.jpg",
			XRegion.SYD_MET, 6)
		data.add(hume_hwy_strathfield)
		// City West Link (Lilyfield)
		val city_west = TrafficCamera(
			"City West Link",
			"Lilyfield",
			"City West Link at Lilyfield looking east towards Victoria XRoad intersection.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/citywestlink.jpg",
			XRegion.SYD_MET, 7)
		data.add(city_west)
		// Easter distributor (Kensington)
		val eastern_dist = TrafficCamera(
			"Eastern Distributor",
			"Kensington",
			"Southern ended of the Eastern Distributor tunnel near the Link XRoad on-ramp looking north towards Sydney",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/easterndist.jpg",
			XRegion.SYD_MET, 8)
		data.add(eastern_dist)
		// Alison XRoad (Randwick)
		val alison_road = TrafficCamera(
			"Alison XRoad",
			"Randwick",
			"Alison XRoad at Darley Street looking north-west towards Anzac Parade.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/alisonrd_randwick.jpg",
			XRegion.SYD_MET, 9)
		data.add(alison_road)
		// Anzac Bridge (Rozelle)
		val anzac_bridge = TrafficCamera(
			"Anzac Bridge",
			"Rozelle",
			"Intersection of Victoria XRoad and Anzac Bridge looking east towards the Sydney CBD.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/anzacbr.jpg",
			XRegion.SYD_MET, 10)
		data.add(anzac_bridge)
		// Anzac Parade (Moore Park)
		val anzac_pde = TrafficCamera(
			"Anzac Parade",
			"Moore Park",
			"Corner of Anzac Parade and Cleveland Street looking south towards Randwick.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/anzacpde.jpg",
			XRegion.SYD_MET, 11)
		data.add(anzac_pde)
		// [12] = M4 Western Mwy (Olympic Park)
		val m4_olympic_park = TrafficCamera(
			"M4 Western Motorway",
			"Olympic Park",
			"M4 at Sydney Olympic Park looking east towards Strathfield.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_olympic.jpg",
			XRegion.SYD_MET, 12)
		data.add(m4_olympic_park)
		// [13] = M5 East (Beverly Hills)
		val m5_east = TrafficCamera(
			"M5 East",
			"Beverly Hills",
			"M5 East at the King Georges XRoad on ramp looking west towards Riverwood.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5east.jpg",
			XRegion.SYD_MET, 13)
		data.add(m5_east)
		// [14] = Parramatta XRoad (Leichardt)
		val parramatta_rd_leichardt = TrafficCamera(
			"Parramatta XRoad",
			"Leichardt",
			"Parramatta XRoad at Elswick Street looking west towards Ashfield.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_leichhardt.jpg",
			XRegion.SYD_MET, 14)
		data.add(parramatta_rd_leichardt)
		// [15] = Parramatta XRoad (Silverwater)
		val parramatta_rd_silverwater = TrafficCamera(
			"Parramatta XRoad",
			"Silverwater",
			"Parramatta XRoad at Silverwater XRoad looking west towards Parramatta.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_silverwater.jpg",
			XRegion.SYD_MET, 15)
		data.add(parramatta_rd_silverwater)
		// [16] = Parramatta XRoad (Strathfield)
		val parramatta_rd_strathfield = TrafficCamera(
			"Parramatta XRoad",
			"Strathfield",
			"Parramatta XRoad at Leicester Avenue looking east towards Burwood.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_strathfield.jpg",
			XRegion.SYD_MET, 16)
		data.add(parramatta_rd_strathfield)
		// [17] = Princes Hwy (Blakehurst)
		val princes_hwy_blakehurst = TrafficCamera(
			"Princes Highway",
			"Blakehurst",
			"Princes Highway at the King Georges XRoad intersection looking south towards Sutherland.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princes_blakehurst.jpg",
			XRegion.SYD_MET, 17)
		data.add(princes_hwy_blakehurst)
		// [18] = Princes Hwy (Kogarah)
		val princes_hwy_kogarah = TrafficCamera(
			"Princes Highway",
			"Kogarah",
			"Princes Highway at President Avenue looking south towards Ramsgate.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princes_kogarah.jpg",
			XRegion.SYD_MET, 18)
		data.add(princes_hwy_kogarah)
		// [19] = Princes Hwy (St Peters)
		val princes_hwy_st_peters = TrafficCamera(
			"Princes Highway",
			"St Peters",
			"Princes Highway at the Canal XRoad intersection looking north towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princes_stpeters.jpg",
			XRegion.SYD_MET, 19)
		data.add(princes_hwy_st_peters)
		// [20] = Southern Cross Drive (Eastlakes)
		val southern_cross_dr = TrafficCamera(
			"Southern Cross Drive",
			"Eastlakes",
			"Southern Cross Drive at Wentworth Avenue looking south towards Mascot.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/scd_eastlakes.jpg",
			XRegion.SYD_MET, 20)
		data.add(southern_cross_dr)
		// [21] = Syd Einfield Drive (Bondi Junction)
		val syd_einfield_dr = TrafficCamera(
			"Syd Einfield Drive",
			"Bondi Junction",
			"Syd Einfeld Drive looking west towards Sydney CBD.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/sed_bondijunction.jpg",
			XRegion.SYD_MET, 21)
		data.add(syd_einfield_dr)
		// [22] = SHB (Milsons Pt)
		val shb = TrafficCamera(
			"Sydney Harbour Bridge",
			"Milsons Point",
			"Sydney Harbour Bridge deck looking south towards the Sydney CBD.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/harbourbridge.jpg",
			XRegion.SYD_MET, 22)
		data.add(shb)
		// [23] = The Grand Pde (Brighton-le-sands)
		val grand_pde = TrafficCamera(
			"The Grand Parade",
			"Brighton-Le-Sands",
			"The Grand Parade at Bay Street looking north towards Sydney Airport.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/grandpde_bls.jpg",
			XRegion.SYD_MET, 23)
		data.add(grand_pde)
		// [24] = George St (Railway Sq)
		val george_st = TrafficCamera(
			"George Street",
			"Railway square",
			"Railway Square, George Street looking north towards Sydney CBD.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/georgest.jpg",
			XRegion.SYD_MET, 24)
		data.add(george_st)

		Collections.sort(data)

		return data
	}

	private fun createSydNorthData(): List<TrafficCamera> {
		val data = LinkedList<TrafficCamera>()
		val cumberland_hwy_carlingford = TrafficCamera(
			"Cumberland Highway",
			"Carlingford",
			"Cumberland Highway at Marsden XRoad looking north towards Beecroft.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/cumberlandhwy_carlingford.jpg",
			XRegion.SYD_NORTH, 25)
		data.add(cumberland_hwy_carlingford)
		val falcon_st_crows_nest = TrafficCamera(
			"Falcon Street",
			"Crows Nest",
			"Corner of Falcon Street and the Pacific Highway looking East towards the Warringah Freeway.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/falconst_crowsnest.jpg",
			XRegion.SYD_NORTH, 32)
		data.add(falcon_st_crows_nest)
		// [33] = M2 (Pennant Hills)
		val m2_pennant_hills = TrafficCamera(
			"M2",
			"Pennant Hills",
			"M2 at Pennant Hills XRoad looking west towards Baulkham Hills.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m2_pennanthills.jpg",
			XRegion.SYD_NORTH, 33)
		data.add(m2_pennant_hills)
		// [34] = Manly XRoad (Seaforth)
		val manly_rd = TrafficCamera(
			"Manly XRoad",
			"Seaforth",
			"Corner of Manly XRoad and Battle Boulevard near The Spit Bridge, looking north towards Manly.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/manlyrd.jpg",
			XRegion.SYD_NORTH, 34)
		data.add(manly_rd)
		// [35] = Military XRoad (Neutral Bay)
		val military_rd = TrafficCamera(
			"Military XRoad",
			"Neutral Bay",
			"Military XRoad at Wycombe XRoad looking west towards the Warringah Freeway.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/militaryrd_neutralbay.jpg",
			XRegion.SYD_NORTH, 35)
		data.add(military_rd)
		// [36] = Pacific Highway (Chatswood)
		val pacific_hwy_chatswood = TrafficCamera(
			"Pacific Highway",
			"Chatswood",
			"Corner of the Pacific Highway and Centennial Avenue near Chatswood Park, looking south towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pacific_chats.jpg",
			XRegion.SYD_NORTH, 36)
		data.add(pacific_hwy_chatswood)
		// [37] = Pacific Hwy (Pymble)
		val pacific_hwy_pymble = TrafficCamera(
			"Pacific Highway",
			"Pymble",
			"Pacific Highway at Ryde XRoad looking south-west towards Ryde.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pacific_pymble.jpg",
			XRegion.SYD_NORTH, 37)
		data.add(pacific_hwy_pymble)
		// [38] = Stewart Street (Eastwood)
		val stewart_st = TrafficCamera(
			"Stewart St",
			"Eastwood",
			"Corner of Stewart Street and Marsden XRoad looking west towards Parramatta.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/stewartst_eastwood.jpg",
			XRegion.SYD_NORTH, 38)
		data.add(stewart_st)
		// [39] = Victoria XRoad (Gladesville)
		val victoria_rd = TrafficCamera(
			"Victoria XRoad",
			"Gladesville",
			"Victoria XRoad at Pittwater XRoad looking north-west towards Ryde.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/victoriard_gladesville.jpg",
			XRegion.SYD_NORTH, 39)
		data.add(victoria_rd)
		// [40] = Warringah Fwy (North Sydney)
		val warringah_fwy = TrafficCamera(
			"Warringa Freeway",
			"North Sydney",
			"The Warringah Freeway, approaching the Sydney Harbour Tunnel looking south towards the city.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/warringahfwy.jpg",
			XRegion.SYD_NORTH, 40)
		data.add(warringah_fwy)
		// [41] = Warringah XRoad (Frenchs Forrest)
		val warringah_rd = TrafficCamera(
			"Warringah XRoad",
			"Frenchs Forrest",
			"Warringah XRoad at Forest Way looking west towards Forestville.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/warringahrd_frenchsforest.jpg",
			XRegion.SYD_NORTH, 41)
		data.add(warringah_rd)
		// [42] = Pittwater XRoad (Narrabeen)
		val pittwater_rd_narrabeen = TrafficCamera(
			"Pittwater XRoad",
			"Narrabeen",
			"Pittwater XRoad at Wakehurst Parkway looking north towards Mona Vale.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pittwaterrd_narrabeen.jpg",
			XRegion.SYD_NORTH, 42)
		data.add(pittwater_rd_narrabeen)
		// [43] = Ryde Bridge (Ryde)
		val ryde_bridge = TrafficCamera(
			"Ryde Bridge",
			"Ryde",
			"Church Street crossing Ryde Bridge looking north towards Ryde.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/rydebridge.jpg",
			XRegion.SYD_NORTH, 43)
		data.add(ryde_bridge)
		// [400] = M2 (Ryde)
		val m2_ryde = TrafficCamera(
			"M2",
			"Ryde",
			"M2 at the XLane Cove XRoad exit looking west towards Epping.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m2_ryde.jpg",
			XRegion.SYD_NORTH, 400)
		data.add(m2_ryde)
		// [401] = Epping Rd (Macquarie Park)
		val epping_rd_mac_park = TrafficCamera(
			"Epping XRoad",
			"Macquarie Park",
			"Epping XRoad at Balaclava XRoad looking west towards Epping.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/eppingrd_macquariepark.jpg",
			XRegion.SYD_NORTH, 401)
		data.add(epping_rd_mac_park)
		// [402] = Pennant Hills XRoad (Thornleigh)
		val thornleigh = TrafficCamera(
			"Pennant Hills XRoad",
			"Thornleigh",
			"Pennant Hills XRoad at The Comenarra Parkway looking south towards Carlingford.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pennanthillsrd_thornleigh.jpg",
			XRegion.SYD_NORTH, 402)
		data.add(thornleigh)
		// [403] = Epping XRoad (XLane cove)
		val epping_rd_lane_cove = TrafficCamera(
			"Epping XRoad",
			"XLane Cove",
			"Epping XRoad at Centennial Avenue looking west towards XLane Cove.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/eppingrd_lanecove.jpg",
			XRegion.SYD_NORTH, 403)
		data.add(epping_rd_lane_cove)
		// [404] = Beecroft XRoad (Epping)
		val beecroft_rd = TrafficCamera(
			"Beecroft XRoad",
			"Epping",
			"Beecroft XRoad at Carlingford XRoad looking west towards Carlingford.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/beecroftrd_epping.jpg",
			XRegion.SYD_NORTH, 404)
		data.add(beecroft_rd)
		// [405] = Gore Hill Fwy (Artarmon)
		val gore_fwy_artarmon = TrafficCamera(
			"Gore Hill Freeway",
			"Artarmon",
			"Gore Hill Freeway at Artarmon looking south towards Sydney Harbour Bridge.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/gorehillfwy_artarmon.jpg",
			XRegion.SYD_NORTH, 405)
		data.add(gore_fwy_artarmon)
		// [406] = Warringah XRoad (Forestville)
		val warringah_rd_forestville = TrafficCamera(
			"Warringah XRoad",
			"Forestville",
			"Warringah XRoad at Healey Way looking east towards Frenchs Forest",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/warringahrd_forestville.jpg",
			XRegion.SYD_NORTH, 406)
		data.add(warringah_rd_forestville)

		Collections.sort(data)

		return data
	}

	private fun createSydSouthData(): List<TrafficCamera> {
		val data = LinkedList<TrafficCamera>()
		// [44] = 5 ways (Miranda)
		val five_ways = TrafficCamera(
			"5 Ways",
			"Miranda",
			"5 ways at The Boulevarde looking west towards Sutherland.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/5ways.jpg",
			XRegion.SYD_SOUTH, 44)
		data.add(five_ways)
		// [45] = Hume Hwy (Bankstown)
		val hume_hwy_bankstown = TrafficCamera(
			"Hume Highway",
			"Bankstown",
			"Corner of Hume Highway and Stacey Street looking east towards Strathfield.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_bankstown.jpg",
			XRegion.SYD_SOUTH, 45)
		data.add(hume_hwy_bankstown)
		// [46] = Hume Mwy (Campbelltown)
		val hume_hwy_campbelltown = TrafficCamera(
			"Hume Motorway",
			"Campbelltown",
			"Corner of Hume Motorway and Narellan XRoad looking north towards Liverpool.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_campbelltown.jpg",
			XRegion.SYD_SOUTH, 46)
		data.add(hume_hwy_campbelltown)
		// [47] = Hume highway (liverpool)
		val hume_hwy_liverpool = TrafficCamera(
			"Hume Highway",
			"Liverpool",
			"Corner of Hume Highway and Cumberland Highway looking south towards Casula.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_liverpool.jpg",
			XRegion.SYD_SOUTH, 47)
		data.add(hume_hwy_liverpool)
		// [48] = Hume Motorway (St Andrews)
		val hume_hwy_st_andrews = TrafficCamera(
			"Hume Motorway",
			"St Andrews",
			"Corner of Hume Motorway and Raby XRoad looking north towards Liverpool.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_standrews.jpg",
			XRegion.SYD_SOUTH, 48)
		data.add(hume_hwy_st_andrews)
		// [49] = Hume Hwy (Villawood)
		val hume_hwy_villawood = TrafficCamera(
			"Hume Highway",
			"Villawood",
			"Corner of Hume Highway and Woodville XRoad looking east towards Bass Hill.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_villawood.jpg",
			XRegion.SYD_SOUTH, 49)
		data.add(hume_hwy_villawood)
		// [50] = M5 / M7 (Prestons)
		val m5_m7 = TrafficCamera(
			"M5 / M7",
			"Prestons",
			"Junction of M5 and M7 looking south towards Ingleburn.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_m7.jpg",
			XRegion.SYD_SOUTH, 50)
		data.add(m5_m7)
		// [51] = M5 (LIverpool)
		val m5_liverpool = TrafficCamera(
			"M5",
			"Liverpool",
			"M5 at Hume Highway looking east towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_liverpool.jpg",
			XRegion.SYD_SOUTH, 51)
		data.add(m5_liverpool)
		// [52] = M5 (Milperra)
		val m5_milperra = TrafficCamera(
			"M5",
			"Milperra",
			"M5 Motorway at Henry Lawson Drive looking east towards Beverly Hills.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_milperra.jpg",
			XRegion.SYD_SOUTH, 52)
		data.add(m5_milperra)
		// [500] = Audley XRoad(Audley Weir)
		val audley_rd = TrafficCamera(
			"Audley XRoad",
			"Audley Weir",
			"Audley XRoad at Audley Weir looking east towards Sir Bertram Stevens Drive.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/audleyrd_audley.jpg",
			XRegion.SYD_SOUTH, 500)
		data.add(audley_rd)
		// [501] = A1 Princes Mwy (heathcote)
		val heathcote = TrafficCamera(
			"A1 Princes Motorway",
			"Heathcote",
			"A1 Princes Motorway at Heathcote looking south towards Wollongong.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f6_waterfall.jpg",
			XRegion.SYD_SOUTH, 501)
		data.add(heathcote)
		// [502] = M5 (padstow)
		val padstow = TrafficCamera(
			"M5",
			"Padstow",
			"M5 at Fairford XRoad looking east towards Arncliffe.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_padstow.jpg",
			XRegion.SYD_SOUTH, 502)
		data.add(padstow)
		// [503] = Princes Hwy (sutherland)
		val sutherland = TrafficCamera(
			"Princes Highway",
			"Sutherland",
			"Princes Highway at Acacia XRoad looking south towards Waterfall.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princeshwy_sutherland.jpg",
			XRegion.SYD_SOUTH, 503)
		data.add(sutherland)

		Collections.sort(data)

		return data
	}

	private fun createSydWestData(): List<TrafficCamera> {
		val data = LinkedList<TrafficCamera>()
		val church_st = TrafficCamera(
			"Church Street",
			"Parramatta",
			"Corner of Church Street and Victoria XRoad looking north towards Northmead.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/churchst_parra.jpg",
			XRegion.SYD_WEST, 53)
		data.add(church_st)
		val cumberland_hwy_merrylands = TrafficCamera(
			"Cumberland Highway",
			"Merrylands",
			"Cumberland Highway at Merrylands XRoad looking north towards the M4 Western Motorway.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/cumberlandhwy_merrylands.jpg",
			XRegion.SYD_WEST, 54)
		data.add(cumberland_hwy_merrylands)
		// [55] = James Ruse Drive (Rosehills)
		val james_ruse_dr_rosehill = TrafficCamera(
			"James Ruse Drive",
			"Rosehills",
			"James Ruse Drive at Grand Avenue looking north towards Rydalmere.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/jrd_rosehill.jpg",
			XRegion.SYD_WEST, 55)
		data.add(james_ruse_dr_rosehill)
		// [56] = M4 Western Mwy (Minchinbury)
		val m4_western_motorway = TrafficCamera(
			"M4 Western Motorway",
			"Minchinbury",
			"M4 Western Motorway at Wallgrove XRoad looking east towards Eastern Creek.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_minchinbury.jpg",
			XRegion.SYD_WEST, 56)
		data.add(m4_western_motorway)
		// [57] = M4 Western Mwy (Prospect)
		val m4_prospect = TrafficCamera(
			"M4 Western Motorway",
			"Prospect",
			"M4 Western Motorway at the Prospect Highway exit ramp looking east towards Parramatta.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_prospect.jpg",
			XRegion.SYD_WEST, 57)
		data.add(m4_prospect)
		// [58] = M4 Western Mwy (St marys)
		val m4_st_marys = TrafficCamera(
			"M4 Western Motorway",
			"St Marys",
			"M4 Western Motorway at Mamre XRoad looking west towards Penrith.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_stmarys.jpg",
			XRegion.SYD_WEST, 58)
		data.add(m4_st_marys)
		// [59] = M7 Motorway (Glenwood)
		val m7_glenwood = TrafficCamera(
			"M7 Motorway",
			"Glenwood",
			"M7 Motorway at Sunnyholt XRoad looking east towards Bella Vista.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m7_glenwood.jpg",
			XRegion.SYD_WEST, 59)
		data.add(m7_glenwood)
		// [60] = m7 (horsely park)
		val m7_horsely = TrafficCamera(
			"M7 at The Horsely Drive",
			"Horsley Park",
			"M7 Motorway at The Horsley Drive looking south towards Hoxton Park.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m7_horsleydr.jpg",
			XRegion.SYD_WEST, 60)
		data.add(m7_horsely)
		// [61] = Old windsor rd (beaumont hills)
		val old_windsor_rd = TrafficCamera(
			"Old Windsor XRoad",
			"Beaumont Hills",
			"The intersection of Old Windsor XRoad and Windsor XRoad looking south towards Parramatta.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/oldwindsorrd.jpg",
			XRegion.SYD_WEST, 61)
		data.add(old_windsor_rd)
		// [62] = old windsor rd (winston hills)
		val old_windsor_rd_winston_hills = TrafficCamera(
			"Old Windsor XRoad",
			"Winston Hills",
			"Old Windsor XRoad at Abbott XRoad looking north towards Bella Vista.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/oldwindsorrd_winstonhills.jpg",
			XRegion.SYD_WEST, 62)
		data.add(old_windsor_rd_winston_hills)
		// [63] = parramatta rd (parramatta)
		val parramatta_rd_parramatta = TrafficCamera(
			"Parramatta XRoad",
			"Parramatta",
			"Parramatta XRoad at Woodville XRoad looking east towards Auburn.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_parra.jpg",
			XRegion.SYD_WEST, 63)
		data.add(parramatta_rd_parramatta)
		// [64] = Seven hills road (seven hills)
		val seven_hills_rd = TrafficCamera(
			"Seven Hills XRoad",
			"Seven Hills",
			"Seven Hills XRoad at Abbot XRoad looking west towards Seven Hills.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/sevenhillsrd_sevenhills.jpg",
			XRegion.SYD_WEST, 64)
		data.add(seven_hills_rd)
		// [300] = M4 wetsern mwy (auburn)
		val m4_auburn = TrafficCamera(
			"M4 Western Motorway",
			"Auburn",
			"M4 Western Motorway at Auburn looking west towards St Marys.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_auburn.jpg",
			XRegion.SYD_WEST, 300)
		data.add(m4_auburn)
		// [301] = Greay western highway (hazelbrook)
		val hazelbrook = TrafficCamera(
			"Great Western Highway",
			"Hazelbrook",
			"Great Western Highway at Oaklands XRoad looking east towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/greatwesternhwy_hazelbrook.jpg",
			XRegion.SYD_WEST, 301)
		data.add(hazelbrook)
		// [302] = M4 Western Motorway (Mays Hills)
		val mays_hill = TrafficCamera(
			"M4 Western Motorway",
			"Mays Hill",
			"M4 Western Motorway at Cumberland Highway looking east towards Sydney.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_mayshill.jpg",
			XRegion.SYD_WEST, 302)
		data.add(mays_hill)
		// [303] = Silverwater XRoad (Silverwater)
		val silverwater_rd = TrafficCamera(
			"Silverwater Rd",
			"Silverwater",
			"Silverwater XRoad at M4 Western Motorway looking south towards Auburn.",
			"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/silverwaterrd_silverwater.jpg",
			XRegion.SYD_WEST, 303)
		data.add(silverwater_rd)

		Collections.sort(data)

		return data
	}

	fun getCamera(index: Int): TrafficCamera? {
		var result: TrafficCamera? = null
		val cameraCollections = unfilteredCamerasPerRegion.values
		for (cameraList in cameraCollections) {
			for (camera in cameraList) {
				if (index == camera.index) {
					result = camera
					break
				}
			}
		}

		return result
	}

	fun getCamerasForRegion(region: XRegion): List<TrafficCamera>? {
		return filteredCamerasPerRegion.get(region)
	}

	private fun initUnfiltered() {
		unfilteredCamerasPerRegion.put(XRegion.SYD_MET, createSydMetData())
		unfilteredCamerasPerRegion.put(XRegion.SYD_NORTH, createSydNorthData())
		unfilteredCamerasPerRegion.put(XRegion.SYD_SOUTH, createSydSouthData())
		unfilteredCamerasPerRegion.put(XRegion.SYD_WEST, createSydWestData())
		unfilteredCamerasPerRegion.put(XRegion.REG_NORTH, createRegNorthData())
		unfilteredCamerasPerRegion.put(XRegion.REG_SOUTH, createRegSouthData())
		// Note: There are no cameras in REG_WEST at this time
		addSelfAsListenerToAllCameras()
	}

	private fun addSelfAsListenerToAllCameras() {
		for (camerasInRegion in unfilteredCamerasPerRegion.values) {
			for (camera in camerasInRegion) {
				camera.addPropertyChangeListener(this)
			}
		}
	}

	private fun loadFavourites() {
		MLog.i(TAG, "Loading favourites")

		// To begin with, mark all as NOT extraFavourite
		for (camerasInRegion in unfilteredCamerasPerRegion.values) {
			for (camera in camerasInRegion) {
				camera.setFavouriteSilently(false)
			}
		}

		val prefs: SharedPreferences? = ctx.getSharedPreferences(FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE)
		val favouriteCameraIds = prefs?.getStringSet(FAVOURITE_STATE_PREF_KEY, null)

		MLog.i(TAG, "Favourite cameras as loaded from prefs is: " + favouriteCameraIds)

		if (favouriteCameraIds != null) {
			for (cameraId in favouriteCameraIds) {
				MLog.i(TAG, cameraId + ",")
			}

			for (camerasInRegion in unfilteredCamerasPerRegion.values) {
				for (camera in camerasInRegion) {
					if (favouriteCameraIds.contains(camera.index.toString())) {
						MLog.i(TAG, "Calling setFavourite(true) for camera " + camera.index)
						camera.isFavourite = true
					}
				}
			}
		}
	}

	/**
	 * We save those cameras that ARE favourites. Applies to all known cameras -
	 * even those not admitted by the current filter.
	 */
	fun saveFavourites() {
		MLog.d(TAG, "Saving extraFavourite states for all cameras")
		val favouriteCameraIds = HashSet<String>()

		for (cameras in unfilteredCamerasPerRegion.values) {
			for (camera in cameras) {
				if (camera.isFavourite) {
					favouriteCameraIds.add(camera.index.toString())
				}
			}
		}
		val prefs = ctx.getSharedPreferences(FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE)
		val editor = prefs.edit()
		editor.putStringSet(FAVOURITE_STATE_PREF_KEY, favouriteCameraIds)
		editor.commit()
	}

	fun setCameraFavourite(cameraIndex: Int, isFavourite: Boolean) {
		MLog.i(TAG, "Set in database: camera " + cameraIndex
			+ " has extraFavourite status = " + isFavourite)
	}

	fun setFilter(newfilter: ITrafficCameraFilter) {
		filter = newfilter

		filteredCamerasPerRegion.clear()

		for (region in unfilteredCamerasPerRegion.keys) {
			val unfilteredCameras: List<TrafficCamera>? = unfilteredCamerasPerRegion[region]

			if ((unfilteredCameras != null) && (!unfilteredCameras.isEmpty())) {
				val filteredCameras = LinkedList<TrafficCamera>()

				for (camera in unfilteredCameras) {
					if (newfilter.admit(camera)) {
						filteredCameras.add(camera)
					}
				}

				if (!filteredCameras.isEmpty()) {
					filteredCamerasPerRegion.put(region, filteredCameras)
				}
			}
		}
	}

	override fun propertyChange(event: PropertyChangeEvent) {
		MLog.d(TAG, "***TCDb " + hashCode() + " receives PCE for event " + event.propertyName)
		// TODO Auto-generated method stub
		if (TrafficCamera.PROPERTY_FAVOURITE == event.propertyName) {
			MLog.d(TAG, "TCDb receives notice that extraFavourite state of a camera has changed.")
			saveFavourites()
			fireFavouritePropertyChangeEvent()
		}
	}
}
