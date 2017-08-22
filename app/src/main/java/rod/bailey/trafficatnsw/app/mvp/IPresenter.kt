package rod.bailey.trafficatnsw.app.mvp

/**
 * This interface is used throughout the app to identify the Presenter
 * for each screen. Each screen is an IView.
 *
 * @see IView
 */
interface IPresenter<V : IView> {

	/**
	 * Call this from then end of your views' onResume() or onCreateView() method.
	 *
	 * @param view The IView to be presented by this IPresenter
	 * @param initData Initialization data the presenter needs to properly present the IView
	 */
	fun onAttachView(view: V, vararg initData: Any?)

	/**
	 * Call this from your views' onPause() or onDestory() method. This will terminate
	 * any outstanding async server requests this presenter has initiated.
	 */
	fun onDetachView()
}