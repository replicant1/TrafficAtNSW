package rod.bailey.trafficatnsw.app.command

/**
 * Created by rodbailey on 13/8/17.
 */
class DefaultErrorHandler: ICommandErrorHandler {
	override fun onError(ex: Throwable) {
		System.out.println("** DefaultErrorHandler.onError() ***" + ex)
	}
}