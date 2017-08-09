package rod.bailey.trafficatnsw.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

/**
 * Internet-related utility methods
 */
object NetUtils {

	private val LOG_TAG: String = NetUtils.javaClass.simpleName

	fun loadRemoteFileAsImage(urlStr: String): Bitmap? {
		val stream = java.net.URL(urlStr).openStream()
		return BitmapFactory.decodeStream(stream)
	}

	fun loadRemoteFileAsString(urlStr: String): String? {
		var bufferedReader: BufferedReader? = null
		var result: String? = null

		try {
			MLog.d(LOG_TAG, "Loading incidents from remote JSON file")
			val url = URL(urlStr)
			val inStreamReader = InputStreamReader(url.openStream())
			bufferedReader = BufferedReader(inStreamReader)
			var line: String?
			val lineBuffer = StringBuffer()
			var eof: Boolean = false

			while (!eof) {
				line = bufferedReader.readLine()
				if (line == null) {
					eof = true
				} else {
					lineBuffer.append(line)
				}
			}

			bufferedReader.close()
			result = lineBuffer.toString()
		}
		catch (e: Throwable) {
			MLog.w(LOG_TAG, "Failed to load hazards JSON", e)
		}
		finally {
			try {
				bufferedReader?.close()
			}
			catch (e: IOException) {
				MLog.w(LOG_TAG, e)
			}
		}

		return result
	}
}