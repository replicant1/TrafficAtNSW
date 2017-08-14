package rod.bailey.trafficatnsw.traveltime.data

import rod.bailey.trafficatnsw.app.command.ICommand
import rod.bailey.trafficatnsw.service.IDataService
import android.content.Context

/**
 * Command to download all travel times data for the motorway identified by [motorwayConfig]
 */
class DownloadTravelTimesCommand(
	val ctx: Context,
	val motorwayConfig: MotorwayConfig,
	val dataService: IDataService): ICommand<MotorwayTravelTimesStore> {

	override fun execute(): MotorwayTravelTimesStore {
		return dataService.getMotorwayTravelTimes(motorwayConfig)
			?: MotorwayTravelTimesStore(ctx, motorwayConfig)
	}
}