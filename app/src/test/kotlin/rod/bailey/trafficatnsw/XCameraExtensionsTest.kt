package rod.bailey.trafficatnsw

import org.junit.Test
import rod.bailey.trafficatnsw.cameras.data.XCameraProperties
import rod.bailey.trafficatnsw.cameras.data.deriveSuburb
import rod.bailey.trafficatnsw.cameras.data.deriveTitle
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by rodbailey on 20/8/17.
 */
class XCameraExtensionsTest {

	@Test
	fun testDeriveSuburb() {
		val props: XCameraProperties = XCameraProperties(
			"SYD_NORTH", "Alison Road (Randwick)", "The View", "N", "blah.com")
		val suburb: String? = props.deriveSuburb()
		assertNotNull(suburb)
		assertEquals("Randwick", suburb)
	}

	@Test
	fun testDeriveTitle() {
		val props: XCameraProperties = XCameraProperties(
			"SYD_NORTH", "Alison Road (Randwick)","The View", "N", "blah.com")
		val title: String? = props.deriveTitle()
		assertNotNull(title)
		assertEquals("Alison Road", title)
	}

	@Test
	fun testDeriveSuburbNoBrackets() {
		val props: XCameraProperties = XCameraProperties(
			"SYD_NORTH", "Alison Road", "The View", "N", "blah.com")
		val suburb: String? = props.deriveSuburb()
		assertNull(suburb)
	}

	@Test
	fun testDeriveTitleNoBrackets() {
		val props: XCameraProperties = XCameraProperties(
			"SYD_NORTH", "Alison Road", "The View", "N","blah.com")
		val title: String? = props.deriveTitle()
		assertNotNull(title)
		assertEquals("Alison Road", title)
	}
}