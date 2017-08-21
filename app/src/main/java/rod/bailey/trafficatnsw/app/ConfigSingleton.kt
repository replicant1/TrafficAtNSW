package rod.bailey.trafficatnsw.app

import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.util.Properties

/**
 * Provides global access to the config.properties file. Always inject this to make sure
 * you get the singleton instance.
 */
class ConfigSingleton {
	companion object {
		// The names of the properties in config.properties
		private val DURATION_TIME_FORMAT = "DurationTimeFormat"
		private val LOCAL_M1_JSON_FILE = "LocalM1JSONFile"
		private val LOCAL_M4_JSON_FILE = "LocalM4JSONFile"
		private val LOCAL_M7_JSON_FILE = "LocalM7JSONFile"
		private val REMOTE_INCIDENTS_JSON_FILE = "RemoteIncidentsJSONFile"
		private val REMOTE_M1_JSON_FILE = "RemoteM1JSONURL"
		private val REMOTE_M4_JSON_FILE = "RemoteM4JSONURL"
		private val REMOTE_M7_JSON_FILE = "RemoteM7JSONURL"
		private val SHOW_LOG_MESSAGES = "ShowLogMessages"
		private val HAZARD_TIME_FORMAT = "HazardTimeFormat"
		private val HAZARD_DATE_FORMAT = "HazardDateFormat"

		private val LOG_TAG = ConfigSingleton::class.java.simpleName
	}

	private val configProperties = Properties()

	@Synchronized
	fun init(propertiesFileName: String, context: Context) {
		val istream: InputStream
		try {
			istream = context.assets.open(propertiesFileName)
			configProperties.load(istream)
		}
		catch (e: IOException) {
			Log.e(LOG_TAG, "Failed to load config.properties file", e)
		}
	}

	fun durationTimeFormat(): String {
		return configProperties.getProperty(DURATION_TIME_FORMAT)
	}

	fun hazardTimeFormat(): String {
		return configProperties.getProperty(HAZARD_TIME_FORMAT)
	}

	fun hazardDateFormat(): String {
		return configProperties.getProperty(HAZARD_DATE_FORMAT)
	}

	private fun getBoolProperty(propertyName: String?): Boolean {
		return java.lang.Boolean.parseBoolean(configProperties.getProperty(propertyName))
	}

	private fun getStringProperty(propertyName: String?): String {
		return configProperties.getProperty(propertyName)
	}

	fun localM1JSONFile(): String {
		return getStringProperty(LOCAL_M1_JSON_FILE)
	}

	fun localM4JSONFile(): String {
		return getStringProperty(LOCAL_M4_JSON_FILE)
	}

	fun localM7JSONFile(): String {
		return getStringProperty(LOCAL_M7_JSON_FILE)
	}

	fun remoteIncidentsJSONFile(): String {
		return getStringProperty(REMOTE_INCIDENTS_JSON_FILE)
	}

	fun remoteM1JSONFile(): String {
		return getStringProperty(REMOTE_M1_JSON_FILE)
	}

	fun remoteM4JSONFile(): String {
		return getStringProperty(REMOTE_M4_JSON_FILE)
	}

	fun remoteM7JSONFile(): String {
		return getStringProperty(REMOTE_M7_JSON_FILE)
	}

	/**
	 * @return If YES, MLog messages appear on the console. This slows things
	 * *         down considerably.
	 */
	fun showLogMessages(): Boolean {
		return getBoolProperty(SHOW_LOG_MESSAGES)
	}

}
