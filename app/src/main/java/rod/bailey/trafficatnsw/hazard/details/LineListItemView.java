package rod.bailey.trafficatnsw.hazard.details;

import android.content.Context;
import android.widget.TextView;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.text.TextUtils.TruncateAt.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class LineListItemView extends TextView {

	private static final int TEXT_SIZE_SP = 12;
	@SuppressWarnings("unused")
	private static final String TAG = LineListItemView.class.getSimpleName();

	public LineListItemView(Context ctx, String text) {
		super(ctx);

		setText(text);
		setTextColor(BLACK);
		setTypeface(DEFAULT);
		// Note that setTextSize() takes an argument in SP, not PX
		setTextSize(TEXT_SIZE_SP);
		setSingleLine();
		setEllipsize(END);
		setBackgroundColor(TRANSPARENT);
		setBackgroundColor(WHITE);

		int p = dp2Px(ctx, 5);
		setPadding(p, p, p, p);
	}
}
