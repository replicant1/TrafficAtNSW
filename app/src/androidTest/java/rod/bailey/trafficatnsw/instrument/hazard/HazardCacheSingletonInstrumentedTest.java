package rod.bailey.trafficatnsw.instrument.hazard;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton;
import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.hazard.data.XRegion;
import rod.bailey.trafficatnsw.hazard.filter.AdmitAnyHazardFilter;
import rod.bailey.trafficatnsw.hazard.filter.AdmitRegionalHazardFilter;
import rod.bailey.trafficatnsw.hazard.filter.AdmitSydneyHazardFilter;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by rodbailey on 11/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HazardCacheSingletonInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private HazardCacheSingleton cache;

	private static final String JSON_FILE = "hazard/test_hazards_09jul2013.json";

	@Before
	public void setup() throws IOException {
		cache = new HazardCacheSingleton();
		Context appContext = InstrumentationRegistry.getContext();
		String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, JSON_FILE);
		cache.init(jsonString);
	}

	@Test
	public void testDefaultFiterAdmitsAny() {
		assertTrue(cache.getFilter() instanceof AdmitAnyHazardFilter);
		assertEquals(2, cache.getFilteredHazardsForRegion(XRegion.SYD_MET).size());
		assertEquals(1, cache.getFilteredHazardsForRegion(XRegion.SYD_NORTH).size());
		assertEquals(0, cache.getFilteredHazardsForRegion(XRegion.SYD_SOUTH).size());
		assertEquals(0, cache.getFilteredHazardsForRegion(XRegion.SYD_WEST).size());
		assertEquals(4, cache.getFilteredHazardsForRegion(XRegion.REG_SOUTH).size());
		assertEquals(0, cache.getFilteredHazardsForRegion(XRegion.REG_WEST).size());
		assertEquals(1, cache.getFilteredHazardsForRegion(XRegion.REG_NORTH).size());
	}

	@Test
	public void testAdmitSydneyFilter() {
		cache.setFilter(new AdmitSydneyHazardFilter());
		assertEquals(2, cache.getFilteredHazardsForRegion(XRegion.SYD_MET).size());
		assertEquals(0, cache.getFilteredHazardsForRegion(XRegion.REG_NORTH).size());
	}

	@Test
	public void testAdmitRegionalFilter() {
		cache.setFilter(new AdmitRegionalHazardFilter());
		assertEquals(0, cache.getFilteredHazardsForRegion(XRegion.SYD_MET).size());
		assertEquals(1, cache.getFilteredHazardsForRegion(XRegion.REG_NORTH).size());
	}

	@Test
	public void testGetHazardNotEnded() {
		assertNotNull(cache.getUnfilteredHazard(438290));
	}

	@Test
	public void testGetHazardEnded() {
		assertNull(cache.getUnfilteredHazard(440375));
	}

}
