package rod.bailey.trafficatnsw.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateUtils {
	fun isYesterday(someDate: Date): Boolean {
		// If the day after 'someDate' is today, then someDate must have been
		// yesterday
		val cal = Calendar.getInstance()
		cal.time = someDate
		cal.add(Calendar.DAY_OF_MONTH, 1)
		return isToday(cal.time)
	}

	fun isToday(someDate: Date): Boolean {
		val today = Calendar.getInstance()
		today.time = Date()
		val todayDay = today.get(Calendar.DAY_OF_MONTH)
		val todayMonth = today.get(Calendar.MONTH)
		val todayYear = today.get(Calendar.YEAR)
		val some = Calendar.getInstance()
		some.time = someDate
		val someDay = some.get(Calendar.DAY_OF_MONTH)
		val someMonth = some.get(Calendar.MONTH)
		val someYear = some.get(Calendar.YEAR)

		return todayDay == someDay && todayMonth == someMonth
			&& todayYear == someYear
	}

	fun relativeDateAndTime(someDate: Date, capitalize: Boolean): String {
		var formatStr = String()

		if (isToday(someDate)) {
			formatStr = if (capitalize) "'Today at' " else "'today at' "
		} else if (isYesterday(someDate)) {
			formatStr = if (capitalize) "'Yesterday at' " else "'yesterday at' "
		} else {
			formatStr = formatStr + "EEE d MMM "
		}

		formatStr += "h:mm a"
		val sdf = SimpleDateFormat(formatStr)

		return sdf.format(someDate)
	}
}
