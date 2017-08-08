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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore;
import rod.bailey.trafficatnsw.traveltime.data.SegmentId;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeCollection;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by rodbailey on 8/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MotorwayTravelTimesStoreInstrumentedTest {

	public static final String SEGMENT_ID = "N7";

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule  = new ActivityTestRule(MainActivity_.class);

	private static String JSON_FILE = "travel_times_m1.json";
	private static MotorwayTravelTimesStore store;

	@Test
	public void setup01() {
		// Store is created based on *target* (not instrumented) context so that we have access
		// to the app's private storage area, where the shared prefs and exclusion state files
		// are written.
		Context appContext = InstrumentationRegistry.getTargetContext();
		MotorwayConfig config = new MotorwayConfigRegistry().getM1Config();
		store = new MotorwayTravelTimesStore(appContext, config);
		assertNotNull(store);
	}

	@Test
	public void setup02() throws IOException {
		// JSON is loaded from the *instrumentation* context, as data file is in the 'androidTest' dir
		String jsonStr = AssetUtils.INSTANCE.loadAssetFileAsString(InstrumentationRegistry.getContext(), JSON_FILE);
		List<XTravelTimeSegment> times = XTravelTimeCollection.Companion.parseTravelTimesJson(jsonStr).getTravelTimes();
		assertFalse(store.isPrimed());
		store.primeWithTravelTimes(times);
		assertTrue(store.isPrimed());
		assertNotNull(store.getTravelTimes());
		assertEquals(23, store.getTravelTimes().size());
	}

	@Test
	public void testStoreConfig() {
		MotorwayConfig config = new MotorwayConfigRegistry().getM1Config();
		assertEquals(config, store.getConfig());
	}

	@Test
	public void testChangeIncludedInTotalOfSegmentIsPropagatedToStore() {
		XTravelTimeSegment seg = TestUtils.findSegmentById(store.getTravelTimes(), SEGMENT_ID);
		final AtomicBoolean semaphore = new AtomicBoolean(false);
		store.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				semaphore.set(true);
			}
		});
		seg.setIncludedInTotal(false);
		assertTrue(semaphore.get());
	}

	@Test
	public void testSaveExclusionStates() throws IOException{
		// The act of changing the exclusion state of a TT segment automatically results
		// in the exclusion state of all segments in the store being persisted to prefs
		XTravelTimeSegment segN7 = TestUtils.findSegmentById(store.getTravelTimes(), SEGMENT_ID);
		segN7.setIncludedInTotal(false);

		// Exclusion states now persisted to shared prefs in target context

		// Prime new store with data from instrumented context
		String jsonStr = AssetUtils.INSTANCE.loadAssetFileAsString(InstrumentationRegistry.getContext(), JSON_FILE);

		// Create a new store in target context
		MotorwayConfig config = new MotorwayConfigRegistry().getM1Config();
		List<XTravelTimeSegment> times = XTravelTimeCollection.Companion.parseTravelTimesJson(jsonStr).getTravelTimes();
		MotorwayTravelTimesStore newStore = new MotorwayTravelTimesStore(InstrumentationRegistry.getTargetContext(), config);

		XTravelTimeSegment newN7 = TestUtils.findSegmentById(times, SEGMENT_ID);
		assertNotNull(newN7);
		assertNull(newN7.getIncludedInTotal());

		// After priming newStore with newly parsed segments, the *old* exclusion state of N7 should be
		// re-instated from the shared prefs
		newStore.primeWithTravelTimes(times);
		assertFalse(newN7.getIncludedInTotal());
	}

	@Test
	public void testGetExcludedSegmentIdsNotEmpty() {
		resetStoreToAllSegsIncluded();
		XTravelTimeSegment segN7 = TestUtils.findSegmentById(store.getTravelTimes(), SEGMENT_ID);
		segN7.setIncludedInTotal(false);

		Set<SegmentId> excludedIds  = store.getSavedExcludedSegmentIds();
		assertNotNull(excludedIds);
		assertEquals(1, excludedIds.size());
	}

	@Test
	public void testGetExcludedSegmentIdsEmpty() {
		resetStoreToAllSegsIncluded();
		XTravelTimeSegment segN7 = TestUtils.findSegmentById(store.getTravelTimes(), SEGMENT_ID);
		segN7.setIncludedInTotal(true);

		Set<SegmentId> excludedIds = store.getSavedExcludedSegmentIds();
		assertNotNull(excludedIds);
		assertTrue(excludedIds.isEmpty());
	}

	private void resetStoreToAllSegsIncluded() {
		for (XTravelTimeSegment segment : store.getTravelTimes()) {
			segment.setIncludedInTotalSilently(true);
		}

		if (!store.getTravelTimes().isEmpty()) {
			store.getTravelTimes().get(0).setIncludedInTotal(true);
		}
	}

	@Test
	public void testSetIncludeInTotalSilently() {
		XTravelTimeSegment seg = TestUtils.findSegmentById(store.getTravelTimes(), SEGMENT_ID);
		final AtomicBoolean semaphore = new AtomicBoolean(false);
		store.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				semaphore.set(true);
			}
		});
		seg.setIncludedInTotalSilently(false);
		assertFalse(semaphore.get());
	}
}
