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

import rod.bailey.trafficatnsw.instrument.util.TestUtils;
import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection;
import rod.bailey.trafficatnsw.hazard.data.XLane;
import rod.bailey.trafficatnsw.hazard.data.XProperties;
import rod.bailey.trafficatnsw.hazard.data.XRoad;
import rod.bailey.trafficatnsw.util.ContextExtensionsKt;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by rodbailey on 6/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParseIncident3InstrumentedTest {

	private static final String JSON_FILE = "hazard/test_hazards_09jul2013.json";
	private static final int HAZARD_ID = 440363;
	private static XHazardCollection hazards;
	private static XHazard hazard;
	private static String jsonString;

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	@Test
	public void setup00() throws IOException {
		Context appContext = InstrumentationRegistry.getContext();
		jsonString = ContextExtensionsKt.assetFileAsString(appContext, JSON_FILE);
		assertNotNull(jsonString);
	}

	@Test
	public void setup01() {
		hazards = XHazardCollection.Companion.parseIncidentJson(jsonString);
		assertNotNull(hazards);
		assertEquals(29, hazards.getHazards().size());
	}

	@Test
	public void setup02() {
		hazard = TestUtils.findHazardById(hazards.getHazards(), HAZARD_ID);
		assertNotNull(hazard);
	}

	@Test
	public void testLatLng() {
		double loc[] = hazard.getGeometry().getLatlng();
		assertNotNull(loc);
		assertEquals(2, loc.length);
		assertEquals(loc[0], 151.00576, 0.0001);
		assertEquals(loc[1], -33.80355, 0.0001);
	}

	@Test
	public void testDates() {
		XProperties props = hazard.getProperties();
		assertNotNull(props);
		assertNotNull(props.getCreated());
		assertEquals(new Long(1373312188608L), props.getCreated());
		assertNotNull(props.getLastUpdated());
		assertEquals(new Long(1373318476172L), props.getLastUpdated());
	}

	@Test
	public void testArrays() {
		XProperties props = hazard.getProperties();
		assertNotNull(props);
		assertNull(props.getArrangementElements());
		assertNotNull(props.getAttendingGroups());
		assertEquals(1, props.getAttendingGroups().size());
		assertEquals("RMS", props.getAttendingGroups().get(0));
	}

	@Test
	public void testAdvice() {
		XProperties props = hazard.getProperties();
		assertNotNull(props);
		assertEquals("Exercise caution", props.getAdviceA());
		assertEquals(" ", props.getAdviceB());
		assertEquals(" ", props.getOtherAdvice());
	}

	@Test
	public void testRoad() {
		XRoad road = hazard.getProperties().getRoads().get(0);
		assertEquals("", road.getConditionTendency());
		assertEquals("Church Street", road.getCrossStreet());
		assertEquals("", road.getDelay());
		assertEquals("at", road.getLocationQualifier());
		assertEquals("Pennant Hills Road", road.getMainStreet());
		assertEquals((int)0, (int) road.getQueueLength());
		assertEquals("SYD_WEST", road.getRegion());
		assertEquals(" ", road.getSecondLocation());
		assertEquals("North Parramatta", road.getSuburb());
		assertEquals("", road.getTrafficVolume());
	}

	@Test
	public void testLane() {
		XRoad road = hazard.getProperties().getRoads().get(0);
		XLane lane = road.getImpactedLanes().get(0);
		assertEquals("All directions", lane.getAffectedDirection());
		assertEquals(" ", lane.getClosedLanes());
		assertEquals(" ", lane.getDescription());
		assertEquals("Affected", lane.getExtent());
		assertEquals(" ", lane.getNumberOfLanes());
		assertEquals(" ", lane.getRoadType());
	}

	@Test
	public void testBasicProperties() {
		XProperties props = hazard.getProperties();
		assertNotNull(props.getHeadline());
		assertEquals("Lights flashing yellow", props.getSubCategoryA());
		assertEquals(" ", props.getSubCategoryB());
		assertEquals("TRAFFIC LIGHTS FLASHING YELLOW", props.getDisplayName());
		assertEquals("Traffic signals", props.getMainCategory());
	}
}
