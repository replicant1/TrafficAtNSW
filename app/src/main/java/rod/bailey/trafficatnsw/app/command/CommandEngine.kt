package rod.bailey.trafficatnsw.app.command

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.Trace
import java.util.concurrent.Callable

/**
 * Singleton providing the ability to execute a given [ICommand] on a non-UI thread,
 * with success, error and progress monitors invoked in the UI thread so that UI
 * updates can be immediately performed.
 */
object CommandEngine {

	/**
	 * Asynchronously execute the given command on a non-UI thread.
	 *
	 * @param command The command to be executed on a non-UI thread
	 * @param monitor Displays a progress indicator to the user while the [command]
	 * is executing. Invoked in the UI thread.
	 * @param success Takes receipt of the result of successful execution of the
	 * [command] in the UI thread.
	 * @param error Invoked in the UI thread in the event of an error (exception)
	 * @return A disposable that, when disposed of, immediately disconnects [monitor],
	 * [success] and [error] from the [command] so that none of these partys
	 * receives any further notifications.
	 */
	@Trace
	fun execute(command: ICommand<*>,
				monitor: ICommandProgressMonitor,
				success: ICommandSuccessHandler,
				error: ICommandErrorHandler): Disposable {

		val disposable = Observable.fromCallable(
			object : Callable<Any> {
				override fun call(): Any {
					return command.execute()
				}
			})
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(
				object : Consumer<Any> {
					override fun accept(t: Any) {
						monitor.startProgress()
					}
				}
			)
			.subscribe(

				object : Consumer<Any> {
					override fun accept(t: Any) {
						success.onSuccess(t)
					}
				}, // onNext

				object : Consumer<Throwable> {
					override fun accept(t: Throwable) {
						monitor.stopProgress()
						error.onError(t)
					}
				}, // onError

				object : Action {
					override fun run() {
						monitor.stopProgress()
					}
				}) // onComplete
		return disposable
	}

	private val LOG_TAG: String = CommandEngine::class.java.simpleName
}