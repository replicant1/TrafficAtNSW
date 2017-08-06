package rod.bailey.trafficatnsw;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection;
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
public class ParseIncident1InstrumentedTest {

	private static final String JSON_FILE = "09jul2013.json";
	private static final int HAZARD_ID = 440375;
	private static XHazardCollection hazards;
	private static XHazard hazard;
	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	@BeforeClass
	public static void parseJsonFile() throws IOException {
		Context appContext = InstrumentationRegistry.getContext();
		String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, JSON_FILE);
		hazards = XHazardCollection.Companion.parseIncidentJson(jsonString);
		assertNotNull(hazards);
		assertEquals(29, hazards.getHazards().size());
		hazard = TestUtils.findHazardById(hazards.getHazards(), HAZARD_ID);
	}

	@Test
	public void testAdvice() {
		assertEquals(" ", hazard.getProperties().getAdviceA());
		assertEquals(" ", hazard.getProperties().getAdviceB());
		assertEquals(" ", hazard.getProperties().getOtherAdvice());
	}

	@Test
	public void testArrays() {
		// Check arrays
		assertTrue(hazard.getProperties().getPeriods().isEmpty());
		assertTrue(hazard.getProperties().getArrangementElements().isEmpty());
		assertEquals(1, hazard.getProperties().getAttendingGroups().size());
		assertEquals("Tow truck", hazard.getProperties().getAttendingGroups().get(0));
		assertEquals(1, hazard.getProperties().getRoads().size());
	}

	@Test
	public void testBasicProperties() {
		assertEquals("BREAKDOWN Car - ROZELLE Western Distributor approaching City West Link",
				hazard.getProperties().getHeadline());
		assertEquals(" ", hazard.getProperties().getSubCategoryB());
		assertEquals("Car", hazard.getProperties().getSubCategoryA());
		assertEquals("Breakdown", hazard.getProperties().getMainCategory());
		assertEquals("BREAKDOWN Car", hazard.getProperties().getDisplayName());
	}

	@Test
	public void testBooleans() {
		assertFalse(hazard.getProperties().isMajor());
		assertFalse(hazard.getProperties().isInitialReport());
		assertNull(hazard.getProperties().getStart());
		assertTrue(hazard.getProperties().isEnded());
		assertFalse(hazard.getProperties().isImpactingNetwork());
		assertNull(hazard.getProperties().getEnd());
	}

	@Test
	public void testDates() {
		assertEquals(new Long(1373321524927L), (Long) hazard.getProperties().getCreated().getTime());
		assertEquals(new Long(1373322559566L), (Long) hazard.getProperties().getLastUpdated().getTime());
	}

	@Test
	public void testLane() {
		XRoad road = hazard.getProperties().getRoads().get(0);
		XLane lane = road.getImpactedLanes().get(0);
		assertEquals("Westbound", lane.getAffectedDirection());
		assertEquals("1", lane.getClosedLanes());
		assertEquals("Lane 2", lane.getDescription());
		assertEquals("Lanes closed", lane.getExtent());
		assertEquals("2", lane.getNumberOfLanes());
		assertEquals(" ", lane.getRoadType());
	}

	@Test
	public void testLatLng() {
		// Check its geometry
		double loc[] = hazard.getGeometry().getLatlng();
		assertNotNull(loc);

		// Check latitude and longitude
		assertEquals(loc[0], 151.18589, 0.0001);
		assertEquals(loc[1], -33.86936, 0.0001);
	}

	@Test
	public void testRoad() {
		XRoad road = hazard.getProperties().getRoads().get(0);
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
	}

	@Test
	public void testWebLink() {
		assertTrue("null".equals(hazard.getProperties().getWebLinkUrl()));
		assertTrue("null".equals(hazard.getProperties().getWebLinkName()));
	}
}
