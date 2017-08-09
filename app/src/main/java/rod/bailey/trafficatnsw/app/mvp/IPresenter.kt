package rod.bailey.trafficatnsw.app.mvp

/**
 * Created by rodbailey on 9/8/17.
 */
interface IPresenter<V : IView> {

	fun resumePresenting(view: V)

	fun pausePresenting()
}