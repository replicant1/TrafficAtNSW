package rod.bailey.trafficatnsw.util;

import android.content.Context;
import android.util.Log;

public class MLog {

	@SuppressWarnings("unused")
	private static final String TAG = MLog.class.getSimpleName();

	public enum MLogLevel {
		DEBUG, INFO, WARNING, ERROR;
	}

	private static boolean showLogMessages;

	public static void d(String tag, String msg) {
		if (showLogMessages) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (showLogMessages) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable thr) {
		if (showLogMessages) {
			Log.e(tag, msg, thr);
		}
	}

	public static void i(String tag, String msg) {
		if (showLogMessages) {
			Log.i(tag, msg);
		}
	}

	public static void init(ConfigSingleton config, Context ctx) {
		showLogMessages = config.showLogMessages();

	}

	public static void w(String tag, String msg) {
		if (showLogMessages) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable thr) {
		if (showLogMessages) {
			Log.w(tag, msg, thr);
		}
	}

	public static void w(String tag, Throwable thr) {
		if (showLogMessages) {
			Log.w(tag, thr);
		}
	}
}
