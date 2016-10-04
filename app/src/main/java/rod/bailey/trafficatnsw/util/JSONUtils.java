package rod.bailey.trafficatnsw.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JSONUtils {

	private static final String TAG = JSONUtils.class.getSimpleName();

	private static final boolean MAX_DEBUG = false;

	public static String safeGetString(JSONObject jsonObject,
			String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		String result = null;

		try {
			result = jsonObject.getString(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse property '%s'",
						propertyName));
			}
		}

		return result;
	}

	public static int safeGetInt(JSONObject jsonObject, String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		int result = -1;
		try {
			result = jsonObject.getInt(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to prase property '%s'",
						propertyName));
			}
		}
		return result;
	}

	public static boolean safeGetBoolean(JSONObject jsonObject,
			String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		boolean result = false;

		try {
			result = jsonObject.getBoolean(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format(
						"Failed to parse boolean property %s", propertyName));
			}
		}
		return result;
	}

	public static long safeGetLong(JSONObject jsonObject, String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		long result = -1L;

		try {
			result = jsonObject.getLong(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse long property %s",
						propertyName));
			}
		}
		return result;
	}

	public static Date safeGetDate(JSONObject jsonObject, String propertyName) {
		Date result = null;

		try {
			long millis = jsonObject.getLong(propertyName);
			result = new Date(millis);
		} catch (JSONException e) {
			if (MAX_DEBUG) {
				MLog.w(TAG, "Failed to parse date in propety " + propertyName);
			}
		}
		return result;
	}

	public static double safeGetDouble(JSONObject jsonObject,
			String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		double result = -1D;
		try {
			result = jsonObject.getDouble(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse property '%s'",
						propertyName));
			}
		}
		return result;
	}

	public static double safeGetDoubleArrayElement(JSONArray jsonArray,
			int index) {
		assert jsonArray != null;
		assert index < jsonArray.length();

		double result = -1D;
		try {
			result = jsonArray.getDouble(index);
		} catch (JSONException e) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
						"Failed to get element %d from array as double", index));
		}
		return result;
	}

	public static String safeGetStringArrayElement(JSONArray jsonArray,
			int index) {
		String result = null;

		try {
			result = jsonArray.getString(index);
		} catch (JSONException e) {
			if (MAX_DEBUG)
				MLog.w(TAG, "Failed to parse string property from array");
		}
		return result;
	}

	public static JSONObject safeGetJSONObjectArrayElement(JSONArray jsonArray,
			int index) {
		JSONObject result = null;

		try {
			result = jsonArray.getJSONObject(index);
		} catch (JSONException e) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
						"Failed to get element %d from array as json object",
						index));
		}
		return result;
	}

	public static JSONArray safeGetJSONArray(JSONObject jsonObject,
			String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		JSONArray result = null;

		try {
			result = jsonObject.getJSONArray(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
						"Failed to parse array property '%s'", propertyName));
		}

		return result;
	}

	public static JSONObject safeGetJSONObject(JSONObject jsonObject,
			String propertyName) {
		assert jsonObject != null;
		assert propertyName != null;

		JSONObject result = null;
		try {
			result = jsonObject.getJSONObject(propertyName);
		} catch (JSONException e) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
						"Failed to parse json object property '%s'",
						propertyName));
		}
		return result;
	}

	public static boolean isEmpty(String str) {
		return (str != null) && (str.trim().length() > 0);
	}

	public static boolean isNonEmpty(String str) {
		return !isEmpty(str);
	}
}
