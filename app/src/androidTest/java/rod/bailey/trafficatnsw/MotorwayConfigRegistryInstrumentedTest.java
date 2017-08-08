package rod.bailey.trafficatnsw;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by rodbailey on 8/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MotorwayConfigRegistryInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule  = new ActivityTestRule(MainActivity_.class);

	@Test
	public void testGetConfigsNotNull() {
		MotorwayConfigRegistry reg = new MotorwayConfigRegistry();
		assertNotNull(reg.getM1Config());
		assertNotNull(reg.getM2Config());
		assertNotNull(reg.getM4Config());
		assertNotNull(reg.getM7Config());
	}

	@Test
	public void testMotorwayNamesInConfigs() {
		MotorwayConfigRegistry reg = new MotorwayConfigRegistry();
		assertEquals("M1", reg.getM1Config().getMotorwayName());
		assertEquals("M2", reg.getM2Config().getMotorwayName());
		assertEquals("M4", reg.getM4Config().getMotorwayName());
		assertEquals("M7", reg.getM7Config().getMotorwayName());
	}
}
