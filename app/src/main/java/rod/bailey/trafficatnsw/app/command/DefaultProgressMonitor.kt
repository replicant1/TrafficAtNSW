package rod.bailey.trafficatnsw.app.command

/**
 * Created by rodbailey on 13/8/17.
 */
class DefaultProgressMonitor: ICommandProgressMonitor {
	override fun startProgress() {
		System.out.println("** DefaultProgressMonitor.startProgress **")
	}

	override fun stopProgress() {
		System.out.println("** DefaultProgressMonitor.stopProgress **")
	}
}