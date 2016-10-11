package rod.bailey.trafficatnsw.cameras;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import rod.bailey.trafficatnsw.json.hazard.XRegion;


public class TrafficCamera implements Comparable<TrafficCamera> {
	
	public static final String PROPERTY_FAVOURITE = "favourite";
	
	private String suburb;
	private String street;
	private String description;
	private String url;
	private XRegion region;
	private boolean favourite;
	private int index;
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public TrafficCamera(String street, String suburb, String description, String url, XRegion region, int index) {
		assert street != null;
		assert suburb != null;
		assert description != null;
		assert url != null;
		assert region != null;
		assert index >= 0;
		
		this.street = street;
		this.suburb = suburb;
		this.description = description;
		this.url = url;
		this.region = region;
		this.index = index;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	private void fireFavouritePropertyChangeEvent() {
		support.firePropertyChange(PROPERTY_FAVOURITE, null, favourite);
	}
	
	public void setFavourite(boolean value) {
		favourite = value;
		fireFavouritePropertyChangeEvent();
	}
	
	public void setFavouriteSilently(boolean value) {
		favourite = value;
	}

	public String getSuburb() {
		return suburb;
	}

	public String getStreet() {
		return street;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public XRegion getRegion() {
		return region;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int compareTo(TrafficCamera another) {
		int result = 0;
		
		if (another != null) {
			result = street.compareTo(another.street);
			if (result == 0) {
				result = suburb.compareTo(another.suburb);
			}
		}
		
		return result;
	}

}
