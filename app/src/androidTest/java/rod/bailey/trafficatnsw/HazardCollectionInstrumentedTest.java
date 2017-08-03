package rod.bailey.trafficatnsw;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import rod.bailey.trafficatnsw.app.MainActivity;
import rod.bailey.trafficatnsw.app.MainActivity_;
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
    public ActivityTestRule<MainActivity_> mActivityRule = new ActivityTestRule(MainActivity_.class);

    /**
     * Sample json file 09jul2013 contains 29 hazards.
     */
    @Test
    public void parse09jul2013Json440375() throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, "09jul2013.json");
        HazardCollection hazards = HazardCollection.Companion.parseJson(jsonString);

        assertNotNull(hazards);
        assertNotNull(hazards.getFeatures());
        assertEquals(29, hazards.getFeatures().size());

        // Examine the first hazard, which has ID 440375
        Hazard hazard = hazards.findHazardById(440375);
        assertNotNull(hazard);

        // Check its geometry
        HazardGeometry geometry = hazard.getGeometry();
        assertNotNull(geometry);

        List<Float> coords = geometry.getCoordinates();
        assertNotNull(coords);

        // Check latitude and longitude
        assertEquals(coords.get(0), 151.18589, 0.0001);
        assertEquals(coords.get(1), -33.86936, 0.0001);

        // Check simple (non-object, non-array) properties of hazard
        HazardProperties properties = hazard.getProperties();
        assertEquals("BREAKDOWN Car - ROZELLE Western Distributor approaching City West Link", properties.getHeadline());
        assertNull(properties.getWebLinkUrl());
        assertTrue(properties.getEnded());
        assertFalse(properties.getImpactingNetwork());
        assertEquals(" ", properties.getSubCategoryB());
        assertFalse(properties.isInitialReport());
        assertEquals(new Long(1373321524927L), (Long) properties.getCreated());
        assertFalse(properties.isMajor());
        assertEquals("Car", properties.getSubCategoryA());
        assertEquals(" ", properties.getAdviceA());
        assertEquals(" ", properties.getAdviceB());
        assertEquals(0L, properties.getEnd());
        assertEquals("Breakdown", properties.getMainCategory());
        assertEquals(new Long(1373322559566L), (Long)properties.getLastUpdated());
        assertEquals(" ", properties.getOtherAdvice());
        assertNull(properties.getWebLinkName());
        assertNull(properties.getStart());
        assertEquals("BREAKDOWN Car", properties.getDisplayName());

        // Check arrays
        assertTrue(properties.getPeriods().isEmpty());
        assertTrue(properties.getArrangementElements().isEmpty());
        assertEquals(1, properties.getAttendingGroups().size());
        assertEquals("Tow truck", properties.getAttendingGroups().get(0));
        assertEquals(1, properties.getRoads().size());

        // Check road
        Road road = properties.getRoads().get(0);
        assertEquals("", road.getConditionTendency());
        assertEquals("City West Link", road.getCrossStreet());
        assertEquals("", road.getDelay());
        assertEquals("approaching", road.getLocationQualifier());
        assertEquals("Western Distributor", road.getMainStreet());
        assertEquals(0, road.getQueueLength());
        assertEquals("SYD_MET", road.getRegion());
        assertEquals(" ", road.getSecondLocation());
        assertEquals("ROZELLE", road.getSuburb());
        assertEquals("", road.getTrafficVolume());

        // Check impacted lane
        Lane lane = road.getImpactedLanes().get(0);
        assertEquals("Westbound", lane.getAffectedDirection());
        assertEquals("1", lane.getClosedLanes());
        assertEquals("Lane 2", lane.getDescription());
        assertEquals("Lanes closed", lane.getExtent());
        assertEquals("2", lane.getNumberOfLanes());
        assertEquals(" ", lane.getRoadType());
    }

    @Test
    public void parse09jul2013Json440361()  throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String jsonString = AssetUtils.INSTANCE.loadAssetFileAsString(appContext, "09jul2013.json");
        HazardCollection hazards = HazardCollection.Companion.parseJson(jsonString);

        // This test case examines hazard id 440361
        Hazard hazard = hazards.findHazardById(440361);

        // Check geometry
        HazardGeometry geometry = hazard.getGeometry();
        assertNotNull(geometry);
        List<Float> coords = geometry.getCoordinates();
        assertEquals(2, coords.size());
        assertEquals(coords.get(0), 147.59154, 0.0001);
        assertEquals(coords.get(1), -35.46987, 0.0001);

        // Check properties
        HazardProperties properties = hazard.getProperties();
        assertNotNull(properties.getHeadline());
        assertTrue(properties.getPeriods().isEmpty());
        assertNull(properties.getWebLinkUrl());
        assertFalse(properties.getEnded());
        assertFalse(properties.getImpactingNetwork());
        assertEquals(" ", properties.getSubCategoryB());
        assertFalse(properties.isInitialReport());
        assertEquals(new Long(1373305277059L),(Long) properties.getCreated());
        assertFalse(properties.isMajor());
        assertEquals("Truck", properties.getSubCategoryA());
        assertEquals(" ", properties.getAdviceB());
        assertEquals("Exercise caution", properties.getAdviceA());
        assertEquals("Accident", properties.getMainCategory());
        assertEquals(new Long(1373324947947L), (Long) properties.getLastUpdated());
        assertEquals(" ", properties.getOtherAdvice());
        assertTrue(properties.getArrangementElements().isEmpty());
        assertNull(properties.getWebLinkName());
        assertEquals(2, properties.getAttendingGroups().size());
        assertEquals("Emergency service(s)", properties.getAttendingGroups().get(0));
        assertEquals("RMS", properties.getAttendingGroups().get(1));
        assertNull(properties.getStart());
        assertEquals("ACCIDENT Truck", properties.getDisplayName());

        // Check road
        Road road = properties.getRoads().get(0);
        assertEquals("", road.getConditionTendency());
        assertEquals("Tumbarumba Road", road.getCrossStreet());
        assertEquals("", road.getDelay());
        assertEquals("between", road.getLocationQualifier());
        assertEquals("Hume Highway", road.getMainStreet());
        assertEquals("REG_SOUTH", road.getRegion());
        assertEquals("Little Billabong Road", road.getSecondLocation());
        assertEquals("Kyeamba Gap", road.getSuburb());
        assertEquals("", road.getTrafficVolume());

        // Check impacted lane
        Lane lane = road.getImpactedLanes().get(0);
        assertEquals("Northbound", lane.getAffectedDirection());
        assertEquals("1", lane.getClosedLanes());
        assertEquals("Lane 1", lane.getDescription());
        assertEquals("Lanes closed", lane.getExtent());
        assertEquals("2", lane.getNumberOfLanes());
        assertEquals(" ", lane.getRoadType());
    }

}
