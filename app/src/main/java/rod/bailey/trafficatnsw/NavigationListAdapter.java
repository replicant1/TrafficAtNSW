package rod.bailey.trafficatnsw;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.LinkedList;
import java.util.List;

public class NavigationListAdapter extends BaseAdapter implements ListAdapter {

	public class Item {
		private final String text;

		public Item(String text) {
			this.text = text;
		}
	}

	public class PrimaryItem extends Item {
		private final int drawableId;

		public PrimaryItem(String text, int drawableId) {
			super(text);
			this.drawableId = drawableId;
		}
	}

	public class SecondaryItem extends Item {
		public SecondaryItem(String text) {
			super(text);
		}
	}

	private final List<Item> items = new LinkedList<Item>();

	/**
	 * 
	 */
	private final MainActivity mainActivity;

	public NavigationListAdapter(MainActivity mainActivity) {
		this.mainActivity = mainActivity;

		items.add(new PrimaryItem("Incidents", R.drawable.ic_action_warning)); // [0]
		items.add(new SecondaryItem("Sydney")); // [1]
		items.add(new SecondaryItem("Regional NSW")); // [2]

		items.add(new PrimaryItem("Cameras", R.drawable.ic_action_camera)); // [3]
		items.add(new SecondaryItem("Favourites")); // [4]
		items.add(new SecondaryItem("Sydney")); // [5]
		items.add(new SecondaryItem("Regional NSW")); // [6]

		items.add(new PrimaryItem("Travel Times", R.drawable.ic_action_time)); // [7]
		items.add(new SecondaryItem("M1")); // [8]
		items.add(new SecondaryItem("M2"));// [9]
		items.add(new SecondaryItem("M4")); // [10]
		items.add(new SecondaryItem("M7")); // [11]
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		Item item = items.get(position);
		return (item instanceof SecondaryItem);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item item = items.get(position);

		View result = null;

		if (item instanceof PrimaryItem) {
			result = new NavigationListHeadingView(mainActivity, item.text,
					((PrimaryItem) item).drawableId, position > 0);
		} else {

			result = new NavigationListSubheadingView(mainActivity, item.text);
		}

		return result;
	}

}