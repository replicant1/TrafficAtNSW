package rod.bailey.trafficatnsw;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import rod.bailey.trafficatnsw.json.hazard.HazardCollection;
import rod.bailey.trafficatnsw.util.AssetUtils;

/**
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class HazardCollectionInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void parse09jul2013Json() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String jsonString = AssetUtils.loadAssetFileAsString(appContext, "09jul2013.json");
        HazardCollection hazards = HazardCollection.parseJson(jsonString);

        Assert.assertNotNull(hazards);
        Assert.assertNotNull(hazards.features);
        Assert.assertEquals(29,hazards.features.size());
    }
}
