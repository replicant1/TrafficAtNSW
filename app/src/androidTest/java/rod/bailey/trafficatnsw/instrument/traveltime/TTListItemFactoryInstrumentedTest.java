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
import rod.bailey.trafficatnsw.traveltime.data.SegmentDirection;
import rod.bailey.trafficatnsw.traveltime.data.SegmentId;
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

	MotorwayConfig m1Config;
	MotorwayTravelTimesStore m1Store;

	@Before
	public void before() {
		m1Config = new MotorwayConfigRegistry().getM1Config();
		m1Store = new MotorwayTravelTimesStore(mActivityRule.getActivity(), m1Config);
	}

	@Test
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

	@Test
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

}
