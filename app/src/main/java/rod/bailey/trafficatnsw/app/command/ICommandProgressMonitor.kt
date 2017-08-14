package rod.bailey.trafficatnsw.app.command

/**
 * Used to start and stop the display of an indeterminate progress monitor. Invoked
 * appropriately by the [CommandEngine]. In event of an error, the [CommandEngine] will
 * still call the [stopProgress] method before calling the [ICommandErrorHandler].
 *
 * The progress message to display should be passed into the implementors constructor.
 */
interface ICommandProgressMonitor {

	/**
	 * Show the indeterminate progress monitor on the screen, because the associated
	 * [ICommand] has begun execution on a non-UI thread.
	 */
	fun startProgress()

	/**
	 * Hide the indeterminate progress monitor, because the associated [ICommand]
	 * has completed or failed.
	 */
	fun stopProgress()
}