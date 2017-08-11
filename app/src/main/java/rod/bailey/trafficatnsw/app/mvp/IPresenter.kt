package rod.bailey.trafficatnsw.app.mvp

/**
 * This interface is used throughout the app to identify the Presenter
 * for each screen. Each screen is an IView.
 *
 * @see IView
 */
interface IPresenter<V : IView> {

	/**
	 * Call this from then end of your views' onIViewCreated() method.
	 *
	 * @param view The IView to be presented
	 * @param initData Initialization initData the presenter needs to parameterize its' operation.
	 */
	fun onIViewCreated(view: V, vararg initData: Any?)

	/**
	 * Call this from your views' onDestroyView() method. This will terminate
	 * any outstanding async server requests this presenter has initiated.
	 */
	fun onIViewDestroyed()
}