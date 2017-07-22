package rod.bailey.trafficatnsw.ui;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;

import rod.bailey.trafficatnsw.ui.DataLicenceView;
import rod.bailey.trafficatnsw.util.DisplayUtils;

public class FooterCancellingListView extends ListView {

	private boolean footerPresent;

	private DataLicenceView footerView;

	public FooterCancellingListView(Context ctx) {
		super(ctx);
		footerView = new DataLicenceView(ctx);
		setFooterDividersEnabled(false);
		setBackgroundColor(DisplayUtils.viewBackgroundColor());
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (adapter.getCount() == 0) {
			// Remove the footer from an empty list, as it looks silly
			// by itself.
			if (footerPresent) {
				footerPresent = false;
				removeFooterView(footerView);
			}
		} else {
			// Add the footer to list if it is not empty
			if (!footerPresent) {
				footerPresent = true;
				addFooterView(footerView);
			}
		}
		
		// Note for reasons unknown, footerview must be added *before* this
		// adapter is set, otherwise it doesn' appear.
		super.setAdapter(adapter);
	}
}
