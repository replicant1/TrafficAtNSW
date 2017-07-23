package rod.bailey.trafficatnsw.util

import android.content.Context
import android.util.Log

object MLog {
	private val TAG = MLog::class.java.simpleName

	enum class MLogLevel {
		DEBUG, INFO, WARNING, ERROR
	}

	private var showLogMessages: Boolean = false

	fun d(tag: String, msg: String) {
		if (showLogMessages) {
			Log.d(tag, msg)
		}
	}

	fun e(tag: String, msg: String) {
		if (showLogMessages) {
			Log.e(tag, msg)
		}
	}

	fun e(tag: String, msg: String, thr: Throwable) {
		if (showLogMessages) {
			Log.e(tag, msg, thr)
		}
	}

	fun i(tag: String, msg: String) {
		if (showLogMessages) {
			Log.i(tag, msg)
		}
	}

	fun init(config: ConfigSingleton) {
		showLogMessages = config.showLogMessages()
	}

	fun w(tag: String, msg: String) {
		if (showLogMessages) {
			Log.w(tag, msg)
		}
	}

	fun w(tag: String, msg: String, thr: Throwable) {
		if (showLogMessages) {
			Log.w(tag, msg, thr)
		}
	}

	fun w(tag: String, thr: Throwable) {
		if (showLogMessages) {
			Log.w(tag, thr)
		}
	}
}
