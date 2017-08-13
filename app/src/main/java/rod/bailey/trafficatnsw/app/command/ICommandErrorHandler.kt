package rod.bailey.trafficatnsw.app.command

/**
 * Created by rodbailey on 13/8/17.
 */
interface ICommandErrorHandler {

	fun onError(ex: Throwable)
}