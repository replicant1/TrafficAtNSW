package rod.bailey.trafficatnsw;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
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
 * Created by rodbailey on 4/8/17.
 */
@RunWith(AndroidJUnit4.class)
public class ParseIncident2InstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private static List<XHazard> hazards;
	private static XHazard hazard;
	private static final String JSON_FILE = "09jul2013.json";
	private static final int HAZARD_ID = 440361;

	@BeforeClass
	public static void parseJsonFile() throws IOException {
		Context appContext = InstrumentationRegistry.getTargetContext();
		String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, JSON_FILE);
		hazards = XHazard.Companion.parseIncidentJson(jsonString);
		assertNotNull(hazards);
		assertEquals(29, hazards.size());
		hazard = TestUtils.findHazardById(hazards, HAZARD_ID);
	}

	@Test
	public void testLatLng() {
		// Check geometry
		Location loc = hazard.getLatlng();

		assertEquals(loc.getLongitude(), 147.59154, 0.0001);
		assertEquals(loc.getLatitude(), -35.46987, 0.0001);
	}

	@Test
	public void testDates() {
		assertEquals(new Long(1373305277059L), (Long) hazard.getCreated().getTime());
		assertEquals(new Long(1373324947947L), (Long) hazard.getLastUpdated().getTime());
	}

	@Test
	public void testArrays() {
		assertTrue(hazard.getArrangementElements().isEmpty());
		assertEquals(2, hazard.getAttendingGroups().size());
		assertEquals("Emergency service(s)", hazard.getAttendingGroups().get(0));
		assertEquals("RMS", hazard.getAttendingGroups().get(1));
	}

	@Test
	public void testAdvice() {
		assertEquals(" ", hazard.getAdviceB());
		assertEquals("Exercise caution", hazard.getAdviceA());
		assertEquals(" ", hazard.getOtherAdvice());
	}

	@Test
	public void testRoad() {
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
	}

	@Test
	public void testLane() {
		XRoad road = hazard.getRoads().get(0);
		XLane lane = road.getImpactedLanes().get(0);
		assertEquals("Northbound", lane.getAffectedDirection());
		assertEquals("1", lane.getClosedLanes());
		assertEquals("Lane 1", lane.getDescription());
		assertEquals("Lanes closed", lane.getExtent());
		assertEquals("2", lane.getNumberOfLanes());
		assertEquals(" ", lane.getRoadType());
	}

	@Test
	public void testBasicProperties() {
		assertNotNull(hazard.getHeadline());
		assertEquals(" ", hazard.getSubCategoryB());
		assertEquals("ACCIDENT Truck", hazard.getDisplayName());
		assertEquals("Truck", hazard.getSubCategoryA());
		assertEquals("Accident", hazard.getMainCategory());
	}

	@Test
	public void testWebLink() {
		assertTrue("null".equals(hazard.getWebLinkName()));
		assertTrue("null".equals(hazard.getWebLinkUrl()));
	}

	@Test
	public void testBooleans() {
		assertFalse(hazard.isEnded());
		assertFalse(hazard.isImpactingNetwork());
		assertFalse(hazard.isInitialReport());
		assertTrue(hazard.getPeriods().isEmpty());
		assertFalse(hazard.isMajor());
		assertNull(hazard.getStart());
	}
}
