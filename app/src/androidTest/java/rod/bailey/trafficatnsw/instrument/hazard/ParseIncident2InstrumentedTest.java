package rod.bailey.trafficatnsw.instrument.hazard;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

import rod.bailey.trafficatnsw.instrument.util.TestUtils;
import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection;
import rod.bailey.trafficatnsw.hazard.data.XLane;
import rod.bailey.trafficatnsw.hazard.data.XProperties;
import rod.bailey.trafficatnsw.hazard.data.XRoad;
import rod.bailey.trafficatnsw.util.ContextExtensionsKt;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rodbailey on 4/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParseIncident2InstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private static List<XHazard> hazards;
	private static XHazard hazard;
	private static final String JSON_FILE = "hazard/test_hazards_09jul2013.json";
	private static final int HAZARD_ID = 440361;
	private static String jsonString;

	@Test
	public void setup00() throws IOException {
		Context appContext = InstrumentationRegistry.getContext();
		jsonString = ContextExtensionsKt.assetFileAsString(appContext, JSON_FILE);
	}

	@Test
	public void setup01() {
		assertNotNull(jsonString);
		hazards = XHazardCollection.Companion.parseIncidentJson(jsonString).getHazards();
		assertNotNull(hazards);
	}

	@Test
	public void setup02() {
		assertEquals(29, hazards.size());
		hazard = TestUtils.findHazardById(hazards, HAZARD_ID);
	}

	@Test
	public void testLatLng() {
		// Check geometry
		double loc[] = hazard.getGeometry().getLatlng();

		assertEquals(loc[0], 147.59154, 0.0001);
		assertEquals(loc[1], -35.46987, 0.0001);
	}

	@Test
	public void testDates() {
		assertEquals(new Long(1373305277059L), (Long) hazard.getProperties().getCreated());
		assertEquals(new Long(1373324947947L), (Long) hazard.getProperties().getLastUpdated());
	}

	@Test
	public void testArrays() {
		XProperties props = hazard.getProperties();
		assertNotNull(props);
		assertNull(props.getArrangementElements());
		assertEquals(2, props.getAttendingGroups().size());
		assertEquals("Emergency service(s)", props.getAttendingGroups().get(0));
		assertEquals("RMS", props.getAttendingGroups().get(1));
	}

	@Test
	public void testAdvice() {
		assertEquals(" ", hazard.getProperties().getAdviceB());
		assertEquals("Exercise caution", hazard.getProperties().getAdviceA());
		assertEquals(" ", hazard.getProperties().getOtherAdvice());
	}

	@Test
	public void testRoad() {
		XRoad road = hazard.getProperties().getRoads().get(0);
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
		XRoad road = hazard.getProperties().getRoads().get(0);
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
		assertNotNull(hazard.getProperties().getHeadline());
		assertEquals(" ", hazard.getProperties().getSubCategoryB());
		assertEquals("ACCIDENT Truck", hazard.getProperties().getDisplayName());
		assertEquals("Truck", hazard.getProperties().getSubCategoryA());
		assertEquals("Accident", hazard.getProperties().getMainCategory());
	}

	@Test
	public void testWebLink() {
		assertNull(hazard.getProperties().getWebLinkName());
		assertNull(hazard.getProperties().getWebLinkUrl());
	}

	@Test
	public void testBooleans() {
		assertFalse(hazard.getProperties().isEnded());
		assertFalse(hazard.getProperties().isImpactingNetwork());
		assertFalse(hazard.getProperties().isInitialReport());
		assertTrue(hazard.getProperties().getPeriods().isEmpty());
		assertFalse(hazard.getProperties().isMajor());
		assertNull(hazard.getProperties().getStart());
	}
}
