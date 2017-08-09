package rod.bailey.trafficatnsw.service

import android.graphics.Bitmap
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfig

/**
 * Created by rodbailey on 5/8/17.
 */
interface IDataService {

	fun getHazards(): List<XHazard>?
	fun getMotorwayTravelTimes(motorway: MotorwayConfig): MotorwayTravelTimesStore?
	fun getTrafficCameraImage(trafficCameraId: Int): Bitmap?
}