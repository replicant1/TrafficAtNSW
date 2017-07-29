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
	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
		context = base
	}

	override fun onCreate() {
		super.onCreate()
	}

	companion object {
		lateinit var context: Context
	}
}
