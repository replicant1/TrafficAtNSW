package rod.bailey.trafficatnsw.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * True if this Date is some time today
 */
fun Date.isToday(): Boolean {
	val today = Calendar.getInstance()
	today.time = Date()
	val todayDay = today.get(Calendar.DAY_OF_MONTH)
	val todayMonth = today.get(Calendar.MONTH)
	val todayYear = today.get(Calendar.YEAR)

	val thisCalendar = Calendar.getInstance()
	thisCalendar.time = this
	val thisDay = thisCalendar.get(Calendar.DAY_OF_MONTH)
	val thisMonth = thisCalendar.get(Calendar.MONTH)
	val thisYear = thisCalendar.get(Calendar.YEAR)

	return (todayDay == thisDay) && (todayMonth == thisMonth)
			&& (todayYear == thisYear)
}

/**
 * If the day after this Date is today, then this Date must have been
 * yesterday
 */
fun Date.isYesterday(): Boolean {
	val cal = Calendar.getInstance()
	cal.time = this
	cal.add(Calendar.DAY_OF_MONTH, 1)
	return cal.time.isToday()
}

/**
 * @return If this Date is today, a string of form "Today at". If yesterday,
 * a string of the form "Yesterday at". Otherwise a string of form
 */
fun Date.relativeDateAndTime(capitalizeFirstLetter: Boolean): String {
	var formatStr: String = when {
		isToday() -> if (capitalizeFirstLetter) "'Today at' " else "'today at' "
		isYesterday() -> if (capitalizeFirstLetter) "'Yesterday at' " else "'yesterday at' "
		else -> "EEE d MMM "
	} + "h:mm a"

	return SimpleDateFormat(formatStr).format(this)
}