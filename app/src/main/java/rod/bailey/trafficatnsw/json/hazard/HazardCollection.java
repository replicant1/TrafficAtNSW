package rod.bailey.trafficatnsw.json.hazard;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

/**
 * Represents the entire JSON file containing multiple hazards.
 */
public class HazardCollection {
    public List<Hazard> features;
    public long lastPubished;

    public static HazardCollection parseJson(String jsonContents) {
        Gson gson = new Gson();
        return gson.fromJson(jsonContents, HazardCollection.class);
    }
}
