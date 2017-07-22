package rod.bailey.trafficatnsw.ui;

import android.content.Context;
import android.widget.TextView;

import static android.graphics.Color.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class DataLicenceView extends TextView {

	private static final int TEXT_SIZE_SP = 10;
	private static final String TEXT = "Based on data from Live Traffic NSW. The accuracy or suitability of the data "
			+ "is not verified and it is provided on an 'as is' basis.";

	public DataLicenceView(Context ctx) {
		super(ctx);
		setText(TEXT);
		setBackgroundColor(TRANSPARENT);
		setTextColor(GRAY);
		// Note that setTextSize takes an argument in SP not PX
		setTextSize(TEXT_SIZE_SP);

		int smallPx = dp2Px(ctx, 5);
		int bigPx = dp2Px(ctx, 10);
		setPadding(smallPx, bigPx, smallPx, bigPx);
	}
}
