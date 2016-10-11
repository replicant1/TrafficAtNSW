package rod.bailey.trafficatnsw.json.hazard;

import android.location.Location;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * GSON domain class - represents a HazardProperties (incident).
 */
public class HazardProperties implements Comparable<HazardProperties> {
    public String adviceA;

    public String adviceB;

    public List<ArrangementElement> arrangementElements;

    public List<String> attendingGroups;

    public long created;

    public String displayName;

    public long end;

    public boolean ended;

    public int hazardId;

    public String headline;

    public boolean impactingNetwork;

    public boolean isInitialReport;

    public boolean isMajor;

    public long lastUpdated;

    public Location latlng;

    public String mainCategory;

    public String otherAdvice;

    public List<Period> periods;

    public JSONObject properties;

    public String publicTransport;

    public List<Road> roads;

    public long start;

    public String subCategoryA;

    public String subCategoryB;

    public String webLinkName;

    public String webLinkUrl;

    /**
     * @return A negative integer if this < another. A positive integer if
     * this > another. 0 if this.equals() another,
     */
    @Override
    public int compareTo(HazardProperties another) {
        int result = 0;

        result = (int) (lastUpdated -  another.lastUpdated);

        return result;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        return buf.toString();
    }
}
