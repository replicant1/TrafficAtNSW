package rod.bailey.trafficatnsw;

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

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.hazard.data.XGeometry;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeCollection;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeProperties;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by rodbailey on 7/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParseTravelTimes1InstrumentedTest {

	private static final String JSON_FILE = "travel_times_m1.json";
	private static final String SEGMENT_ID = "N7";
	private static XTravelTimeCollection travelTimes;
	private static XTravelTimeSegment n7;
	private static XTravelTimeSegment s10;
	private static String jsonString;

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	@Test
	public void setup00() throws IOException {
		Context appContext = InstrumentationRegistry.getContext();
		jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, JSON_FILE);
	}

	@Test
	public void setup01() {
		assertNotNull(jsonString);
		travelTimes = XTravelTimeCollection.Companion.parseTravelTimesJson(jsonString);
		assertNotNull(travelTimes);
		assertEquals(23, travelTimes.getTravelTimes().size());
	}

	@Test
	public void setup02() {
		n7 = TestUtils.findSegmentById(travelTimes.getTravelTimes(), "N7");
		assertNotNull(n7);
	}

	@Test
	public void testN7Geometry() {
		XGeometry geometry = n7.getGeometry();
		assertEquals(geometry.getLatlng()[0], 151.36733, 0.0001);
		assertEquals(geometry.getLatlng()[1], -33.346, 0.0001);
	}

	@Test
	public void testN7Properties() {
		XTravelTimeProperties props = n7.getProperties();
		assertNotNull(props);
		assertTrue(props.isActive());
		assertEquals(new Integer(3), props.getTravelTimeMinutes());
		assertEquals("Ourimbah", props.getFromDisplayName());
		assertEquals("N-E", props.getDirection());
		assertEquals("Wyong", props.getToDisplayName());
	}


	@Test
	public void testFindS10() {
		s10 = TestUtils.findSegmentById(travelTimes.getTravelTimes(), "S10");
		assertNotNull(s10);
	}

	@Test
	public void testS10Geometry() {
		XGeometry geometry = s10.getGeometry();
		assertNotNull(geometry);
		assertEquals(geometry.getLatlng()[0], 151.1682, 0.0001);
		assertEquals(geometry.getLatlng()[1], -33.60178, 0.0001);
	}

	@Test
	public void testS10Properties() {
		XTravelTimeProperties props = s10.getProperties();
		assertNotNull(props);
		assertTrue(props.isActive());
		assertEquals((int)9, (int)props.getTravelTimeMinutes());
		assertEquals("Berowra", props.getFromDisplayName());
		assertEquals("S-W", props.getDirection());
		assertEquals("Wahroonga", props.getToDisplayName());
	}
}
