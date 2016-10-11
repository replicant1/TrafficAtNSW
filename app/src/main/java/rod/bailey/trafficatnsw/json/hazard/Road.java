package rod.bailey.trafficatnsw.json.hazard;

import java.util.List;

/**
 * Created by rodbailey on 11/10/2016.
 */

public class Road {
    public String conditionTendency;

    public String crossStreet;

    public String delay;

    public String fullStreetAddress;

    public List<Lane> impactedLanes;

    public String locationQualifier;

    public String mainStreet;

    public int queueLength;

    public String region;

    public String secondLocation;

    public String suburb;

    public String trafficVolume;
}
