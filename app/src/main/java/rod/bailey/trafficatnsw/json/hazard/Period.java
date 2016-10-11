package rod.bailey.trafficatnsw.json.hazard;

/**
 * Created by rodbailey on 11/10/2016.
 */
public class Period {
    public String closureType;
    public String direction;
    public String finishTime;
    public String fromDay;
    public String startTime;
    public String toDay;

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer( super.toString());

        buf.append("closeType="+ closureType);
        buf.append(",direction=" + direction);
        buf.append(",finishTime=" +finishTime);
        buf.append(",fromDay=" + fromDay);
        buf.append(",startTime="+ startTime);
        buf.append(",toDay="+ toDay);

        return buf.toString();
    }
}
