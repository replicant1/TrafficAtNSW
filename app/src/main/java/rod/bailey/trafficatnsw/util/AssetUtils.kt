package rod.bailey.trafficatnsw.util

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream

/**
 * A collection of static utility methods for operating on the contents of the "assets" directory for this app.
 */
object AssetUtils {
	/**
	 * Loads a given file from the /assets folder for this app.

	 * @param context App context
	 * *
	 * @param assetFileName Simple file name to be loaded.
	 * *
	 * @return Contents of the asset file in String form
	 */
	@Throws(IOException::class)
	fun loadAssetFileAsString(context: Context, assetFileName: String): String {
		val bufferedReader: BufferedReader? = null

		try {
			val inputStream = context.assets.open(assetFileName)
			val size = inputStream.available()
			val buffer = ByteArray(size)
			inputStream.read(buffer)
			inputStream.close()

			return String(buffer)
		}
		finally {
			bufferedReader?.close()
		}
	}
}
