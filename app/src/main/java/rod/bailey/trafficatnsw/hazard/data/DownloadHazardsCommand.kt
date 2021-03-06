package rod.bailey.trafficatnsw.hazard.data

import rod.bailey.trafficatnsw.app.command.ICommand
import rod.bailey.trafficatnsw.service.IDataService
import javax.inject.Inject

/**
 * Command to download all known incidents (hazards) from the data service.
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