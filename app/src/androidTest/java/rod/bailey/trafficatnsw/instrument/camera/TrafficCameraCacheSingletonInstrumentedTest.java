package rod.bailey.trafficatnsw.instrument.camera;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.cameras.data.XCamera;
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton;
import rod.bailey.trafficatnsw.cameras.filter.AdmitAnyTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitFavouritesTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitRegionalTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitSydneyTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter;
import rod.bailey.trafficatnsw.hazard.data.XRegion;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TrafficCameraCacheSingletonInstrumentedTest {

	public static final String FIRST_CAMERA = "d2e386";
	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private TrafficCameraCacheSingleton cache;

	private static final String JSON_FILE = "cameras.json";

	@Before
	public void setup() throws IOException {
		// Cache must be in target context so it has permission to write to shared prefs
		cache = new TrafficCameraCacheSingleton(InstrumentationRegistry.getTargetContext());

		// Use target context as cameras.json is NOT in the androidTest dir
		String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(
				InstrumentationRegistry.getTargetContext(), JSON_FILE);
		cache.init(jsonString);
	}

	@Test
	public void testIsPrimed() {
		assertTrue(cache.isPrimed());
	}

	@Test
	public void testFavouritePersistsAcrossCacheInstances() throws IOException {
		XCamera camera = cache.getUnfilteredCamera(FIRST_CAMERA);
		camera.setFavourite(true);

		setup();
		XCamera reloadedCamera = cache.getUnfilteredCamera(FIRST_CAMERA);
		assertTrue(reloadedCamera.getFavourite());
	}

	@Test
	public void testDefaultFilterReturnsAllCameras() {
		assertNotNull(cache.getFilter());
		assertTrue(cache.getFilter() instanceof AdmitAnyTrafficCameraFilter);
		assertEquals(33, cache.getCamerasForRegion(XRegion.SYD_MET).size());
	}

	@Test
	public void testNumCamerasPerRegion() {
		assertEquals(33, cache.getCamerasForRegion(XRegion.SYD_MET).size());
		assertEquals(22, cache.getCamerasForRegion(XRegion.SYD_NORTH).size());
		assertEquals(16, cache.getCamerasForRegion(XRegion.SYD_SOUTH).size());
		assertEquals(16, cache.getCamerasForRegion(XRegion.SYD_WEST).size());
		assertEquals(16, cache.getCamerasForRegion(XRegion.REG_NORTH).size());
		assertEquals(9, cache.getCamerasForRegion(XRegion.REG_SOUTH).size());
		assertEquals(0, cache.getCamerasForRegion(XRegion.REG_WEST).size());
	}

	@Test
	public void testAdmitRegionalFilter() {
		cache.setFilter(new AdmitRegionalTrafficCameraFilter());
		assertTrue(cache.getCamerasForRegion(XRegion.SYD_MET).isEmpty());
		assertTrue(cache.getCamerasForRegion(XRegion.SYD_NORTH).isEmpty());
		assertTrue(cache.getCamerasForRegion(XRegion.SYD_SOUTH).isEmpty());
		assertTrue(cache.getCamerasForRegion(XRegion.SYD_WEST).isEmpty());
		assertFalse(cache.getCamerasForRegion(XRegion.REG_NORTH).isEmpty());
		assertFalse(cache.getCamerasForRegion(XRegion.REG_SOUTH).isEmpty());
	}

	@Test
	public void testAdmitSydneyFilter() {
		cache.setFilter(new AdmitSydneyTrafficCameraFilter());
		assertFalse(cache.getCamerasForRegion(XRegion.SYD_MET).isEmpty());
		assertFalse(cache.getCamerasForRegion(XRegion.SYD_NORTH).isEmpty());
		assertFalse(cache.getCamerasForRegion(XRegion.SYD_SOUTH).isEmpty());
		assertFalse(cache.getCamerasForRegion(XRegion.SYD_WEST).isEmpty());
		assertTrue(cache.getCamerasForRegion(XRegion.REG_NORTH).isEmpty());
		assertTrue(cache.getCamerasForRegion(XRegion.REG_SOUTH).isEmpty());
	}

	@Test
	public void testSaveFavourites() {
		XCamera camera = cache.getUnfilteredCamera(FIRST_CAMERA);
		camera.setFavourite(true);
		Set<String> favouriteIds = cache.saveFavourites();
		assertNotNull(favouriteIds);
		assertTrue(favouriteIds.contains(FIRST_CAMERA));
	}

	@Test
	public void testLoadFavourites() {
		XCamera camera = cache.getUnfilteredCamera(FIRST_CAMERA);
		camera.setFavourite(true);
		Set<String> loaded = cache.loadFavourites();
		assertNotNull(loaded);
		assertFalse(loaded.isEmpty());
		assertTrue(loaded.contains(FIRST_CAMERA));
	}

	@Test
	public void testAdmitFavouritesFilter() {
		List<XCamera> cameras = cache.getCamerasForRegion(XRegion.SYD_MET);
		XCamera targetCamera = cameras.get(0);
		targetCamera.setFavourite(true);
		cache.setFilter(new AdmitFavouritesTrafficCameraFilter());
		List<XCamera> favouriteCameras = cache.getCamerasForRegion(XRegion.SYD_MET);
		assertTrue(favouriteCameras.contains(targetCamera));
	}

	@Test
	public void testGetCameraIgnoresFilter() {
		cache.setFilter(new ITrafficCameraFilter() {
			@Override
			public boolean admit(XCamera camera) {
				return false;
			}
		});
		XCamera camera = cache.getUnfilteredCamera(FIRST_CAMERA);
		assertNotNull(camera);
		assertEquals(FIRST_CAMERA, camera.getId());
		assertEquals("SYD_SOUTH", camera.getProperties().getRegion());
	}

	@Test
	public void testMakingFavouriteSendsEventToCacheSingleton() {
		XCamera camera = cache.getUnfilteredCamera(FIRST_CAMERA);
		final AtomicBoolean eventReceived = new AtomicBoolean(false);
		cache.setPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				eventReceived.set(true);
			}
		});
		camera.setFavourite(true);
		assertTrue(eventReceived.get());
	}

}
