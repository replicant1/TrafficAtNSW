package rod.bailey.trafficatnsw.instrument.camera;

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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.cameras.data.TrafficCamera;
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton;
import rod.bailey.trafficatnsw.cameras.filter.AdmitAnyTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitFavouritesTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitRegionalTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.AdmitSydneyTrafficCameraFilter;
import rod.bailey.trafficatnsw.cameras.filter.ITrafficCameraFilter;
import rod.bailey.trafficatnsw.hazard.data.XRegion;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TrafficCameraCacheSingletonInstrumentedTest {

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

	private TrafficCameraCacheSingleton cache;

	@Before
	public void setup() {
		cache = new TrafficCameraCacheSingleton();
		cache.init(mActivityRule.getActivity());
	}

	@Test
	public void testDefaultFilterReturnsAllCameras() {
		assertNotNull(cache.getFilter());
		assertTrue(cache.getFilter() instanceof AdmitAnyTrafficCameraFilter);
		assertEquals(24, cache.getCamerasForRegion(XRegion.SYD_MET).size());
	}

	@Test
	public void testNumCamerasPerRegion() {
		assertEquals(24, cache.getCamerasForRegion(XRegion.SYD_MET).size());
		assertEquals(20, cache.getCamerasForRegion(XRegion.SYD_NORTH).size());
		assertEquals(13, cache.getCamerasForRegion(XRegion.SYD_SOUTH).size());
		assertEquals(16, cache.getCamerasForRegion(XRegion.SYD_WEST).size());
		assertEquals(10, cache.getCamerasForRegion(XRegion.REG_NORTH).size());
		assertEquals(4, cache.getCamerasForRegion(XRegion.REG_SOUTH).size());
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
	public void testAdmitFavouritesFilter() {
		cache.setFilter(new AdmitFavouritesTrafficCameraFilter());
		List<TrafficCamera> cameras = cache.getCamerasForRegion(XRegion.SYD_MET);
		boolean favouriteSet = false;
		for (TrafficCamera camera : cameras) {
			if (!favouriteSet) {
				camera.setFavourite(true);
				favouriteSet = true;
			}
		}


		List<TrafficCamera> favouriteCameras = cache.getCamerasForRegion(XRegion.SYD_MET);
		assertNotNull(favouriteCameras);
		assertEquals(1, favouriteCameras.size());
	}

	@Test
	public void testGetCameraIgnoresFilter() {
		cache.setFilter(new ITrafficCameraFilter() {
			@Override
			public boolean admit(TrafficCamera camera) {
				return false;
			}
		});
		TrafficCamera camera = cache.getCamera(1);
		assertNotNull(camera);
		assertEquals(1, camera.getIndex());
		assertEquals(XRegion.SYD_MET, camera.getRegion());
	}

	@Test
	public void testMakingFavouriteSendsEventToCacheSingleton() {
		TrafficCamera camera = cache.getCamera(1);
		final AtomicBoolean eventReceived = new AtomicBoolean(false);
		cache.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				eventReceived.set(true);
			}
		});
		camera.setFavourite(true);
		assertTrue(eventReceived.get());
	}

}
