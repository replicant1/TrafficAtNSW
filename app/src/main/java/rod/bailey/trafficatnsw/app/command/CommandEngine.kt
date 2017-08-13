package rod.bailey.trafficatnsw.app.command

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

/**
 * Created by rodbailey on 13/8/17.
 */
object CommandEngine {

	fun execute(command: ICommand<*>,
				monitor: ICommandProgressMonitor,
				success: ICommandSuccessHandler,
				error: ICommandErrorHandler): Disposable {
		val disposable = Observable.fromCallable (
			object: Callable<Any> {
				override fun call(): Any {
					return command.execute()
				}
			})
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.doOnSubscribe(
				object: Consumer<Any> {
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