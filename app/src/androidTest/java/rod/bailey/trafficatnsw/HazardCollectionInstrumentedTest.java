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
import java.util.List;

import rod.bailey.trafficatnsw.json.hazard.Hazard;
import rod.bailey.trafficatnsw.json.hazard.HazardCollection;
import rod.bailey.trafficatnsw.json.hazard.HazardGeometry;
import rod.bailey.trafficatnsw.json.hazard.HazardProperties;
import rod.bailey.trafficatnsw.json.hazard.Lane;
import rod.bailey.trafficatnsw.json.hazard.Road;
import rod.bailey.trafficatnsw.util.AssetUtils;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class HazardCollectionInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    /**
     * Sample json file 09jul2013 contains 29 hazards.
     */
    @Test
    public void parse09jul2013Json440375() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String jsonString = AssetUtils.loadAssetFileAsString(appContext, "09jul2013.json");
        HazardCollection hazards = HazardCollection.parseJson(jsonString);

        assertNotNull(hazards);
        assertNotNull(hazards.features);
        assertEquals(29,hazards.features.size());

        // Examine the first hazard, which has ID 440375
        Hazard hazard = hazards.findHazardById(440375);
        assertNotNull(hazard);

        // Check its geometry
        HazardGeometry geometry = hazard.geometry;
        assertNotNull(geometry);

        List<Float> coords = geometry.coordinates;
        assertNotNull(coords);

        // Check latitude and longitude
        assertEquals(coords.get(0), 151.18589, 0.0001);
        assertEquals(coords.get(1), -33.86936, 0.0001);

        // Check simple (non-object, non-array) properties of hazard
        HazardProperties properties = hazard.properties;
        assertEquals("BREAKDOWN Car - ROZELLE Western Distributor approaching City West Link", properties.headline);
        assertNull(properties.webLinkUrl);
        assertTrue(properties.ended);
        assertFalse(properties.impactingNetwork);
        assertEquals(" ", properties.subCategoryB);
        assertFalse(properties.isInitialReport);
        assertEquals(1373321524927L, properties.created);
        assertFalse(properties.isMajor);
        assertEquals("Car", properties.subCategoryA);
        assertEquals(" ", properties.adviceA);
        assertEquals(" ", properties.adviceB);
        assertEquals(0L, properties.end);
        assertEquals("Breakdown", properties.mainCategory);
        assertEquals(1373322559566L, properties.lastUpdated);
        assertEquals(" ", properties.otherAdvice);
        assertNull(properties.webLinkName);
        assertEquals(0L, properties.start);
        assertEquals("BREAKDOWN Car", properties.displayName);

        // Check arrays
        assertTrue(properties.periods.isEmpty());
        assertTrue(properties.arrangementElements.isEmpty());
        assertEquals(1, properties.attendingGroups.size());
        assertEquals("Tow truck", properties.attendingGroups.get(0));
        assertEquals(1, properties.roads.size());

        // Check road
        Road road = properties.roads.get(0);
        assertEquals("", road.conditionTendency);
        assertEquals("City West Link", road.crossStreet);
        assertEquals("", road.delay);
        assertEquals("approaching", road.locationQualifier);
        assertEquals("Western Distributor", road.mainStreet);
        assertEquals(0, road.queueLength);
        assertEquals("SYD_MET", road.region);
        assertEquals(" ", road.secondLocation);
        assertEquals("ROZELLE", road.suburb);
        assertEquals("", road.trafficVolume);

        // Check impacted lane
        Lane lane = road.impactedLanes.get(0);
        assertEquals("Westbound", lane.affectedDirection);
        assertEquals("1", lane.closedLanes);
        assertEquals("Lane 2", lane.description);
        assertEquals("Lanes closed", lane.extent);
        assertEquals("2", lane.numberOfLanes);
        assertEquals(" ", lane.roadType);
    }

    @Test
    public void parse09jul2013Json440361()  throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String jsonString = AssetUtils.loadAssetFileAsString(appContext, "09jul2013.json");
        HazardCollection hazards = HazardCollection.parseJson(jsonString);

        // This test case examines hazard id 440361
        Hazard hazard = hazards.findHazardById(440361);

        // Check geometry
        HazardGeometry geometry = hazard.geometry;
        assertNotNull(geometry);
        List<Float> coords = geometry.coordinates;
        assertEquals(2, coords.size());
        assertEquals(coords.get(0), 147.59154, 0.0001);
        assertEquals(coords.get(1), -35.46987, 0.0001);

        // Check properties
        HazardProperties properties = hazard.properties;
        assertNotNull(properties.headline);
        assertTrue(properties.periods.isEmpty());
        assertNull(properties.webLinkUrl);
        assertFalse(properties.ended);
        assertFalse(properties.impactingNetwork);
        assertEquals(" ", properties.subCategoryB);
        assertFalse(properties.isInitialReport);
        assertEquals(1373305277059L, properties.created);
        assertFalse(properties.isMajor);
        assertEquals("Truck", properties.subCategoryA);
        assertEquals(" ", properties.adviceB);
        assertEquals("Exercise caution", properties.adviceA);
        assertEquals("Accident", properties.mainCategory);
        assertEquals(1373324947947L, properties.lastUpdated);
        assertEquals(" ", properties.otherAdvice);
        assertTrue(properties.arrangementElements.isEmpty());
        assertNull(properties.webLinkName);
        assertEquals(2, properties.attendingGroups.size());
        assertEquals("Emergency service(s)", properties.attendingGroups.get(0));
        assertEquals("RMS", properties.attendingGroups.get(1));
        assertEquals(0L, properties.start);
        assertEquals("ACCIDENT Truck", properties.displayName);

        // Check road
        Road road = properties.roads.get(0);
        assertEquals("", road.conditionTendency);
        assertEquals("Tumbarumba Road", road.crossStreet);
        assertEquals("", road.delay);
        assertEquals("between", road.locationQualifier);
        assertEquals("Hume Highway", road.mainStreet);
        assertEquals("REG_SOUTH", road.region);
        assertEquals("Little Billabong Road", road.secondLocation);
        assertEquals("Kyeamba Gap", road.suburb);
        assertEquals("", road.trafficVolume);

        // Check impacted lane
        Lane lane = road.impactedLanes.get(0);
        assertEquals("Northbound", lane.affectedDirection);
        assertEquals("1", lane.closedLanes);
        assertEquals("Lane 1", lane.description);
        assertEquals("Lanes closed", lane.extent);
        assertEquals("2", lane.numberOfLanes);
        assertEquals(" ", lane.roadType);
    }

}
