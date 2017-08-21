package rod.bailey.trafficatnsw

import org.junit.Test
import rod.bailey.trafficatnsw.util.isToday
import rod.bailey.trafficatnsw.util.isYesterday
import rod.bailey.trafficatnsw.util.relativeDateAndTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by rodbailey on 21/8/17.
 */
class DateExtensionsTest {

	@Test
	fun testTodayIsToday() {
		assertTrue(Date().isToday())
	}

	@Test
	fun yesterdayIsYesterday() {
		val yesterday:Date = Date(Date().time - (24 * 60 * 60 * 1000))
		assertTrue(yesterday.isYesterday())
	}

	@Test
	fun testTodayAt3pmRelativeString() {
		val cal = Calendar.getInstance()
		cal.time = Date()
		cal.set(Calendar.HOUR, 3)
		cal.set(Calendar.MINUTE, 0)
		cal.set(Calendar.MILLISECOND, 0)
		cal.set(Calendar.AM_PM, Calendar.PM)
		assertEquals("Today at 3:00 PM",
				Date(cal.time.time).relativeDateAndTime(true))
		assertEquals("today at 3:00 PM",
				Date(cal.time.time).relativeDateAndTime(false))
	}

	@Test
	fun testYesterdayAt11amRelativeString() {
		val cal = Calendar.getInstance()
		cal.time = Date(Date().time - (24 * 60 * 60 * 1000))
		cal.set(Calendar.HOUR, 11)
		cal.set(Calendar.MINUTE, 0)
		cal.set(Calendar.MILLISECOND, 0)
		cal.set(Calendar.AM_PM, Calendar.AM)
		assertEquals("Yesterday at 11:00 AM",
				Date(cal.time.time).relativeDateAndTime(true))
		assertEquals("yesterday at 11:00 AM",
				Date(cal.time.time).relativeDateAndTime(false))
	}

	@Test
	fun testTwoDaysAgoAt0307amRelativeString() {
		val cal = Calendar.getInstance()
		cal.time = Date()
		cal.set(Calendar.HOUR, 3)
		cal.set(Calendar.MINUTE, 7)
		cal.set(Calendar.MILLISECOND, 0)
		cal.set(Calendar.AM_PM, Calendar.AM)

		cal.roll(Calendar.DAY_OF_MONTH, -2)

		val rolledBackDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
		val rolledBackMonth = cal.get(Calendar.MONTH)
		val rolledBackDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

		val rolledBackDayOfWeekShortName = dayOfWeekShortName(rolledBackDayOfWeek)
		val rolledBackMonthShortName = monthOfYearShortName(rolledBackMonth)

		// e.g. "Sat 19 Aug 3:07 AM"
		val rolledBackStr = String.format("%s %d %s 3:07 AM",
				rolledBackDayOfWeekShortName, rolledBackDayOfMonth, rolledBackMonthShortName)
		assertEquals(rolledBackStr, Date(cal.time.time).relativeDateAndTime(true))
	}

	fun dayOfWeekShortName(dayOfWeekIndex: Int): String {
		return when (dayOfWeekIndex) {
			Calendar.MONDAY -> "Mon"
			Calendar.TUESDAY -> "Tue"
			Calendar.WEDNESDAY -> "Wed"
			Calendar.THURSDAY -> "Thu"
			Calendar.FRIDAY -> "Fri"
			Calendar.SATURDAY -> "Sat"
			Calendar.SUNDAY -> "Sun"
			else -> "Ouch"
		}
	}

	fun monthOfYearShortName(monthOfYearIndex: Int): String {
		return when (monthOfYearIndex) {
			Calendar.JANUARY -> "Jan"
			Calendar.FEBRUARY -> "Feb"
			Calendar.MARCH -> "Mar"
			Calendar.APRIL -> "Apr"
			Calendar.MAY -> "May"
			Calendar.JUNE -> "Jun"
			Calendar.JULY -> "Jul"
			Calendar.AUGUST -> "Aug"
			Calendar.SEPTEMBER -> "Sep"
			Calendar.OCTOBER -> "Oct"
			Calendar.NOVEMBER -> "Nov"
			Calendar.DECEMBER -> "Dec"
			else -> "Ouch"
		}
	}
}