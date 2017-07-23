package rod.bailey.trafficatnsw.traveltime.common;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import rod.bailey.trafficatnsw.traveltime.config.TravelTimeConfig;
import rod.bailey.trafficatnsw.util.ConfigSingleton;
import rod.bailey.trafficatnsw.util.MLog;

public class TravelTimesSingleton {
	private static final TravelTimesSingleton singleton = new TravelTimesSingleton();

	private static final String TAG = TravelTimesSingleton.class
			.getSimpleName();

	public static synchronized TravelTimesSingleton getSingleton() {
		return singleton;
	}

	private TravelTimeConfig m1Config;
	private MotorwayTravelTimesDatabase m1db;
	private TravelTimeConfig m2Config;
	private MotorwayTravelTimesDatabase m2db;

	private TravelTimeConfig m4Config;
	private MotorwayTravelTimesDatabase m4db;
	private TravelTimeConfig m7Config;
	private MotorwayTravelTimesDatabase m7db;

	private ConfigSingleton configSingleton;

	private TravelTimeConfig createM1Config() {
		return new TravelTimeConfig("M1", //
				"Northbound", //
				"Southbound", //
				"N", //
				"S", //
				"excluded_from_total_m1", //
				configSingleton.remoteM1JSONFile(), //
				configSingleton.localM1JSONFile());
	}

	private TravelTimeConfig createM2Config() {
		return new TravelTimeConfig("M2", //
				"Eastbound", //
				"Westbound", //
				"E", //
				"W", //
				"excluded_from_total_m2", //
				configSingleton.remoteM2JSONFile(), //
				configSingleton.localM2JSONFile());
	}

	private TravelTimeConfig createM4Config() {
		return new TravelTimeConfig("M4", //
				"Eastbound", //
				"Westbound", //
				"E", //
				"W", //
				"excluded_from_total_m4", //
				configSingleton.remoteM4JSONFile(), //
				configSingleton.localM4JSONFile());
	}

	private TravelTimeConfig createM7Config() {
		return new TravelTimeConfig("M7", //
				"Northbound", //
				"Southbound", //
				"N", //
				"S", //
				"excluded_from_total_m7", //
				configSingleton.remoteM7JSONFile(), //
				configSingleton.localM7JSONFile());
	}

	public MotorwayTravelTimesDatabase getM1Database() {
		return m1db;
	}
	
	public TravelTimeConfig getM1Config() {
		return m1Config;
	}

	public MotorwayTravelTimesDatabase getM2Database() {
		return m2db;
	}
	
	public TravelTimeConfig getM2Config() {
		return m2Config;
	}

	public MotorwayTravelTimesDatabase getM4Database() {
		return m4db;
	}
	
	public TravelTimeConfig getM4Config() {
		return m4Config;
	}

	public MotorwayTravelTimesDatabase getM7Database() {
		return m7db;
	}
	
	public TravelTimeConfig getM7Config() {
		return m7Config;
	}

	public synchronized void init(Context ctx) {
		configSingleton = ConfigSingleton.getInstance();

		if ((m1Config == null) || (m2Config == null) || (m4Config == null)
				|| (m7Config == null)) {
			m1Config = createM1Config();
			m2Config = createM2Config();
			m4Config = createM4Config();
			m7Config = createM7Config();

			// TODO: initXXTravelTimes for other motorways - move to async
			// activity. Switch on configSingleton to see if thsould be loaded
			// // remotely or locally
			// m1db = loadTravelTimesFromLocalJSONFile(ctx, m1Config);
			// m2db = loadTravelTimesFromLocalJSONFile(ctx, m2Config);
			// m4db = loadTravelTimesFromLocalJSONFile(ctx, m4Config);
			// m7db = loadTravelTimesFromLocalJSONFile(ctx, m7Config);
		}
	}

	public MotorwayTravelTimesDatabase loadTravelTimesFromLocalJSONFile(
			Context ctx, TravelTimeConfig config) {
		MotorwayTravelTimesDatabase result = null;

		try {
			String assetFileName = config.localJsonFileName;
			MLog.INSTANCE.d(TAG, String.format("Fetching %s from assets folder",
					assetFileName));

			InputStream input = ctx.getAssets().open(assetFileName);
			int size = input.available();

			MLog.INSTANCE.d(TAG, String.format("Found %d available bytes in %s", size,
					assetFileName));
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();

			// byte buffer into a string
			String text = new String(buffer);

			List<TravelTime> tts = TravelTime
					.parseTravelTimesFromJsonFile(text);

			result = new MotorwayTravelTimesDatabase(ctx, config);
			result.primeWithTravelTimes(tts);
		} catch (IOException ioe) {
			MLog.INSTANCE.e(TAG, "M4 JSON parsing failed", ioe);
		}

		return result;
	}

	/**
	 * Blocking
	 * 
	 * @param jsonUrl
	 * @return
	 */
	public MotorwayTravelTimesDatabase loadTravelTimesFromRemoteJSONFile(Context ctx,
			TravelTimeConfig config) {
		
		MLog.INSTANCE.i(TAG, "Beginning load of " + config.motorwayName + " travel times from remote file "
				+ config.remoteJsonUrl);

		MotorwayTravelTimesDatabase result = null;
		BufferedReader bufferedReader = null;

		try {
			URL jsonUrl = new URL(config.remoteJsonUrl);
			InputStreamReader instreamReader = new InputStreamReader(
					jsonUrl.openStream());
			bufferedReader = new BufferedReader(instreamReader);

			String line = null;
			StringBuffer lineBuffer = new StringBuffer();

			while ((line = bufferedReader.readLine()) != null) {
				lineBuffer.append(line);
			}

			String text = new String(lineBuffer.toString());

			List<TravelTime> tts = TravelTime
					.parseTravelTimesFromJsonFile(text);
			result = new MotorwayTravelTimesDatabase(ctx, config);
			result.primeWithTravelTimes(tts);
		} catch (Exception e) {
			MLog.INSTANCE.e(TAG, "Failed to retrive and/or parse travel times for "
					+ config.motorwayName, e);
			result = null;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	public synchronized boolean isInitialised() {
		return (m1Config != null) && (m2Config != null) && (m4Config != null)
				&& (m7Config != null);
	}

}
