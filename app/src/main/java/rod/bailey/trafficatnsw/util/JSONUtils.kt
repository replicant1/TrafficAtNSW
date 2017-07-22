package rod.bailey.trafficatnsw.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object JSONUtils {
	private val TAG = JSONUtils::class.java.simpleName
	private val MAX_DEBUG = false

	fun safeGetString(jsonObject: JSONObject?, propertyName: String): String? {
		var result: String? = null

		try {
			result = jsonObject?.getString(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse property '%s'",
										  propertyName))
			}
		}

		return result
	}

	fun safeGetInt(jsonObject: JSONObject?, propertyName: String): Int? {
		var result:Int? = null
		try {
			result = jsonObject?.getInt(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse property '%s'",
										  propertyName))
			}
		}

		return result
	}

	fun safeGetBoolean(jsonObject: JSONObject?, propertyName: String): Boolean? {
		var result:Boolean? = null

		try {
			result = jsonObject?.getBoolean(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format(
					"Failed to parse boolean property %s", propertyName))
			}
		}

		return result
	}

	fun safeGetLong(jsonObject: JSONObject?, propertyName: String?): Long? {
		var result:Long? = null

		try {
			result = jsonObject?.getLong(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse long property %s",
										  propertyName))
			}
		}

		return result
	}

	fun safeGetDate(jsonObject: JSONObject?, propertyName: String): Date? {
		var result: Date? = null

		try {
			val millis:Long ? = jsonObject?.getLong(propertyName)
			if (millis != null) {
				result = Date(millis)
			}
		}
		catch (e: JSONException) {
			if (MAX_DEBUG) {
				MLog.w(TAG, "Failed to parse date in propety " + propertyName)
			}
		}

		return result
	}

	fun safeGetDouble(jsonObject: JSONObject?, propertyName: String): Double? {
		var result:Double? = null
		try {
			result = jsonObject?.getDouble(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG) {
				MLog.w(TAG, String.format("Failed to parse property '%s'",
										  propertyName))
			}
		}

		return result
	}

	fun safeGetDoubleArrayElement(jsonArray: JSONArray?, index: Int): Double? {
		var result:Double? = null
		try {
			result = jsonArray?.getDouble(index)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
					"Failed to get element %d from array as double", index))
		}

		return result
	}

	fun safeGetStringArrayElement(jsonArray: JSONArray?, index: Int): String? {
		var result: String? = null

		try {
			result = jsonArray?.getString(index)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG)
				MLog.w(TAG, "Failed to parse string property from array")
		}

		return result
	}

	fun safeGetJSONObjectArrayElement(jsonArray: JSONArray?, index: Int): JSONObject? {
		var result: JSONObject? = null

		try {
			result = jsonArray?.getJSONObject(index)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
					"Failed to get element %d from array as json object",
					index))
		}

		return result
	}

	fun safeGetJSONArray(jsonObject: JSONObject?, propertyName: String): JSONArray? {
		var result: JSONArray? = null

		try {
			result = jsonObject?.getJSONArray(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
					"Failed to parse array property '%s'", propertyName))
		}

		return result
	}

	fun safeGetJSONObject(jsonObject: JSONObject?, propertyName: String): JSONObject? {
		var result: JSONObject? = null
		try {
			result = jsonObject?.getJSONObject(propertyName)
		}
		catch (e: JSONException) {
			if (MAX_DEBUG)
				MLog.w(TAG, String.format(
					"Failed to parse json object property '%s'",
					propertyName))
		}

		return result
	}

	fun isEmpty(str: String?): Boolean {
		return str != null && str.trim { it <= ' ' }.length > 0
	}

	fun isNonEmpty(str: String?): Boolean {
		return !isEmpty(str)
	}
}
