package rod.bailey.trafficatnsw

import android.test.ViewAsserts
import org.junit.Test
import rod.bailey.trafficatnsw.traveltime.data.SegmentDirection
import rod.bailey.trafficatnsw.traveltime.data.SegmentId
import kotlin.test.*

/**
 * Created by rodbailey on 8/8/17.
 */
class SegmentIdTest {

	@Test
	fun testTOTALConstructor() {
		val sd: SegmentId = SegmentId(SegmentDirection.N)
		assertNotNull(sd)
		assertTrue(sd.isTotalSegment)
		assertEquals(SegmentDirection.N, sd.direction)
		assertEquals(-1, sd.ordinal)
	}

	@Test
	fun testNonTotalConstructor() {
		val sd: SegmentId = SegmentId(SegmentDirection.S, 0)
		assertNotNull(sd)
		assertFalse(sd.isTotalSegment)
		assertEquals(SegmentDirection.S, sd.direction)
		assertEquals(0, sd.ordinal)
	}

	@Test
	fun testCompareSameTotalGivesZero() {
		val sd1: SegmentId = SegmentId(SegmentDirection.N)
		val sd2: SegmentId = SegmentId(SegmentDirection.N)
		assertEquals(0, sd1.compareTo(sd2))
	}

	@Test
	fun testCompareSameNonTotalGivesZero() {
		val id1: SegmentId = SegmentId(SegmentDirection.N, 0)
		val id2: SegmentId = SegmentId(SegmentDirection.N, 0)
		assertEquals(0, id1.compareTo(id2))
	}

	@Test
	fun testCompareSameDirectionDiffOrdinalsLess() {
		val id1: SegmentId = SegmentId(SegmentDirection.E, 0)
		val id2: SegmentId = SegmentId(SegmentDirection.E, 1)
		assertEquals(-1, id1.compareTo(id2))
	}

	@Test
	fun testCompareSameDirectionDiffOrdinalsMore() {
		val id1: SegmentId = SegmentId(SegmentDirection.W, 2)
		val id2: SegmentId = SegmentId(SegmentDirection.W, 1)
		assertEquals(1, id1.compareTo(id2))
	}

	@Test
	fun testCompareDiffDirectionsSameOrdinalsLess() {
		val id1: SegmentId = SegmentId(SegmentDirection.N, 0)
		val id2: SegmentId = SegmentId(SegmentDirection.S, 0)
		assertEquals(-1, id1.compareTo(id2))
	}

	@Test
	fun testCompareDiffDirectionsSameOrdinalsMore() {
		val id1: SegmentId = SegmentId(SegmentDirection.S, 0)
		val id2: SegmentId = SegmentId(SegmentDirection.N, 0)
		assertEquals(1, id1.compareTo(id2))
	}

	@Test
	fun testCompareDiffDirectionsDiffOrdinalsLess() {
		val id1: SegmentId = SegmentId(SegmentDirection.N, 7)
		val id2: SegmentId = SegmentId(SegmentDirection.S, 0)
		assertEquals(-1, id1.compareTo(id2))
	}

	@Test
	fun testCompareDiffDirectionsDiffOrdinalsMore() {
		val id1: SegmentId = SegmentId(SegmentDirection.W, 3)
		val id2: SegmentId = SegmentId(SegmentDirection.E, 4)
		assertEquals(1, id1.compareTo(id2))
	}

	@Test
	fun testParseN7() {
		val id: SegmentId? = SegmentId.parse("N7")
		assertNotNull(id)
		assertEquals(SegmentDirection.N, id?.direction)
		assertEquals(7, id?.ordinal)
	}

	@Test
	fun testParseW3() {
		val id: SegmentId? = SegmentId.parse("W3")
		assertNotNull(id)
		assertEquals(SegmentDirection.W, id?.direction)
		assertEquals(3, id?.ordinal)
	}

}
