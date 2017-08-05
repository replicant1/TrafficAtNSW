package rod.bailey.trafficatnsw.common.service

import android.graphics.Bitmap
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesDatabase
import rod.bailey.trafficatnsw.traveltime.data.TravelTimeConfig

/**
 * Created by rodbailey on 5/8/17.
 */
interface IDataService {

	fun getHazards(): List<XHazard>?
	fun getMotorwayTravelTimes(motorway: TravelTimeConfig): MotorwayTravelTimesDatabase?
	fun getTrafficCameraImage(trafficCameraId: Int): Bitmap?
}