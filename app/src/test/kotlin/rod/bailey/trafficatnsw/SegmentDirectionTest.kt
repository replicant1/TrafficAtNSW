package rod.bailey.trafficatnsw

import org.junit.Test
import rod.bailey.trafficatnsw.traveltime.data.SegmentDirection
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Uninstrumented tests
 */
class SegmentDirectionTest {

	@Test
	fun findNDirectionByApiToken() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken("N")
		assertEquals(SegmentDirection.N, dir)
	}

	@Test
	fun findSDirectionByApiToken() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken("S")
		assertEquals(SegmentDirection.S, dir)
	}

	@Test
	fun findEDirectionByApiToken() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken("E")
		assertEquals(SegmentDirection.E, dir)
	}

	@Test
	fun findWDirectionByApiToken() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken("W")
		assertEquals(SegmentDirection.W, dir)
	}

	@Test
	fun findDirectionByEmptyApiTokenGivesNull() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken("")
		assertNull(dir)
	}

	@Test
	fun finDirectionByWhitespaceApiTokenGivesNull() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken(" ")
		assertNull(dir)
	}

	@Test
	fun findDirectionByLowerCaseNGivesNull() {
		val dir: SegmentDirection? = SegmentDirection.findDirectionByApiToken("n")
		assertNull(dir)
	}
}