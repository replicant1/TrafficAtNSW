package rod.bailey.trafficatnsw.service

import android.graphics.Bitmap
import rod.bailey.trafficatnsw.cameras.data.XCameraCollection
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig

/**
 * Interface between this app and the remote data sources it relies upon. There are
 * two implementations of this interface provided - [RemoteDataService] and [TestDataService].
 * The latter is only used for unit testing.
 */
interface IDataService {

	/**
	 * @return List of all known incidents - but only those that have not ended. Null means
	 * the info wasn't available.
	 */
	fun getHazards(): List<XHazard>?

	/**
	 * @param motorway Static aspects of the motorway whose travel times we want.
	 * @return A store already primed with the latest travel times for the given motorway.
	 */
	fun getMotorwayTravelTimes(motorway: MotorwayConfig): MotorwayTravelTimesStore?

	/**
	 * @param trafficCameraId Identifies the traffic camera whose latest image we want.
	 * @return The image itself, or null if not available.
	 */
	fun getTrafficCameraImage(trafficCameraId: String): Bitmap?


	fun getTrafficCameras(): XCameraCollection?
}