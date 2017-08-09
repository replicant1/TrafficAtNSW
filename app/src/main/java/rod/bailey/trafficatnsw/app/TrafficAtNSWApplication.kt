package rod.bailey.trafficatnsw.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import dagger.Component
import org.androidannotations.annotations.EApplication
import javax.inject.Singleton

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
