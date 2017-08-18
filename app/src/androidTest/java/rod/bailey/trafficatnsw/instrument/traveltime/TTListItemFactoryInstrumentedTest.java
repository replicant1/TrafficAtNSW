package rod.bailey.trafficatnsw.instrument.traveltime;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.LinkedList;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem;
import rod.bailey.trafficatnsw.traveltime.item.TTListItemFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.assertIsHeading;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.assertIsSimple;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.assertIsTotal;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.getHeading;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.getSimpleTravelTime;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.newSegment;
import static rod.bailey.trafficatnsw.instrument.util.TestUtils.newTotalSegment;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TTListItemFactoryInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private static final String LOG_TAG = TTListItemFactoryInstrumentedTest.class.getSimpleName();

	MotorwayConfig m1Config;
	MotorwayTravelTimesStore m1Store;

	@Before
	public void before() {
		m1Config = new MotorwayConfigRegistry().getM1Config();
		m1Store = new MotorwayTravelTimesStore(mActivityRule.getActivity(), m1Config);
	}

	@Test
	public void testBothDirsSegsAndTotals() {
		XTravelTimeSegment n1 = newSegment("N1", 0);
		XTravelTimeSegment n2 = newSegment("N2", 0);
		XTravelTimeSegment ntotal = newTotalSegment("NTOTAL", 0);
		XTravelTimeSegment s1 = new XTravelTimeSegment("S1", null, null);
		XTravelTimeSegment s2 = new XTravelTimeSegment("S2", null, null);
		XTravelTimeSegment stotal = new XTravelTimeSegment("STOTAL", null, null);

		LinkedList<XTravelTimeSegment> segs = new LinkedList<>();
		segs.addAll(Arrays.asList(s1, stotal, n2, ntotal, s2, n1));
		m1Store.primeWithTravelTimes(segs);

		TTListItemFactory factory = new TTListItemFactory(m1Store);
		LinkedList<ITTListItem> items = factory.createItemList();

		assertNotNull(items);
		assertEquals(segs.size() + 2, items.size()); // Add 2 for headings

		assertIsHeading(0, items); // Northbound
		assertIsSimple(1, items); // N1
		assertIsSimple(2, items); // N2
		assertIsTotal(3, items); // NTOTAL
		assertIsHeading(4, items); // Southbound
		assertIsSimple(5, items); // S1
		assertIsSimple(6, items); // S2
		assertIsTotal(7, items); // STOTAL

		assertEquals("Northbound", getHeading(0, items).getText());
		assertEquals("Southbound", getHeading(4, items).getText());

		assertIsTotal(3, items);
		assertIsTotal(7, items);
	}

	@Test
	public void testBothDirsTotalsOnly() {
		XTravelTimeSegment ntotal = newTotalSegment("NTOTAL", 0);
		XTravelTimeSegment stotal = newTotalSegment("STOTAL", 0);
		LinkedList<XTravelTimeSegment> segs = new LinkedList<>();
		segs.addAll(Arrays.asList(ntotal, stotal));
		m1Store.primeWithTravelTimes(segs);

		TTListItemFactory factory = new TTListItemFactory(m1Store);
		LinkedList<ITTListItem> items = factory.createItemList();

		assertNotNull(items);
		assertEquals(segs.size() + 2, items.size());

		assertIsHeading(0, items); // Northbound
		assertIsSimple(1, items); // NTOTAL
		assertIsHeading(2, items); // Southbound
		assertIsSimple(3, items); // STOTAL

		assertEquals("Northbound", getHeading(0, items).getText());
		assertEquals("Southbound", getHeading(2, items).getText());

		assertIsTotal(1, items);
		assertIsTotal(3, items);
	}

	@Test
	public void testExcludeFromTotal() {
		XTravelTimeSegment n1 = newSegment("N1", 5);
		XTravelTimeSegment n2 = newSegment("N2", 7);
		XTravelTimeSegment ntotal = newSegment("NTOTAL", 0);
		XTravelTimeSegment s1 = newSegment("S1", 10);
		XTravelTimeSegment stotal = newSegment("STOTAL", 0);

		LinkedList<XTravelTimeSegment> segs = new LinkedList<>();
		segs.addAll(Arrays.asList(s1, n1, n2, ntotal, stotal));

		m1Store.primeWithTravelTimes(segs);
		m1Store.setAllIncludedInTotal();

		TTListItemFactory factory = new TTListItemFactory(m1Store);
		LinkedList<ITTListItem> items = factory.createItemList();

		assertNotNull(items);
		assertEquals(segs.size() + 2, items.size());

		assertIsHeading(0, items); // "Northbound" heading
		assertIsSimple(1, items); // N1 segment (5 mins, active)
		assertIsSimple(2, items); // N2 segment (7 mins, active)
		assertIsTotal(3, items); // NTOTAL
		assertIsHeading(4, items); // "Southbound" segment
		assertIsSimple(5, items); // S1 segment (10 mins, active)
		assertIsTotal(6, items); // STOTAL

		factory.updateTotalsItems(items);

		// Check basic totalling with all segments included
		assertEquals(12, getSimpleTravelTime(3, items));
		assertEquals(10, getSimpleTravelTime(6, items));

		// Make N1 inactive
		n1.setIncludedInTotalSilently(false);
		factory.updateTotalsItems(items);

		// Now that N1 is inactive, check that NTOTAL is 7 minutes
		assertEquals(7, getSimpleTravelTime(3, items));

		// Make N2 inactive
		n2.setIncludedInTotalSilently(false);
		factory.updateTotalsItems(items);

		// Now that N2 AND N2 are inactive, check that NTOTAL is 0 minutes
		assertEquals(0, getSimpleTravelTime(3, items));

		// Make S1 inactive. Check that STOTAL is now 0 minutes.
		s1.setIncludedInTotalSilently(false);
		factory.updateTotalsItems(items);

		assertEquals(0, getSimpleTravelTime(6, items));
	}


}
