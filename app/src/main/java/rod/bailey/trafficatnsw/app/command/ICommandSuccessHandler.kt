package rod.bailey.trafficatnsw.app.command

/**
 * Receives the result of successful asynchronous execution of an [ICommand].
 * ATM, the type of the result is an implicit contract between the the [ICommand]
 * and this [ICommandSuccessHandler]
 */
interface ICommandSuccessHandler {

	/**
	 * Invoked when an [ICommand] has successfully executed and returned a result.
	 * @param result The result from the [ICommand]
	 */
	fun onSuccess(result: Any)
}