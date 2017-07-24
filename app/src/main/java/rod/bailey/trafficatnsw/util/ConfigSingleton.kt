package rod.bailey.trafficatnsw.util

import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.util.Properties

/**
 * Provides global access to the config.properties file
 */
class ConfigSingleton private constructor() {
	private object Holder {
		val INSTANCE = ConfigSingleton()
	}

	companion object {
		/**
		 * The names of the properties in config.properties
		 */
		private val LOAD_INCIDENTS_FROM_LOCAL_JSON_FILE = "LoadIncidentsFromLocalJSONFile"
		private val LOAD_TRAVEL_TIMES_FROM_LOCAL_JSON_FILES = "LoadTravelTimesFromLocalJSONFiles"
		private val LOCAL_INCIDENTS_JSON_FILE = "LocalIncidentsJSONFile"
		private val LOCAL_M1_JSON_FILE = "LocalM1JSONFile"
		private val LOCAL_M2_JSON_FILE = "LocalM2JSONFile"
		private val LOCAL_M4_JSON_FILE = "LocalM4JSONFile"
		private val LOCAL_M7_JSON_FILE = "LocalM7JSONFile"
		private val REMOTE_INCIDENTS_JSON_FILE = "RemoteIncidentsJSONFile"
		private val REMOTE_M1_JSON_FILE = "RemoteM1JSONURL"
		private val REMOTE_M2_JSON_FILE = "RemoteM2JSONURL"
		private val REMOTE_M4_JSON_FILE = "RemoteM4JSONURL"
		private val REMOTE_M7_JSON_FILE = "RemoteM7JSONURL"
		private val SHOW_LOG_MESSAGES = "ShowLogMessages"

		private val LOG_TAG = ConfigSingleton::class.java.simpleName
		val instance: ConfigSingleton by lazy { Holder.INSTANCE }
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

	private fun getBoolProperty(propertyName: String?): Boolean {
		return java.lang.Boolean.parseBoolean(configProperties.getProperty(propertyName))
	}

	private fun getStringProperty(propertyName: String?): String {
		return configProperties.getProperty(propertyName)
	}

	fun loadIncidentsFromLocalJSONFile(): Boolean {
		return getBoolProperty(LOAD_INCIDENTS_FROM_LOCAL_JSON_FILE)
	}

	fun loadTravelTimesFromLocalJSONFiles(): Boolean {
		return getBoolProperty(LOAD_TRAVEL_TIMES_FROM_LOCAL_JSON_FILES)
	}

	fun localIncidentsJSONFile(): String {
		return getStringProperty(LOCAL_INCIDENTS_JSON_FILE)
	}

	fun localM1JSONFile(): String {
		return getStringProperty(LOCAL_M1_JSON_FILE)
	}

	fun localM2JSONFile(): String {
		return getStringProperty(LOCAL_M2_JSON_FILE)
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

	fun remoteM2JSONFile(): String {
		return getStringProperty(REMOTE_M2_JSON_FILE)
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
