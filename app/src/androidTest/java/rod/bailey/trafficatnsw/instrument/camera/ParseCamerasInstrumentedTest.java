package rod.bailey.trafficatnsw.instrument.camera;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.cameras.data.XCamera;
import rod.bailey.trafficatnsw.cameras.data.XCameraCollection;
import rod.bailey.trafficatnsw.cameras.data.XCameraProperties;
import rod.bailey.trafficatnsw.instrument.util.TestUtils;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by rodbailey on 20/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParseCamerasInstrumentedTest {

	private static final String JSON_FILE = "cameras.json";
	private static final String CAMERA_ID = "d2e386";
	private static XCameraCollection cameras;
	private static XCamera camera;
	private static String jsonString;

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	@Test
	public void setup00() throws IOException {
		Context appContext = InstrumentationRegistry.getTargetContext();
		jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, JSON_FILE);
	}

	@Test
	public void setup01() {
		cameras = XCameraCollection.Companion.parseCameraJson(jsonString);
		assertNotNull(cameras);
		assertEquals(112, cameras.getCameras().size());
	}

	@Test
	public void setup02() {
		camera = TestUtils.findCameraById(cameras.getCameras(), CAMERA_ID);
		assertNotNull(camera);
	}

	@Test
	public void testLatLng() {
		double loc[] = camera.getGeometry().getLatlng();
		assertNotNull(loc);
		assertEquals(loc[0], 151.10533, 0.0001);
		assertEquals(loc[1], -34.02977, 0.0001);
	}

	@Test
	public void testProperties() {
		XCameraProperties props = camera.getProperties();
		assertNotNull(props);
		assertEquals("SYD_SOUTH", props.getRegion());
		assertEquals("5 Ways (Miranda)", props.getTitle());
		assertEquals("5 ways at The Boulevarde looking west towards Sutherland.", props.getView());
		assertEquals("W", props.getDirection());
		assertEquals("http://www.rms.nsw.gov.au/trafficreports/cameras/camera_images/5ways.jpg", props.getImageURL());
	}
}
