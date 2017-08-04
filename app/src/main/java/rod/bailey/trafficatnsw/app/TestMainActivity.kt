package rod.bailey.trafficatnsw.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by rodbailey on 4/8/17.
 */
class TestMainActivity: AppCompatActivity() {

	override fun onCreate(saved: Bundle?) {
		super.onCreate(saved)
		Log.d(LOG_TAG, "**** Into onCreate ****")
	}

	companion object {
		val LOG_TAG: String = TestMainActivity::class.java.simpleName
	}
}