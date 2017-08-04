package rod.bailey.trafficatnsw

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import rod.bailey.trafficatnsw.app.MainActivity_

/**
 * Instrumentation test, which will execute on an Android device.
 * Fri 4 Aug 6pm - this works!
 */
class KtInstrumentedTest {

	@Rule @JvmField
	var mActivityRule: ActivityTestRule<MainActivity_> = ActivityTestRule(MainActivity_::class.java)

	@Test
	fun testMe() {
//		assertTrue(2 == 2)
	}

}
