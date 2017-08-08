package rod.bailey.trafficatnsw

import org.junit.Test
import rod.bailey.trafficatnsw.traveltime.data.SegmentDirection
import rod.bailey.trafficatnsw.traveltime.data.SegmentId
import java.util.*
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
	fun testSortSameDirectionDiffOrdinals() {
		val id1 = SegmentId(SegmentDirection.N, 0)
		val id2 = SegmentId(SegmentDirection.N, 1)
		val id3 = SegmentId(SegmentDirection.N, 2)
		val list = listOf(id2, id3, id1)
		Collections.sort(list)
		assertEquals(id1, list[0])
		assertEquals(id2, list[1])
		assertEquals(id3, list[2])
	}

	@Test
	fun testSortDiffDirectionDiffOrdinals() {
		val id1 = SegmentId(SegmentDirection.S, 3)
		val id2 = SegmentId(SegmentDirection.S, 2)
		val id3 = SegmentId(SegmentDirection.N, 3)
		val list = listOf(id1, id2, id3)
		Collections.sort(list)
		assertEquals(id3, list[0])
		assertEquals(id2, list[1])
		assertEquals(id1, list[2])
	}

	@Test
	fun testSortDiffDirectionWithTotal() {
		val id1 = SegmentId(SegmentDirection.N)
		val id2 = SegmentId(SegmentDirection.N, 1)
		val id3 = SegmentId(SegmentDirection.E, 2)
		val id4 = SegmentId(SegmentDirection.E)
		val list = listOf(id1, id2, id3, id4)
		Collections.sort(list)
		assertEquals(id2, list[0])
		assertEquals(id1, list[1])
		assertEquals(id3, list[2])
		assertEquals(id4, list[3])
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

	@Test
	fun testParseNTOTAL() {
		val id: SegmentId? = SegmentId.parse("NTOTAL")
		assertNotNull(id)
		assertEquals(SegmentDirection.N, id?.direction)
		assertEquals(true, id?.isTotalSegment)
	}

	@Test
	fun testParsentotalIsNull() {
		val id: SegmentId? = SegmentId.parse("ntotal")
		assertNull(id)
	}

	@Test
	fun testParsen7IsNull() {
		val id: SegmentId? = SegmentId.parse("n7")
		assertNull(id)
	}

	@Test
	fun testDotEqualsTrue() {
		val id1: SegmentId? = SegmentId.parse("N1")
		val id2: SegmentId? = SegmentId.parse("N1")
		assertNotNull(id1)
		assertNotNull(id2)
		if ((id1 != null) && (id2 != null)) {
			assertTrue(id1.equals(id2))
		}
	}

	@Test
	fun testDotEqualsSelf() {
		val id1: SegmentId? = SegmentId.parse("S1")
		assertNotNull(id1)
		if (id1 != null) {
			assertTrue(id1.equals(id1))
		}
	}

	@Test
	fun testDotEqualsFalse() {
		val id1: SegmentId? = SegmentId.parse("W1")
		val id2: SegmentId? = SegmentId.parse("W3")
		assertNotNull(id1)
		assertNotNull(id2)
		if ((id1 != null) && (id2 != null)) {
			assertFalse(id1.equals(id2))
		}
	}

	@Test
	fun testFindInSet() {
		val id1: SegmentId = SegmentId(SegmentDirection.S, 1)
		val id2: SegmentId = SegmentId(SegmentDirection.E, 2)
		val id3: SegmentId = SegmentId(SegmentDirection.N, 3)
		val id4: SegmentId = SegmentId(SegmentDirection.W, 1)

		val set: Set<SegmentId> = setOf(id1, id2, id3)

		assertEquals(4, set.size)
		assertTrue(set.contains(id1))
		assertTrue(set.contains(id2))
		assertTrue(set.contains(id3))
		assertFalse(set.contains(id4))
	}
}
