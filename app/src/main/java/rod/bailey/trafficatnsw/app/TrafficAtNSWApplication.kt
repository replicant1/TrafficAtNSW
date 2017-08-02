package rod.bailey.trafficatnsw.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import org.androidannotations.annotations.EApplication

/**
 * Application class represents Traffic@NSW and exists solely to provide
 * easy global access to the app context.
 */
@EApplication
open class TrafficAtNSWApplication : Application() {

//	val component: AppComponent by lazy {
//		DaggerAppComponent.builder()
//			.appModule(AppModule(this))
//			.build()
//	}

	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
		context = base
	}

	override fun onCreate() {
		super.onCreate()
		graph = DaggerAppComponent.builder().appModule(AppModule(this)).build()
		graph.inject(this)
	}

	companion object {
		lateinit var context: Context
		@JvmStatic lateinit var graph: AppComponent
	}
}
