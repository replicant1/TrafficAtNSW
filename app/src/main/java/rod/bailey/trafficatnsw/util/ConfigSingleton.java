package rod.bailey.trafficatnsw.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author baileyr
 * 
 */
public class ConfigSingleton {

	private static final String LOAD_INCIDENTS_FROM_LOCAL_JSON_FILE = "LoadIncidentsFromLocalJSONFile";
	private static final String LOAD_TRAVEL_TIMES_FROM_LOCAL_JSON_FILES = "LoadTravelTimesFromLocalJSONFiles";
	private static final String LOCAL_INCIDENTS_JSON_FILE = "LocalIncidentsJSONFile";
	private static final String LOCAL_M1_JSON_FILE = "LocalM1JSONFile";
	private static final String LOCAL_M2_JSON_FILE = "LocalM2JSONFile";
	private static final String LOCAL_M4_JSON_FILE = "LocalM4JSONFile";
	private static final String LOCAL_M7_JSON_FILE = "LocalM7JSONFile";
	private static final String REMOTE_INCIDENTS_JSON_FILE = "RemoteIncidentsJSONFile";
	private static final String REMOTE_M1_JSON_FILE = "RemoteM1JSONURL";
	private static final String REMOTE_M2_JSON_FILE = "RemoteM2JSONURL";
	private static final String REMOTE_M4_JSON_FILE = "RemoteM4JSONURL";
	private static final String REMOTE_M7_JSON_FILE = "RemoteM7JSONURL";
	private static final String SHOW_LOG_MESSAGES = "ShowLogMessages";

	private static final String TAG = ConfigSingleton.class.getSimpleName();

	private Properties configProperties = new Properties();
	
	private static final ConfigSingleton singleton = new ConfigSingleton();
	
	public synchronized static ConfigSingleton getInstance() {
		return singleton;
	}

	public void init(String propertiesFileName, Context context) {
		InputStream istream;
		try {
			istream = context.getAssets().open(propertiesFileName);
			configProperties.load(istream);
		} catch (IOException e) {
			Log.e(TAG, "Failed to load config.properties file", e);
		}
	}

	private boolean getBoolProperty(String propertyName) {
		assert propertyName != null;
		return Boolean.parseBoolean(configProperties.getProperty(propertyName));
	}

	private String getStringProperty(String propertyName) {
		assert propertyName != null;
		return configProperties.getProperty(propertyName);
	}

	public boolean loadIncidentsFromLocalJSONFile() {
		return getBoolProperty(LOAD_INCIDENTS_FROM_LOCAL_JSON_FILE);
	}

	public boolean loadTravelTimesFromLocalJSONFiles() {
		return getBoolProperty(LOAD_TRAVEL_TIMES_FROM_LOCAL_JSON_FILES);
	}

	public String localIncidentsJSONFile() {
		return getStringProperty(LOCAL_INCIDENTS_JSON_FILE);
	}

	public String localM1JSONFile() {
		return getStringProperty(LOCAL_M1_JSON_FILE);
	}

	public String localM2JSONFile() {
		return getStringProperty(LOCAL_M2_JSON_FILE);
	}

	public String localM4JSONFile() {
		return getStringProperty(LOCAL_M4_JSON_FILE);
	}

	public String localM7JSONFile() {
		return getStringProperty(LOCAL_M7_JSON_FILE);
	}

	public String remoteIncidentsJSONFile() {
		return getStringProperty(REMOTE_INCIDENTS_JSON_FILE);
	}

	public String remoteM1JSONFile() {
		return getStringProperty(REMOTE_M1_JSON_FILE);
	}

	public String remoteM2JSONFile() {
		return getStringProperty(REMOTE_M2_JSON_FILE);
	}

	public String remoteM4JSONFile() {
		return getStringProperty(REMOTE_M4_JSON_FILE);
	}

	public String remoteM7JSONFile() {
		return getStringProperty(REMOTE_M7_JSON_FILE);
	}

	/**
	 * @return If YES, MLog messages appear on the console. This slows things
	 *         down considerably.
	 */
	public boolean showLogMessages() {
		return getBoolProperty(SHOW_LOG_MESSAGES);
	}

}
