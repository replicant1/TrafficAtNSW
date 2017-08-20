package rod.bailey.trafficatnsw.cameras.data

import rod.bailey.trafficatnsw.app.command.ICommand
import rod.bailey.trafficatnsw.service.IDataService

/**
 * Created by rodbailey on 20/8/17.
 */
class DownloadCamerasCommand(val dataService: IDataService) : ICommand<XCameraCollection> {
	override fun execute(): XCameraCollection? = dataService.getTrafficCameras();
}