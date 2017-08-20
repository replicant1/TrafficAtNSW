package rod.bailey.trafficatnsw.cameras.data

import rod.bailey.trafficatnsw.app.command.ICommand
import rod.bailey.trafficatnsw.service.IDataService

/**
 * Asynchronous retrieval of the JSON file that defines all the traffic cameras that are
 * available, when passed into [CommandEngine]
 */
class DownloadCamerasCommand(val dataService: IDataService) : ICommand<XCameraCollection> {
	override fun execute(): XCameraCollection? = dataService.getTrafficCameras();
}