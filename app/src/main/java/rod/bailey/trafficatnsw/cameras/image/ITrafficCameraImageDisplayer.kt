package rod.bailey.trafficatnsw.cameras.image

import android.graphics.Bitmap

/**
 * Can display a traffic camera imageView
 */
interface ITrafficCameraImageDisplayer {

	/**
	 * @param image Image to display
	 */
	fun displayImage(image: Bitmap)
}