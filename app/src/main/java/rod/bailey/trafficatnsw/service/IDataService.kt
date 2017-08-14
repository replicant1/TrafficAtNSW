package rod.bailey.trafficatnsw.service

import android.graphics.Bitmap
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig

/**
 * Interface between this app and the remote data sources it relies upon. There are
 * two implementations of this interface provided - [RemoteDataService] and [TestDataService].
 * The latter is only used for unit testing.
 */
interface IDataService {
	fun getHazards(): List<XHazard>?
	fun getMotorwayTravelTimes(motorway: MotorwayConfig): MotorwayTravelTimesStore?
	fun getTrafficCameraImage(trafficCameraId: Int): Bitmap?
}