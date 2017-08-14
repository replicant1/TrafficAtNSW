package rod.bailey.trafficatnsw.app.command

/**
 * Invoked by the [CommandEngine] when an [ICommand] experiences an error while executing
 * or handling a returned result.
 */
interface ICommandErrorHandler {

	/**
	 * @param ex The original cause of the error.
	 */
	fun onError(ex: Throwable)
}