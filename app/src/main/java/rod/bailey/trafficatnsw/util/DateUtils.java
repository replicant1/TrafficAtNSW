package rod.bailey.trafficatnsw.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static boolean isYesterday(Date someDate) {
		// If the day after 'someDate' is today, then someDate must have been
		// yesterday
		Calendar cal = Calendar.getInstance();
		cal.setTime(someDate);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return isToday(cal.getTime());
	}

	public static boolean isToday(Date someDate) {
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());

		int todayDay = today.get(Calendar.DAY_OF_MONTH);
		int todayMonth = today.get(Calendar.MONTH);
		int todayYear = today.get(Calendar.YEAR);

		Calendar some = Calendar.getInstance();
		some.setTime(someDate);
		int someDay = some.get(Calendar.DAY_OF_MONTH);
		int someMonth = some.get(Calendar.MONTH);
		int someYear = some.get(Calendar.YEAR);

		return (todayDay == someDay) && (todayMonth == someMonth)
				&& (todayYear == someYear);
	}

	public static String relativeDateAndTime(Date someDate, boolean capitalize) {

		String formatStr = new String();

		if (isToday(someDate)) {
			formatStr = capitalize ? "'Today at' " : "'today at' ";
		} else if (isYesterday(someDate)) {
			formatStr = capitalize ? "'Yesterday at' " : "'yesterday at' ";
		} else {
			formatStr = formatStr + "EEE d MMM ";
		}
		
		formatStr += "h:mm a";

		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

		return sdf.format(someDate);
	}
}
