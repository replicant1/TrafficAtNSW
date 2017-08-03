package rod.bailey.trafficatnsw;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.hazard.data.XLane;
import rod.bailey.trafficatnsw.hazard.data.XRoad;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class HazardCollectionInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private XHazard findHazardById(List<XHazard> hazards, int hazardId) {
		XHazard result = null;
		for (XHazard hazard : hazards) {
			if (hazardId == hazard.getHazardId().intValue()) {
				result = hazard;
				break;
			}
		}
		return result;
	}

	/**
	 * Sample json file 09jul2013 contains 29 hazards.
	 */
	@Test
	public void parse09jul2013JsonHazard440375() throws IOException {
		Context appContext = InstrumentationRegistry.getTargetContext();
		String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, "09jul2013.json");
		List<XHazard> hazards = XHazard.Companion.parseIncidentJson(jsonString);

		assertNotNull(hazards);
		assertEquals(29, hazards.size());

		// Examine the first hazard, which has ID 440375
		XHazard hazard = findHazardById(hazards, 440375);
		assertNotNull(hazard);

		// Check its geometry
		Location loc = hazard.getLatlng();
		assertNotNull(loc);

		// Check latitude and longitude
		assertEquals(loc.getLongitude(), 151.18589, 0.0001);
		assertEquals(loc.getLatitude(), -33.86936, 0.0001);

		// Check simple (non-object, non-array) properties of hazard
		assertEquals("BREAKDOWN Car - ROZELLE Western Distributor approaching City West Link", hazard.getHeadline());

		assertTrue("null".equals(hazard.getWebLinkUrl()));
		assertTrue(hazard.isEnded());
		assertFalse(hazard.isImpactingNetwork());
		assertEquals(" ", hazard.getSubCategoryB());
		assertFalse(hazard.isInitialReport());
		assertEquals(new Long(1373321524927L), (Long) hazard.getCreated().getTime());
		assertFalse(hazard.isMajor());
		assertEquals("Car", hazard.getSubCategoryA());
		assertEquals(" ", hazard.getAdviceA());
		assertEquals(" ", hazard.getAdviceB());

		assertNull(hazard.getEnd());
		assertEquals("Breakdown", hazard.getMainCategory());
		assertEquals(new Long(1373322559566L), (Long) hazard.getLastUpdated().getTime());
		assertEquals(" ", hazard.getOtherAdvice());

		assertTrue("null".equals(hazard.getWebLinkName()));
		assertNull(hazard.getStart());
		assertEquals("BREAKDOWN Car", hazard.getDisplayName());

		// Check arrays
		assertTrue(hazard.getPeriods().isEmpty());
		assertTrue(hazard.getArrangementElements().isEmpty());
		assertEquals(1, hazard.getAttendingGroups().size());
		assertEquals("Tow truck", hazard.getAttendingGroups().get(0));
		assertEquals(1, hazard.getRoads().size());

		// Check road
		XRoad road = hazard.getRoads().get(0);
		assertEquals("", road.getConditionTendency());
		assertEquals("City West Link", road.getCrossStreet());
		assertEquals("", road.getDelay());
		assertEquals("approaching", road.getLocationQualifier());
		assertEquals("Western Distributor", road.getMainStreet());
		assertEquals(new Integer(0), road.getQueueLength());
		assertEquals("SYD_MET", road.getRegion());
		assertEquals(" ", road.getSecondLocation());
		assertEquals("ROZELLE", road.getSuburb());
		assertEquals("", road.getTrafficVolume());

		// Check impacted lane
		XLane lane = road.getImpactedLanes().get(0);
		assertEquals("Westbound", lane.getAffectedDirection());
		assertEquals("1", lane.getClosedLanes());
		assertEquals("Lane 2", lane.getDescription());
		assertEquals("Lanes closed", lane.getExtent());
		assertEquals("2", lane.getNumberOfLanes());
		assertEquals(" ", lane.getRoadType());
	}

	@Test
	public void parse09jul2013JsonHazard440361() throws IOException {
		Context appContext = InstrumentationRegistry.getTargetContext();
		String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, "09jul2013.json");
		List<XHazard> hazards = XHazard.Companion.parseIncidentJson(jsonString);

		// This test case examines hazard id 440361
		XHazard hazard = findHazardById(hazards, 440361);

		// Check geometry
		Location loc = hazard.getLatlng();

		assertEquals(loc.getLongitude(), 147.59154, 0.0001);
		assertEquals(loc.getLatitude(), -35.46987, 0.0001);

		// Check properties
		assertNotNull(hazard.getHeadline());
		assertTrue(hazard.getPeriods().isEmpty());
		assertTrue("null".equals(hazard.getWebLinkUrl()));
		assertFalse(hazard.isEnded());
		assertFalse(hazard.isImpactingNetwork());
		assertEquals(" ", hazard.getSubCategoryB());
		assertFalse(hazard.isInitialReport());
		assertEquals(new Long(1373305277059L), (Long) hazard.getCreated().getTime());
		assertFalse(hazard.isMajor());
		assertEquals("Truck", hazard.getSubCategoryA());
		assertEquals(" ", hazard.getAdviceB());
		assertEquals("Exercise caution", hazard.getAdviceA());
		assertEquals("Accident", hazard.getMainCategory());
		assertEquals(new Long(1373324947947L), (Long) hazard.getLastUpdated().getTime());
		assertEquals(" ", hazard.getOtherAdvice());
		assertTrue(hazard.getArrangementElements().isEmpty());
		assertTrue("null".equals(hazard.getWebLinkName()));
		assertEquals(2, hazard.getAttendingGroups().size());
		assertEquals("Emergency service(s)", hazard.getAttendingGroups().get(0));
		assertEquals("RMS", hazard.getAttendingGroups().get(1));
		assertNull(hazard.getStart());
		assertEquals("ACCIDENT Truck", hazard.getDisplayName());

		// Check road
		XRoad road = hazard.getRoads().get(0);
		assertEquals("", road.getConditionTendency());
		assertEquals("Tumbarumba Road", road.getCrossStreet());
		assertEquals("", road.getDelay());
		assertEquals("between", road.getLocationQualifier());
		assertEquals("Hume Highway", road.getMainStreet());
		assertEquals("REG_SOUTH", road.getRegion());
		assertEquals("Little Billabong Road", road.getSecondLocation());
		assertEquals("Kyeamba Gap", road.getSuburb());
		assertEquals("", road.getTrafficVolume());

		// Check impacted lane
		XLane lane = road.getImpactedLanes().get(0);
		assertEquals("Northbound", lane.getAffectedDirection());
		assertEquals("1", lane.getClosedLanes());
		assertEquals("Lane 1", lane.getDescription());
		assertEquals("Lanes closed", lane.getExtent());
		assertEquals("2", lane.getNumberOfLanes());
		assertEquals(" ", lane.getRoadType());
	}

}
