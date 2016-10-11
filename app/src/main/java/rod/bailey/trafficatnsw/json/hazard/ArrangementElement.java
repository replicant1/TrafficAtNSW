package rod.bailey.trafficatnsw.json.hazard;

/**
 * Created by rodbailey on 11/10/2016.
 */
public class ArrangementElement {
    public String html;

    public String title;

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        buf.append("title="+ title);
        buf.append(",html="+ html);
        return buf.toString();
    }
}
