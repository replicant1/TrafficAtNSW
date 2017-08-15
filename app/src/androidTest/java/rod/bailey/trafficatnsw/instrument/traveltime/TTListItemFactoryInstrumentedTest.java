package rod.bailey.trafficatnsw.instrument.traveltime;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem;
import rod.bailey.trafficatnsw.traveltime.item.TTListItemFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TTListItemFactoryInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule  = new ActivityTestRule(MainActivity_.class);

	@Test
	public void test1() {
		MotorwayConfig m1Config = new MotorwayConfigRegistry().getM1Config();
		MotorwayTravelTimesStore timesStore = new MotorwayTravelTimesStore(
				mActivityRule.getActivity(), m1Config);

		XTravelTimeSegment n1 = new XTravelTimeSegment("N1", null, null);
		XTravelTimeSegment n2 = new XTravelTimeSegment("N2", null, null);
		XTravelTimeSegment ntotal = new XTravelTimeSegment("NTOTAL", null, null);

		XTravelTimeSegment s1 = new XTravelTimeSegment("S1", null, null);
		XTravelTimeSegment s2 = new XTravelTimeSegment("S2", null, null);
		XTravelTimeSegment stotal = new XTravelTimeSegment("STOTAL", null, null);

		LinkedList<XTravelTimeSegment> segs = new LinkedList<>();
		segs.addAll(Arrays.asList(s1, stotal, n2, ntotal, s2, n1));
		timesStore.primeWithTravelTimes(segs);

		TTListItemFactory factory = new TTListItemFactory(timesStore);
		LinkedList<ITTListItem> items = factory.createItemList();

		assertNotNull(items);
		assertEquals(segs.size() + 2, items.size()); // Add 2 for headings
	}

}
