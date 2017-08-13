package rod.bailey.trafficatnsw.hazard.overview

import rod.bailey.trafficatnsw.app.command.ICommandSuccessHandler
import rod.bailey.trafficatnsw.hazard.data.XHazardCollection

/**
 * Created by rodbailey on 13/8/17.
 */
class DownloadHazardsSuccessHandler() : ICommandSuccessHandler {
	override fun onSuccess(result: Any) {
		System.out.println("** DHSH.onSuccess **. result=${result}")
	}
}