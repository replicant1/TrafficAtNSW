package rod.bailey.trafficatnsw.app.command

/**
 * Created by rodbailey on 13/8/17.
 */
interface ICommand<out T : Any> {

	fun execute(): T
}