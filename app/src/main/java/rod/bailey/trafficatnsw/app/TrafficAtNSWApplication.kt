package rod.bailey.trafficatnsw.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import org.androidannotations.annotations.EApplication
import rod.bailey.trafficatnsw.dagger.AppComponent
import rod.bailey.trafficatnsw.dagger.AppModule
import rod.bailey.trafficatnsw.dagger.DaggerAppComponent
import rod.bailey.trafficatnsw.dagger.RemoteDataServiceModule

/**
 * Application class represents Traffic@NSW and exists solely to provide
 * easy global access to the app context.
 */
@EApplication
open class TrafficAtNSWApplication : Application() {

	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
		context = base
	}

	override fun onCreate() {
		super.onCreate()
		graph = createComponent()
		graph.inject(this)
	}

	open fun createComponent() : AppComponent {
		return DaggerAppComponent.builder()
			.appModule(AppModule(this))
			.remoteDataServiceModule(RemoteDataServiceModule(this))
			.build();
	}

	companion object {
		lateinit var context: Context
		@JvmStatic lateinit var graph: AppComponent
	}
}
