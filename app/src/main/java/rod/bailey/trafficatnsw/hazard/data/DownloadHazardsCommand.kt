package rod.bailey.trafficatnsw.hazard.data

import rod.bailey.trafficatnsw.app.command.ICommand
import rod.bailey.trafficatnsw.service.IDataService
import rod.bailey.trafficatnsw.service.RemoteDataService
import javax.inject.Inject

/**
 * Created by rodbailey on 13/8/17.
 */
class DownloadHazardsCommand : ICommand<XHazardCollection> {

	@Inject
	constructor(dataService: IDataService) {
		this.dataService = dataService
	}

	val dataService: IDataService

	override fun execute(): XHazardCollection {
		return XHazardCollection("hazards", dataService.getHazards())
	}
}