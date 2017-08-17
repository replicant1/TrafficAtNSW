package rod.bailey.trafficatnsw.instrument.traveltime;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.LinkedList;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.instrument.util.TestUtils;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeProperties;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.traveltime.item.HeadingTTListItem;
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem;
import rod.bailey.trafficatnsw.traveltime.item.SimpleTTListItem;
import rod.bailey.trafficatnsw.traveltime.item.TTListItemFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TTListItemFactoryInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule  = new ActivityTestRule(MainActivity_.class);

	private static final String LOG_TAG = TTListItemFactoryInstrumentedTest.class.getSimpleName();

	MotorwayConfig m1Config;
	MotorwayTravelTimesStore m1Store;

	@Before
	public void before() {
		m1Config = new MotorwayConfigRegistry().getM1Config();
		m1Store = new MotorwayTravelTimesStore(mActivityRule.getActivity(), m1Config);
	}

	//@Test
	public void testBothDirsSegsAndTotals() {
		XTravelTimeSegment n1 = new XTravelTimeSegment("N1", null, null);
		XTravelTimeSegment n2 = new XTravelTimeSegment("N2", null, null);
		XTravelTimeSegment ntotal = new XTravelTimeSegment("NTOTAL", null, null);

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

		assertTrue(items.get(0) instanceof HeadingTTListItem); // Northbound
		assertTrue(items.get(1) instanceof SimpleTTListItem); // N1
		assertTrue(items.get(2) instanceof SimpleTTListItem); // N2
		assertTrue(items.get(3) instanceof SimpleTTListItem); // NTOTAL
		assertTrue(items.get(4) instanceof HeadingTTListItem); // Southbound
		assertTrue(items.get(5) instanceof SimpleTTListItem); // S1
		assertTrue(items.get(6) instanceof SimpleTTListItem); // S2
		assertTrue(items.get(7) instanceof SimpleTTListItem); // STOTAL

		assertEquals("Northbound", ((HeadingTTListItem)items.get(0)).getText());
		assertEquals("Southbound", ((HeadingTTListItem)items.get(4)).getText());

		assertTrue(((SimpleTTListItem)items.get(3)).getTravelTime().isTotal());
		assertTrue(((SimpleTTListItem)items.get(7)).getTravelTime().isTotal());
	}

	//@Test
	public void testBothDirsTotalsOnly() {
		XTravelTimeSegment ntotal = new XTravelTimeSegment("NTOTAL", null, null);
		XTravelTimeSegment stotal = new XTravelTimeSegment("STOTAL", null, null);
		LinkedList<XTravelTimeSegment> segs = new LinkedList<>();
		segs.addAll(Arrays.asList(ntotal, stotal));
		m1Store.primeWithTravelTimes(segs);

		TTListItemFactory factory = new TTListItemFactory(m1Store);
		LinkedList<ITTListItem> items = factory.createItemList();

		assertNotNull(items);
		assertEquals(segs.size() + 2, items.size());

		assertTrue(items.get(0) instanceof HeadingTTListItem); // Northbound
		assertTrue(items.get(1) instanceof SimpleTTListItem); // NTOTAL
		assertTrue(items.get(2) instanceof HeadingTTListItem); // Southbound
		assertTrue(items.get(3) instanceof SimpleTTListItem); // STOTAL

		assertEquals("Northbound", ((HeadingTTListItem)items.get(0)).getText());
		assertEquals("Southbound", ((HeadingTTListItem)items.get(2)).getText());

		assertTrue(((SimpleTTListItem)items.get(1)).getTravelTime().isTotal());
		assertTrue(((SimpleTTListItem)items.get(3)).getTravelTime().isTotal());
	}

	@Test
	public void testAllActive() {
		// Segment N1 : 5 minutes, active, includedInTotal
		XTravelTimeProperties n1Props = new XTravelTimeProperties("N", "To", true, "From");
		n1Props.setTravelTimeMinutes(5);
		XTravelTimeSegment n1 = new XTravelTimeSegment("N1", null, n1Props);
		n1.setIncludedInTotalSilently(true);

		// Segment N2: 7 minutes, active, includedInTotal
		XTravelTimeProperties n2Props = new XTravelTimeProperties("N", "XX", true, "YY");
		n2Props.setTravelTimeMinutes(7);
		XTravelTimeSegment n2 = new XTravelTimeSegment("N2", null, n2Props);
		n2.setIncludedInTotalSilently(true);

		// North total
		XTravelTimeProperties ntotalProps = new XTravelTimeProperties("N", "AA", true, "BB");
		XTravelTimeSegment ntotal = new XTravelTimeSegment("NTOTAL", null, ntotalProps);

		// Segment S1 : 10 minutes, active
		XTravelTimeProperties s1Props = new XTravelTimeProperties("S", "To", true, "From");
		s1Props.setTravelTimeMinutes(10);
		XTravelTimeSegment s1 = new XTravelTimeSegment("S1", null, s1Props);
		s1.setIncludedInTotalSilently(true);

		// South total
		XTravelTimeProperties stotalProps = new XTravelTimeProperties("S", "E", true, "F");
		XTravelTimeSegment stotal = new XTravelTimeSegment("STOTAL", null, stotalProps);

		LinkedList<XTravelTimeSegment> segs = new LinkedList<>();
		segs.addAll(Arrays.asList(s1, n1, n2, ntotal, stotal));
		m1Store.primeWithTravelTimes(segs);
		m1Store.setAllIncludedInTotal();

		TTListItemFactory factory = new TTListItemFactory(m1Store);
		LinkedList<ITTListItem> items = factory.createItemList();

		assertNotNull(items);
		assertEquals(segs.size() + 2, items.size());

		assertTrue(items.get(0) instanceof HeadingTTListItem); // "Northbound" heading
		assertTrue(items.get(1) instanceof SimpleTTListItem); // N1 segment (5 mins, active)
		assertTrue(items.get(2) instanceof SimpleTTListItem); // N2 segment (7 mins, active)
		assertTrue(items.get(3) instanceof SimpleTTListItem); // NTOTAL
		assertTrue(items.get(4) instanceof HeadingTTListItem); // "Southbound" segment
		assertTrue(items.get(5) instanceof SimpleTTListItem); // S1 segment (10 mins, active)
		assertTrue(items.get(6) instanceof SimpleTTListItem); // STOTAL

		factory.updateTotalsItems(items);

		// Check basic totalling with all segments included
		assertEquals(12, (int) ((SimpleTTListItem) items.get(3)).getTravelTime().getProperties()
				.getTravelTimeMinutes());
		assertEquals(10, (int) ((SimpleTTListItem) items.get(6)).getTravelTime().getProperties()
				.getTravelTimeMinutes());

		// Make N1 inactive
		n1.setIncludedInTotalSilently(false);
		factory.updateTotalsItems(items);

		// Now that N1 is inactive, check that NTOTAL is 7 minutes, not 12 minutes
		assertEquals(7, (int) ((SimpleTTListItem) items.get(3)).getTravelTime().getProperties()
				.getTravelTimeMinutes());
	}

}
