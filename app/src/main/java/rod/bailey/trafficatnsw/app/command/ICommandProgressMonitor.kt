package rod.bailey.trafficatnsw.app.command

/**
 * Indeterminate progress monitor
 */
interface ICommandProgressMonitor {

	fun startProgress()

	fun stopProgress()
}