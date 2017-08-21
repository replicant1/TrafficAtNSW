package rod.bailey.trafficatnsw.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream

/**
 * Loads a given file from the /assets folder for this app.
 * @param context App context
 * @param assetFileName Simple file name to be loaded.
 * @return Contents of the asset file in String form
 */
@Throws(IOException::class)
fun Context.assetFileAsString(fileName: String): String {
	val bufferedReader: BufferedReader? = null

	try {
		val inputStream = this.assets.open(fileName)
		val size = inputStream.available()
		val buffer = ByteArray(size)
		inputStream.read(buffer)
		inputStream.close()

		return String(buffer)
	} finally {
		bufferedReader?.close()
	}
}

@Throws(IOException::class)
fun Context.assetFileAsImage(fileName: String): Bitmap? {
	val istr: InputStream = this.assets.open(fileName)
	return  BitmapFactory.decodeStream(istr)
}