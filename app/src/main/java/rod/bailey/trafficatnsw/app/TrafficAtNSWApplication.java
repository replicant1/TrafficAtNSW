package rod.bailey.trafficatnsw.app;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.multidex.MultiDex;

/**
 * Created by rodbailey on 11/10/2016.
 */

public class TrafficAtNSWApplication extends Application {

    public static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        context = base;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
