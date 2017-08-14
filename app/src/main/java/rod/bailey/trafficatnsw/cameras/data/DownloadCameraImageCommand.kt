package rod.bailey.trafficatnsw.cameras.data

import android.graphics.Bitmap
import rod.bailey.trafficatnsw.app.command.ICommand
import rod.bailey.trafficatnsw.service.IDataService

/**
 * When executed by the [CommandEngine], loads the image for a particular
 * traffic camera using the [IDataService].
 */
class DownloadCameraImageCommand(val dataService: IDataService,
								 val cameraId: Int) : ICommand<Bitmap?> {

	override fun execute(): Bitmap? {
		return dataService.getTrafficCameraImage(cameraId)
	}
}