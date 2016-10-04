package rod.bailey.trafficatnsw.util;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.view.ViewGroup.LayoutParams.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class ListHeadingView extends LinearLayout {
	private static final int TEXT_SIZE_SP = 14;

	public ListHeadingView(Context ctx, String headingText, boolean addDivider) {
		super(ctx);

		assert ctx != null;
		assert headingText != null;

		setOrientation(VERTICAL);

		// Text
		TextView textView = new TextView(ctx);
		textView.setText(headingText);
		textView.setSingleLine();
		textView.setBackgroundColor(TRANSPARENT);
		textView.setTextColor(DKGRAY);
		// Note that the text size is in SP and so we don't need to
		// call DisplayUtils.sp2Px
		textView.setTextSize(TEXT_SIZE_SP);
		textView.setAllCaps(true);
		textView.setTypeface(DEFAULT);
		setFocusable(false);

		LayoutParams textLP = new LayoutParams(
				MATCH_PARENT, WRAP_CONTENT);
		textView.setLayoutParams(textLP);

		addView(textView);

		if (addDivider) {
			// Divider
			View divider = new View(ctx);
			divider.setBackgroundColor(LTGRAY);

			LayoutParams dividerLP = new LayoutParams(
					MATCH_PARENT, sp2Px(ctx,2));
			divider.setLayoutParams(dividerLP);

			addView(divider);
		}

		int smallPx = dp2Px(ctx, 5);
		int bigPx = dp2Px(ctx, 25);
		setPadding(smallPx, bigPx, smallPx, smallPx);
	}
}
