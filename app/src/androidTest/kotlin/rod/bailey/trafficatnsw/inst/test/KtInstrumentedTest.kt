package rod.bailey.trafficatnsw.inst.test

import org.junit.Test
import kotlin.test.assertTrue

/**
 * Instrumentation test, which will execute on an Android device.
 * Fri 4 Aug 6pm - this works until we insert the activity rule
 * Then it fails because of: https://discuss.kotlinlang.org/t/class-reflection-get-incorrect-class-name/3920
 * A bug in Kotlin reflection. A solution may be to remove @EActivity from MainActivity
 */
//@LargeTest
//@RunWith(AndroidJUnit4::class)
class KtInstrumentedTest {

//	@Rule @JvmField
//	var activity:@JvmField
//	var activity: ActivityTestRule<MainActivity_> = ActivityTestRule<MainActivity_>(MainActivity_::class.java)

	@Test
	fun testMe() {
		assertTrue(2 == 2)
	}

}
