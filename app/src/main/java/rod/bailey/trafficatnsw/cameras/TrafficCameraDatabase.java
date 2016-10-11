package rod.bailey.trafficatnsw.cameras;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter;
import rod.bailey.trafficatnsw.json.hazard.XRegion;
import rod.bailey.trafficatnsw.util.MLog;

public class TrafficCameraDatabase implements PropertyChangeListener {
	
	public static final String PROPERTY_FAVOURITE_SET = "favouriteSet";

	private static final String FAVOURITE_CAMERAS_FILE_NAME = "favourite_cameras";

	private static final String FAVOURITE_STATE_PREF_KEY = "FAVOURITE";

	private static TrafficCameraDatabase singleton = new TrafficCameraDatabase();

	private static final String TAG = TrafficCameraDatabase.class
			.getSimpleName();

	public static TrafficCameraDatabase getInstance() {
		return singleton;
	}

	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	private ITrafficCameraFilter filter;

	private final Map<XRegion, List<TrafficCamera>> filteredCamerasPerRegion = new HashMap<XRegion, List<TrafficCamera>>();

	// Key = XRegion
	// Value = List of TrafficCamera instances
	private final Map<XRegion, List<TrafficCamera>> unfilteredCamerasPerRegion = new HashMap<XRegion, List<TrafficCamera>>();
	
	private Context ctx;

	private TrafficCameraDatabase() {
		initUnfiltered();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	private void fireFavouritePropertyChangeEvent() {
		support.firePropertyChange(PROPERTY_FAVOURITE_SET, null, this);
	}
	
	public void init(Context ctx) {
		this.ctx = ctx;
		loadFavourites();
	}

	private List<TrafficCamera> createRegNorthData() {
		List<TrafficCamera> data = new LinkedList<TrafficCamera>();

		TrafficCamera hexham = new TrafficCamera(
				"New England Highway",
				"Hexham",
				"New England Highway near Hexham Bridge looking south towards Newcastle.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/newenglandhwy_hexham.jpg",
				XRegion.REG_NORTH, 100);
		hexham.setFavourite(false);
		data.add(hexham);

		TrafficCamera newcastle_rd = new TrafficCamera(
				"Newcastle XRoad",
				"Newcastle",
				"Newcastle XRoad at Croudace Street looking west towards M1 Pacific Motorway.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/newcastlelinkrd_newcastle.jpg",
				XRegion.REG_NORTH, 101);
		data.add(newcastle_rd);

		// [108] = M1 Pacific Mwy (John Renshaw Dr)
		TrafficCamera renshaw_dr = new TrafficCamera(
				"M1 Pacific Motorway",
				"John Renshaw Drive",
				"M1 Pacific Motorway at John Renshaw Drive looking south towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_johnrenshawdr.jpg",
				XRegion.REG_NORTH, 108);
		data.add(renshaw_dr);

		// [109] = M1 Pacific Mwy (Sparks road)
		TrafficCamera sparks_rd = new TrafficCamera(
				"M1 Pacific Motorway",
				"Sparks XRoad",
				"M1 Pacific Motorway at Sparks XRoad looking north towards Newcastle.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_sparkesrd.jpg",
				XRegion.REG_NORTH, 109);
		data.add(sparks_rd);

		// [26] = M1 Pacific Mwy (Kariong)
		TrafficCamera f3_kariong = new TrafficCamera(
				"M1 Pacific Motorway",
				"Kariong",
				"M1 Pacific Motorway at the Kariong on ramp looking south towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_kariong.jpg",
				XRegion.REG_NORTH, 26);
		data.add(f3_kariong);

		// [27] = M1 (hawkesbuy river)
		TrafficCamera f3_hawkesbury = new TrafficCamera(
				"M1 Pacific Motorway",
				"Hawkesbury River",
				"M1 Pacific Motorway crossing the Hawkesbury River Bridge looking south towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_mooney.jpg",
				XRegion.REG_NORTH, 27);
		data.add(f3_hawkesbury);

		// [28] = m1 pacific mwy (mount white)
		TrafficCamera f3_mt_white = new TrafficCamera(
				"M1 Pacific Motorway",
				"Mount White",
				"M1 Pacific Motorway at the Mount White heavy vehicle checking station looking north towards Calga.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_mountwhite.jpg",
				XRegion.REG_NORTH, 28);
		data.add(f3_mt_white);

		// [29] = m1 pacific mwy (ourimbah)
		TrafficCamera f3_ourimbah = new TrafficCamera(
				"M1 Pacific Motorway",
				"Ourimbah",
				"M1 Pacific Motorway at Ourimbah on ramp looking north towards Wyong.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_ourimbah.jpg",
				XRegion.REG_NORTH, 29);
		data.add(f3_ourimbah);

		// [30] = m1 pacific mwy (wahroonga)
		TrafficCamera f3_wahroonga = new TrafficCamera(
				"M1 Pacific Motorway",
				"Wahroonga",
				"The Pacific Highway on ramp to the M1 Pacific Motorway looking north towards the Central Coast.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_wahroonga.jpg",
				XRegion.REG_NORTH, 30);
		data.add(f3_wahroonga);

		// [31] = m1 pacific mwy (windy banks)
		TrafficCamera f3_windy = new TrafficCamera(
				"M1 Pacific Motorway",
				"Windy Banks",
				"M1 Pacific Motorway at the Windy Banks on ramp looking south towards Hornsby.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f3_windybanks.jpg",
				XRegion.REG_NORTH, 31);
		data.add(f3_windy);

		Collections.sort(data);

		return data;
	}

	private List<TrafficCamera> createRegSouthData() {
		List<TrafficCamera> data = new LinkedList<TrafficCamera>();

		TrafficCamera kosciuszko_rd = new TrafficCamera(
				"Kosciuszko XRoad",
				"Wilson's Valley",
				"Wilson's Valley chain bay looking south-west towards Perisher Valley.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/kosciuszkord_wilsonsvalley.jpg",
				XRegion.REG_SOUTH, 65);
		data.add(kosciuszko_rd);

		TrafficCamera mt_ousley = new TrafficCamera(
				"M1 Princes Motorway",
				"Mt Ousley",
				"M1 Princes Motorway at Mt Ousley XRoad looking north towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f6_mtousley.jpg",
				XRegion.REG_SOUTH, 200);
		data.add(mt_ousley);

		// [201] = Princes Hwy (Albion park rail)
		TrafficCamera albion = new TrafficCamera(
				"Princes Highway",
				"Albion Park Rail",
				"Princes Highway at Airport XRoad looking north towards Wollongong.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princeshwy_albionparkrail.jpg",
				XRegion.REG_SOUTH, 201);
		data.add(albion);

		// [202] = Princes Hwy (Wollongong)
		TrafficCamera wollongong = new TrafficCamera(
				"Princes Highway",
				"Wollongong",
				"Princes Highway near Bourke Street looking north towards Bulli.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princeshwy_wollongong.jpg",
				XRegion.REG_SOUTH, 202);
		data.add(wollongong);

		Collections.sort(data);

		return data;
	}

	private List<TrafficCamera> createSydMetData() {
		List<TrafficCamera> data = new LinkedList<TrafficCamera>();

		TrafficCamera king_georges_rd = new TrafficCamera(
				"King Georges XRoad",
				"Beverly Hills",
				"Corner of King Georges XRoad and Stoney Creek XRoad looking north towards Beverly Hills.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/knggrd_beverlyhills.jpg",
				XRegion.SYD_MET, 1);
		king_georges_rd.setFavourite(false);
		data.add(king_georges_rd);

		TrafficCamera general_holmes_dr = new TrafficCamera(
				"General Holmes Drive",
				"Botany",
				"General Holmes Drive at the east end of the Airport Tunnel looking east towards Botany.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/ghd_airport.jpg",
				XRegion.SYD_MET, 2);
		general_holmes_dr.setFavourite(false);
		data.add(general_holmes_dr);

		TrafficCamera william_st_east_syd = new TrafficCamera(
				"William Street",
				"East Sydney",
				"Corner of William Street and College Street near Hyde Park looking east towards Kings Cross.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/williamst.jpg",
				XRegion.SYD_MET, 3);
		data.add(william_st_east_syd);

		TrafficCamera gladesville_bridge = new TrafficCamera(
				"Gladesville Bridge",
				"Drummoyne",
				"Gladesville Bridge looking west towards Gladesville.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/gladesvillebr.jpg",
				XRegion.SYD_MET, 4);
		data.add(gladesville_bridge);
		
		TrafficCamera hume_hwy_ashfield = new TrafficCamera(
				"Hume Highway",
				"Ashfield",
				"Corner of Hume Highway and Roberts XRoad looking west towards Strathfield.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_ashfield.jpg",
				XRegion.SYD_MET, 5);
		data.add(hume_hwy_ashfield);

		TrafficCamera hume_hwy_strathfield = new TrafficCamera(
				"Hume Highway",
				"Strathfield",
				"Corner of Hume Highway and Roberts XRoad looking west towards Yagoona.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_strathfield.jpg",
				XRegion.SYD_MET, 6);
		data.add(hume_hwy_strathfield);

		// City West Link (Lilyfield)
		TrafficCamera city_west = new TrafficCamera(
				"City West Link",
				"Lilyfield",
				"City West Link at Lilyfield looking east towards Victoria XRoad intersection.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/citywestlink.jpg",
				XRegion.SYD_MET, 7);
		data.add(city_west);

		// Easter distributor (Kensington)
		TrafficCamera eastern_dist = new TrafficCamera(
				"Eastern Distributor",
				"Kensington",
				"Southern end of the Eastern Distributor tunnel near the Link XRoad on-ramp looking north towards Sydney",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/easterndist.jpg",
				XRegion.SYD_MET, 8);
		data.add(eastern_dist);

		// Alison XRoad (Randwick)
		TrafficCamera alison_road = new TrafficCamera(
				"Alison XRoad",
				"Randwick",
				"Alison XRoad at Darley Street looking north-west towards Anzac Parade.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/alisonrd_randwick.jpg",
				XRegion.SYD_MET, 9);
		data.add(alison_road);

		// Anzac Bridge (Rozelle)
		TrafficCamera anzac_bridge = new TrafficCamera(
				"Anzac Bridge",
				"Rozelle",
				"Intersection of Victoria XRoad and Anzac Bridge looking east towards the Sydney CBD.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/anzacbr.jpg",
				XRegion.SYD_MET, 10);
		data.add(anzac_bridge);

		// Anzac Parade (Moore Park)
		TrafficCamera anzac_pde = new TrafficCamera(
				"Anzac Parade",
				"Moore Park",
				"Corner of Anzac Parade and Cleveland Street looking south towards Randwick.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/anzacpde.jpg",
				XRegion.SYD_MET, 11);
		data.add(anzac_pde);

		// [12] = M4 Western Mwy (Olympic Park)
		TrafficCamera m4_olympic_park = new TrafficCamera(
				"M4 Western Motorway",
				"Olympic Park",
				"M4 at Sydney Olympic Park looking east towards Strathfield.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_olympic.jpg",
				XRegion.SYD_MET, 12);
		data.add(m4_olympic_park);

		// [13] = M5 East (Beverly Hills)
		TrafficCamera m5_east = new TrafficCamera(
				"M5 East",
				"Beverly Hills",
				"M5 East at the King Georges XRoad on ramp looking west towards Riverwood.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5east.jpg",
				XRegion.SYD_MET, 13);
		data.add(m5_east);

		// [14] = Parramatta XRoad (Leichardt)
		TrafficCamera parramatta_rd_leichardt = new TrafficCamera(
				"Parramatta XRoad",
				"Leichardt",
				"Parramatta XRoad at Elswick Street looking west towards Ashfield.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_leichhardt.jpg",
				XRegion.SYD_MET, 14);
		data.add(parramatta_rd_leichardt);

		// [15] = Parramatta XRoad (Silverwater)
		TrafficCamera parramatta_rd_silverwater = new TrafficCamera(
				"Parramatta XRoad",
				"Silverwater",
				"Parramatta XRoad at Silverwater XRoad looking west towards Parramatta.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_silverwater.jpg",
				XRegion.SYD_MET, 15);
		data.add(parramatta_rd_silverwater);

		// [16] = Parramatta XRoad (Strathfield)
		TrafficCamera parramatta_rd_strathfield = new TrafficCamera(
				"Parramatta XRoad",
				"Strathfield",
				"Parramatta XRoad at Leicester Avenue looking east towards Burwood.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_strathfield.jpg",
				XRegion.SYD_MET, 16);
		data.add(parramatta_rd_strathfield);

		// [17] = Princes Hwy (Blakehurst)
		TrafficCamera princes_hwy_blakehurst = new TrafficCamera(
				"Princes Highway",
				"Blakehurst",
				"Princes Highway at the King Georges XRoad intersection looking south towards Sutherland.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princes_blakehurst.jpg",
				XRegion.SYD_MET, 17);
		data.add(princes_hwy_blakehurst);

		// [18] = Princes Hwy (Kogarah)
		TrafficCamera princes_hwy_kogarah = new TrafficCamera(
				"Princes Highway",
				"Kogarah",
				"Princes Highway at President Avenue looking south towards Ramsgate.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princes_kogarah.jpg",
				XRegion.SYD_MET, 18);
		data.add(princes_hwy_kogarah);

		// [19] = Princes Hwy (St Peters)
		TrafficCamera princes_hwy_st_peters = new TrafficCamera(
				"Princes Highway",
				"St Peters",
				"Princes Highway at the Canal XRoad intersection looking north towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princes_stpeters.jpg",
				XRegion.SYD_MET, 19);
		data.add(princes_hwy_st_peters);

		// [20] = Southern Cross Drive (Eastlakes)
		TrafficCamera southern_cross_dr = new TrafficCamera(
				"Southern Cross Drive",
				"Eastlakes",
				"Southern Cross Drive at Wentworth Avenue looking south towards Mascot.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/scd_eastlakes.jpg",
				XRegion.SYD_MET, 20);
		data.add(southern_cross_dr);

		// [21] = Syd Einfield Drive (Bondi Junction)
		TrafficCamera syd_einfield_dr = new TrafficCamera(
				"Syd Einfield Drive",
				"Bondi Junction",
				"Syd Einfeld Drive looking west towards Sydney CBD.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/sed_bondijunction.jpg",
				XRegion.SYD_MET, 21);
		data.add(syd_einfield_dr);

		// [22] = SHB (Milsons Pt)
		TrafficCamera shb = new TrafficCamera(
				"Sydney Harbour Bridge",
				"Milsons Point",
				"Sydney Harbour Bridge deck looking south towards the Sydney CBD.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/harbourbridge.jpg",
				XRegion.SYD_MET, 22);
		data.add(shb);

		// [23] = The Grand Pde (Brighton-le-sands)
		TrafficCamera grand_pde = new TrafficCamera(
				"The Grand Parade",
				"Brighton-Le-Sands",
				"The Grand Parade at Bay Street looking north towards Sydney Airport.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/grandpde_bls.jpg",
				XRegion.SYD_MET, 23);
		data.add(grand_pde);

		// [24] = George St (Railway Sq)
		TrafficCamera george_st = new TrafficCamera(
				"George Street",
				"Railway square",
				"Railway Square, George Street looking north towards Sydney CBD.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/georgest.jpg",
				XRegion.SYD_MET, 24);
		data.add(george_st);

		Collections.sort(data);

		return data;
	}

	private List<TrafficCamera> createSydNorthData() {
		List<TrafficCamera> data = new LinkedList<TrafficCamera>();

		TrafficCamera cumberland_hwy_carlingford = new TrafficCamera(
				"Cumberland Highway",
				"Carlingford",
				"Cumberland Highway at Marsden XRoad looking north towards Beecroft.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/cumberlandhwy_carlingford.jpg",
				XRegion.SYD_NORTH, 25);
		data.add(cumberland_hwy_carlingford);

		TrafficCamera falcon_st_crows_nest = new TrafficCamera(
				"Falcon Street",
				"Crows Nest",
				"Corner of Falcon Street and the Pacific Highway looking East towards the Warringah Freeway.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/falconst_crowsnest.jpg",
				XRegion.SYD_NORTH, 32);
		data.add(falcon_st_crows_nest);

		// [33] = M2 (Pennant Hills)
		TrafficCamera m2_pennant_hills = new TrafficCamera(
				"M2",
				"Pennant Hills",
				"M2 at Pennant Hills XRoad looking west towards Baulkham Hills.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m2_pennanthills.jpg",
				XRegion.SYD_NORTH, 33);
		data.add(m2_pennant_hills);

		// [34] = Manly XRoad (Seaforth)
		TrafficCamera manly_rd = new TrafficCamera(
				"Manly XRoad",
				"Seaforth",
				"Corner of Manly XRoad and Battle Boulevard near The Spit Bridge, looking north towards Manly.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/manlyrd.jpg",
				XRegion.SYD_NORTH, 34);
		data.add(manly_rd);

		// [35] = Military XRoad (Neutral Bay)
		TrafficCamera military_rd = new TrafficCamera(
				"Military XRoad",
				"Neutral Bay",
				"Military XRoad at Wycombe XRoad looking west towards the Warringah Freeway.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/militaryrd_neutralbay.jpg",
				XRegion.SYD_NORTH, 35);
		data.add(military_rd);

		// [36] = Pacific Highway (Chatswood)
		TrafficCamera pacific_hwy_chatswood = new TrafficCamera(
				"Pacific Highway",
				"Chatswood",
				"Corner of the Pacific Highway and Centennial Avenue near Chatswood Park, looking south towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pacific_chats.jpg",
				XRegion.SYD_NORTH, 36);
		data.add(pacific_hwy_chatswood);

		// [37] = Pacific Hwy (Pymble)
		TrafficCamera pacific_hwy_pymble = new TrafficCamera(
				"Pacific Highway",
				"Pymble",
				"Pacific Highway at Ryde XRoad looking south-west towards Ryde.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pacific_pymble.jpg",
				XRegion.SYD_NORTH, 37);
		data.add(pacific_hwy_pymble);

		// [38] = Stewart Street (Eastwood)
		TrafficCamera stewart_st = new TrafficCamera(
				"Stewart St",
				"Eastwood",
				"Corner of Stewart Street and Marsden XRoad looking west towards Parramatta.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/stewartst_eastwood.jpg",
				XRegion.SYD_NORTH, 38);
		data.add(stewart_st);

		// [39] = Victoria XRoad (Gladesville)
		TrafficCamera victoria_rd = new TrafficCamera(
				"Victoria XRoad",
				"Gladesville",
				"Victoria XRoad at Pittwater XRoad looking north-west towards Ryde.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/victoriard_gladesville.jpg",
				XRegion.SYD_NORTH, 39);
		data.add(victoria_rd);

		// [40] = Warringah Fwy (North Sydney)
		TrafficCamera warringah_fwy = new TrafficCamera(
				"Warringa Freeway",
				"North Sydney",
				"The Warringah Freeway, approaching the Sydney Harbour Tunnel looking south towards the city.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/warringahfwy.jpg",
				XRegion.SYD_NORTH, 40);
		data.add(warringah_fwy);

		// [41] = Warringah XRoad (Frenchs Forrest)
		TrafficCamera warringah_rd = new TrafficCamera(
				"Warringah XRoad",
				"Frenchs Forrest",
				"Warringah XRoad at Forest Way looking west towards Forestville.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/warringahrd_frenchsforest.jpg",
				XRegion.SYD_NORTH, 41);
		data.add(warringah_rd);

		// [42] = Pittwater XRoad (Narrabeen)
		TrafficCamera pittwater_rd_narrabeen = new TrafficCamera(
				"Pittwater XRoad",
				"Narrabeen",
				"Pittwater XRoad at Wakehurst Parkway looking north towards Mona Vale.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pittwaterrd_narrabeen.jpg",
				XRegion.SYD_NORTH, 42);
		data.add(pittwater_rd_narrabeen);

		// [43] = Ryde Bridge (Ryde)
		TrafficCamera ryde_bridge = new TrafficCamera(
				"Ryde Bridge",
				"Ryde",
				"Church Street crossing Ryde Bridge looking north towards Ryde.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/rydebridge.jpg",
				XRegion.SYD_NORTH, 43);
		data.add(ryde_bridge);

		// [400] = M2 (Ryde)
		TrafficCamera m2_ryde = new TrafficCamera(
				"M2",
				"Ryde",
				"M2 at the XLane Cove XRoad exit looking west towards Epping.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m2_ryde.jpg",
				XRegion.SYD_NORTH, 400);
		data.add(m2_ryde);

		// [401] = Epping Rd (Macquarie Park)
		TrafficCamera epping_rd_mac_park = new TrafficCamera(
				"Epping XRoad",
				"Macquarie Park",
				"Epping XRoad at Balaclava XRoad looking west towards Epping.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/eppingrd_macquariepark.jpg",
				XRegion.SYD_NORTH, 401);
		data.add(epping_rd_mac_park);

		// [402] = Pennant Hills XRoad (Thornleigh)
		TrafficCamera thornleigh = new TrafficCamera(
				"Pennant Hills XRoad",
				"Thornleigh",
				"Pennant Hills XRoad at The Comenarra Parkway looking south towards Carlingford.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/pennanthillsrd_thornleigh.jpg",
				XRegion.SYD_NORTH, 402);
		data.add(thornleigh);

		// [403] = Epping XRoad (XLane cove)
		TrafficCamera epping_rd_lane_cove = new TrafficCamera(
				"Epping XRoad",
				"XLane Cove",
				"Epping XRoad at Centennial Avenue looking west towards XLane Cove.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/eppingrd_lanecove.jpg",
				XRegion.SYD_NORTH, 403);
		data.add(epping_rd_lane_cove);

		// [404] = Beecroft XRoad (Epping)
		TrafficCamera beecroft_rd = new TrafficCamera(
				"Beecroft XRoad",
				"Epping",
				"Beecroft XRoad at Carlingford XRoad looking west towards Carlingford.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/beecroftrd_epping.jpg",
				XRegion.SYD_NORTH, 404);
		data.add(beecroft_rd);

		// [405] = Gore Hill Fwy (Artarmon)
		TrafficCamera gore_fwy_artarmon = new TrafficCamera(
				"Gore Hill Freeway",
				"Artarmon",
				"Gore Hill Freeway at Artarmon looking south towards Sydney Harbour Bridge.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/gorehillfwy_artarmon.jpg",
				XRegion.SYD_NORTH, 405);
		data.add(gore_fwy_artarmon);

		// [406] = Warringah XRoad (Forestville)
		TrafficCamera warringah_rd_forestville = new TrafficCamera(
				"Warringah XRoad",
				"Forestville",
				"Warringah XRoad at Healey Way looking east towards Frenchs Forest",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/warringahrd_forestville.jpg",
				XRegion.SYD_NORTH, 406);
		data.add(warringah_rd_forestville);

		Collections.sort(data);

		return data;
	}

	private List<TrafficCamera> createSydSouthData() {
		List<TrafficCamera> data = new LinkedList<TrafficCamera>();

		// [44] = 5 ways (Miranda)
		TrafficCamera five_ways = new TrafficCamera(
				"5 Ways",
				"Miranda",
				"5 ways at The Boulevarde looking west towards Sutherland.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/5ways.jpg",
				XRegion.SYD_SOUTH, 44);
		data.add(five_ways);

		// [45] = Hume Hwy (Bankstown)
		TrafficCamera hume_hwy_bankstown = new TrafficCamera(
				"Hume Highway",
				"Bankstown",
				"Corner of Hume Highway and Stacey Street looking east towards Strathfield.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_bankstown.jpg",
				XRegion.SYD_SOUTH, 45);
		data.add(hume_hwy_bankstown);

		// [46] = Hume Mwy (Campbelltown)
		TrafficCamera hume_hwy_campbelltown = new TrafficCamera(
				"Hume Motorway",
				"Campbelltown",
				"Corner of Hume Motorway and Narellan XRoad looking north towards Liverpool.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_campbelltown.jpg",
				XRegion.SYD_SOUTH, 46);
		data.add(hume_hwy_campbelltown);

		// [47] = Hume highway (liverpool)
		TrafficCamera hume_hwy_liverpool = new TrafficCamera(
				"Hume Highway",
				"Liverpool",
				"Corner of Hume Highway and Cumberland Highway looking south towards Casula.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_liverpool.jpg",
				XRegion.SYD_SOUTH, 47);
		data.add(hume_hwy_liverpool);

		// [48] = Hume Motorway (St Andrews)
		TrafficCamera hume_hwy_st_andrews = new TrafficCamera(
				"Hume Motorway",
				"St Andrews",
				"Corner of Hume Motorway and Raby XRoad looking north towards Liverpool.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_standrews.jpg",
				XRegion.SYD_SOUTH, 48);
		data.add(hume_hwy_st_andrews);

		// [49] = Hume Hwy (Villawood)
		TrafficCamera hume_hwy_villawood = new TrafficCamera(
				"Hume Highway",
				"Villawood",
				"Corner of Hume Highway and Woodville XRoad looking east towards Bass Hill.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/humehwy_villawood.jpg",
				XRegion.SYD_SOUTH, 49);
		data.add(hume_hwy_villawood);

		// [50] = M5 / M7 (Prestons)
		TrafficCamera m5_m7 = new TrafficCamera(
				"M5 / M7",
				"Prestons",
				"Junction of M5 and M7 looking south towards Ingleburn.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_m7.jpg",
				XRegion.SYD_SOUTH, 50);
		data.add(m5_m7);

		// [51] = M5 (LIverpool)
		TrafficCamera m5_liverpool = new TrafficCamera(
				"M5",
				"Liverpool",
				"M5 at Hume Highway looking east towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_liverpool.jpg",
				XRegion.SYD_SOUTH, 51);
		data.add(m5_liverpool);

		// [52] = M5 (Milperra)
		TrafficCamera m5_milperra = new TrafficCamera(
				"M5",
				"Milperra",
				"M5 Motorway at Henry Lawson Drive looking east towards Beverly Hills.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_milperra.jpg",
				XRegion.SYD_SOUTH, 52);
		data.add(m5_milperra);

		// [500] = Audley XRoad(Audley Weir)
		TrafficCamera audley_rd = new TrafficCamera(
				"Audley XRoad",
				"Audley Weir",
				"Audley XRoad at Audley Weir looking east towards Sir Bertram Stevens Drive.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/audleyrd_audley.jpg",
				XRegion.SYD_SOUTH, 500);
		data.add(audley_rd);

		// [501] = A1 Princes Mwy (heathcote)
		TrafficCamera heathcote = new TrafficCamera(
				"A1 Princes Motorway",
				"Heathcote",
				"A1 Princes Motorway at Heathcote looking south towards Wollongong.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/f6_waterfall.jpg",
				XRegion.SYD_SOUTH, 501);
		data.add(heathcote);

		// [502] = M5 (padstow)
		TrafficCamera padstow = new TrafficCamera(
				"M5",
				"Padstow",
				"M5 at Fairford XRoad looking east towards Arncliffe.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m5_padstow.jpg",
				XRegion.SYD_SOUTH, 502);
		data.add(padstow);

		// [503] = Princes Hwy (sutherland)
		TrafficCamera sutherland = new TrafficCamera(
				"Princes Highway",
				"Sutherland",
				"Princes Highway at Acacia XRoad looking south towards Waterfall.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/princeshwy_sutherland.jpg",
				XRegion.SYD_SOUTH, 503);
		data.add(sutherland);

		Collections.sort(data);

		return data;
	}

	private List<TrafficCamera> createSydWestData() {
		List<TrafficCamera> data = new LinkedList<TrafficCamera>();

		TrafficCamera church_st = new TrafficCamera(
				"Church Street",
				"Parramatta",
				"Corner of Church Street and Victoria XRoad looking north towards Northmead.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/churchst_parra.jpg",
				XRegion.SYD_WEST, 53);
		data.add(church_st);

		TrafficCamera cumberland_hwy_merrylands = new TrafficCamera(
				"Cumberland Highway",
				"Merrylands",
				"Cumberland Highway at Merrylands XRoad looking north towards the M4 Western Motorway.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/cumberlandhwy_merrylands.jpg",
				XRegion.SYD_WEST, 54);
		data.add(cumberland_hwy_merrylands);

		// [55] = James Ruse Drive (Rosehills)
		TrafficCamera james_ruse_dr_rosehill = new TrafficCamera(
				"James Ruse Drive",
				"Rosehills",
				"James Ruse Drive at Grand Avenue looking north towards Rydalmere.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/jrd_rosehill.jpg",
				XRegion.SYD_WEST, 55);
		data.add(james_ruse_dr_rosehill);

		// [56] = M4 Western Mwy (Minchinbury)
		TrafficCamera m4_western_motorway = new TrafficCamera(
				"M4 Western Motorway",
				"Minchinbury",
				"M4 Western Motorway at Wallgrove XRoad looking east towards Eastern Creek.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_minchinbury.jpg",
				XRegion.SYD_WEST, 56);
		data.add(m4_western_motorway);

		// [57] = M4 Western Mwy (Prospect)
		TrafficCamera m4_prospect = new TrafficCamera(
				"M4 Western Motorway",
				"Prospect",
				"M4 Western Motorway at the Prospect Highway exit ramp looking east towards Parramatta.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_prospect.jpg",
				XRegion.SYD_WEST, 57);
		data.add(m4_prospect);

		// [58] = M4 Western Mwy (St marys)
		TrafficCamera m4_st_marys = new TrafficCamera(
				"M4 Western Motorway",
				"St Marys",
				"M4 Western Motorway at Mamre XRoad looking west towards Penrith.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_stmarys.jpg",
				XRegion.SYD_WEST, 58);
		data.add(m4_st_marys);

		// [59] = M7 Motorway (Glenwood)
		TrafficCamera m7_glenwood = new TrafficCamera(
				"M7 Motorway",
				"Glenwood",
				"M7 Motorway at Sunnyholt XRoad looking east towards Bella Vista.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m7_glenwood.jpg",
				XRegion.SYD_WEST, 59);
		data.add(m7_glenwood);

		// [60] = m7 (horsely park)
		TrafficCamera m7_horsely = new TrafficCamera(
				"M7 at The Horsely Drive",
				"Horsley Park",
				"M7 Motorway at The Horsley Drive looking south towards Hoxton Park.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m7_horsleydr.jpg",
				XRegion.SYD_WEST, 60);
		data.add(m7_horsely);

		// [61] = Old windsor rd (beaumont hills)
		TrafficCamera old_windsor_rd = new TrafficCamera(
				"Old Windsor XRoad",
				"Beaumont Hills",
				"The intersection of Old Windsor XRoad and Windsor XRoad looking south towards Parramatta.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/oldwindsorrd.jpg",
				XRegion.SYD_WEST, 61);
		data.add(old_windsor_rd);

		// [62] = old windsor rd (winston hills)
		TrafficCamera old_windsor_rd_winston_hills = new TrafficCamera(
				"Old Windsor XRoad",
				"Winston Hills",
				"Old Windsor XRoad at Abbott XRoad looking north towards Bella Vista.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/oldwindsorrd_winstonhills.jpg",
				XRegion.SYD_WEST, 62);
		data.add(old_windsor_rd_winston_hills);

		// [63] = parramatta rd (parramatta)
		TrafficCamera parramatta_rd_parramatta = new TrafficCamera(
				"Parramatta XRoad",
				"Parramatta",
				"Parramatta XRoad at Woodville XRoad looking east towards Auburn.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/parrard_parra.jpg",
				XRegion.SYD_WEST, 63);
		data.add(parramatta_rd_parramatta);

		// [64] = Seven hills road (seven hills)
		TrafficCamera seven_hills_rd = new TrafficCamera(
				"Seven Hills XRoad",
				"Seven Hills",
				"Seven Hills XRoad at Abbot XRoad looking west towards Seven Hills.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/sevenhillsrd_sevenhills.jpg",
				XRegion.SYD_WEST, 64);
		data.add(seven_hills_rd);

		// [300] = M4 wetsern mwy (auburn)
		TrafficCamera m4_auburn = new TrafficCamera(
				"M4 Western Motorway",
				"Auburn",
				"M4 Western Motorway at Auburn looking west towards St Marys.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_auburn.jpg",
				XRegion.SYD_WEST, 300);
		data.add(m4_auburn);

		// [301] = Greay western highway (hazelbrook)
		TrafficCamera hazelbrook = new TrafficCamera(
				"Great Western Highway",
				"Hazelbrook",
				"Great Western Highway at Oaklands XRoad looking east towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/greatwesternhwy_hazelbrook.jpg",
				XRegion.SYD_WEST, 301);
		data.add(hazelbrook);

		// [302] = M4 Western Motorway (Mays Hills)
		TrafficCamera mays_hill = new TrafficCamera(
				"M4 Western Motorway",
				"Mays Hill",
				"M4 Western Motorway at Cumberland Highway looking east towards Sydney.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/m4_mayshill.jpg",
				XRegion.SYD_WEST, 302);
		data.add(mays_hill);

		// [303] = Silverwater XRoad (Silverwater)
		TrafficCamera silverwater_rd = new TrafficCamera(
				"Silverwater Rd",
				"Silverwater",
				"Silverwater XRoad at M4 Western Motorway looking south towards Auburn.",
				"http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/silverwaterrd_silverwater.jpg",
				XRegion.SYD_WEST, 303);
		data.add(silverwater_rd);

		Collections.sort(data);

		return data;
	}

	public TrafficCamera getCamera(int index) {
		TrafficCamera result = null;

		Collection<List<TrafficCamera>> cameraCollections = unfilteredCamerasPerRegion
				.values();
		for (List<TrafficCamera> cameraList : cameraCollections) {
			for (TrafficCamera camera : cameraList) {
				if (index == camera.getIndex()) {
					result = camera;
					break;
				}
			}
		}

		return result;
	}

	public List<TrafficCamera> getCamerasForRegion(XRegion region) {
		assert region != null;
		return filteredCamerasPerRegion.get(region);
	}

	private void initUnfiltered() {
		unfilteredCamerasPerRegion.put(XRegion.SYD_MET, createSydMetData());
		unfilteredCamerasPerRegion.put(XRegion.SYD_NORTH, createSydNorthData());
		unfilteredCamerasPerRegion.put(XRegion.SYD_SOUTH, createSydSouthData());
		unfilteredCamerasPerRegion.put(XRegion.SYD_WEST, createSydWestData());
		unfilteredCamerasPerRegion.put(XRegion.REG_NORTH, createRegNorthData());
		unfilteredCamerasPerRegion.put(XRegion.REG_SOUTH, createRegSouthData());
		// Note: There are no cameras in REG_WEST at this time
		
		addSelfAsListenerToAllCameras();
	}
	
	private void addSelfAsListenerToAllCameras() {
		for (List<TrafficCamera> camerasInRegion : unfilteredCamerasPerRegion.values()) {
			for (TrafficCamera camera : camerasInRegion) {
				camera.addPropertyChangeListener(this);
			}
		}
	}

	private void loadFavourites() {
		assert ctx != null;
		MLog.i(TAG, "Loading favourites");
		
		// To begin with, mark all as NOT favourite
		for (List<TrafficCamera> camerasInRegion : unfilteredCamerasPerRegion.values()) {
			for (TrafficCamera camera : camerasInRegion) {
				camera.setFavouriteSilently(false);
			}
		}

		SharedPreferences prefs = ctx.getSharedPreferences(
				FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE);

		Set<String> favouriteCameraIds = prefs.getStringSet(
				FAVOURITE_STATE_PREF_KEY, null);
		
		MLog.i(TAG, "Favourite cameras as loaded from prefs is: " + favouriteCameraIds);
		
		if (favouriteCameraIds != null) {
			for (String cameraId : favouriteCameraIds) {
				MLog.i(TAG, cameraId + ",");
			}
			
			for (List<TrafficCamera> camerasInRegion : unfilteredCamerasPerRegion.values()) {
				for (TrafficCamera camera : camerasInRegion) {
					if (favouriteCameraIds.contains(String.valueOf(camera.getIndex()))) {
						MLog.i(TAG, "Calling setFavourite(true) for camera " + camera.getIndex());
						camera.setFavourite(true);
					}
				}
			}
		}
	}

	/**
	 * We save those cameras that ARE favourites. Applies to all known cameras -
	 * even those not admitted by the current filter.
	 */
	public void saveFavourites() {
		assert ctx != null;

		MLog.d(TAG, "Saving favourite states for all cameras");

		Set<String> favouriteCameraIds = new HashSet<String>();

		for (List<TrafficCamera> cameras : unfilteredCamerasPerRegion.values()) {
			for (TrafficCamera camera : cameras) {
				if (camera.isFavourite()) {
					favouriteCameraIds.add(String.valueOf(camera.getIndex()));
				}
			}
		}

		SharedPreferences prefs = ctx.getSharedPreferences(
				FAVOURITE_CAMERAS_FILE_NAME, Context.MODE_PRIVATE);

		Editor editor = prefs.edit();
		editor.putStringSet(FAVOURITE_STATE_PREF_KEY, favouriteCameraIds);
		editor.commit();
	}

	public void setCameraFavourite(int cameraIndex, boolean isFavourite) {
		MLog.i(TAG, "Set in database: camera " + cameraIndex
				+ " has favourite status = " + isFavourite);
	}

	public void setFilter(ITrafficCameraFilter newfilter) {
		assert newfilter != null;
		this.filter = newfilter;

		filteredCamerasPerRegion.clear();

		for (XRegion region : unfilteredCamerasPerRegion.keySet()) {
			List<TrafficCamera> unfilteredCameras = unfilteredCamerasPerRegion
					.get(region);

			if (!unfilteredCameras.isEmpty()) {
				List<TrafficCamera> filteredCameras = new LinkedList<TrafficCamera>();

				for (TrafficCamera camera : unfilteredCameras) {
					if (filter.admit(camera)) {
						filteredCameras.add(camera);
					}
				}

				if (!filteredCameras.isEmpty()) {
					filteredCamerasPerRegion.put(region, filteredCameras);
				}
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		MLog.d(TAG, "***TCDb " + hashCode() + " receives PCE for event " + event.getPropertyName());
		
		// TODO Auto-generated method stub
		if (TrafficCamera.PROPERTY_FAVOURITE.equals(event.getPropertyName())) {
			MLog.d(TAG, "TCDb receives notice that favourite state of a camera has changed.");
			saveFavourites();
			fireFavouritePropertyChangeEvent();
		}
	}

}
