package rod.bailey.trafficatnsw.cameras.details;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.text.TextUtils.TruncateAt.END;
import static android.view.Gravity.*;
import static android.view.ViewGroup.LayoutParams.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class TrafficCameraTitleView extends LinearLayout {

	private static final int SUB_TITLE_TEXT_SIZE_SP = 12;
	private static final int TITLE_TEXT_SIZE_SP = 14;

	public TrafficCameraTitleView(Context ctx, String title, String subTitle) {
		super(ctx);

		// Title
		TextView titleTextView = new TextView(ctx);
		titleTextView.setText(title);
		titleTextView.setBackgroundColor(TRANSPARENT);
		titleTextView.setTypeface(DEFAULT);
		titleTextView.setTextSize(TITLE_TEXT_SIZE_SP);
		titleTextView.setTextColor(DKGRAY);
		titleTextView.setSingleLine();
		titleTextView.setEllipsize(END);

		LayoutParams titleTextViewLP = new LayoutParams(
				MATCH_PARENT, WRAP_CONTENT);
		titleTextViewLP.gravity = BOTTOM;

		// Subtitle
		TextView subTitleTextView = new TextView(ctx);
		subTitleTextView.setText(subTitle);
		subTitleTextView.setBackgroundColor(TRANSPARENT);
		subTitleTextView.setTypeface(DEFAULT);
		subTitleTextView.setTextSize(SUB_TITLE_TEXT_SIZE_SP);
		subTitleTextView.setTextColor(GRAY);
		subTitleTextView.setSingleLine();
		subTitleTextView.setEllipsize(END);

		// Title on top, subtitle underneath, padding all around
		LayoutParams subTitleTextViewLP = new LayoutParams(
				MATCH_PARENT, WRAP_CONTENT);
		subTitleTextViewLP.gravity = TOP;

		addView(titleTextView, titleTextViewLP);
		addView(subTitleTextView, subTitleTextViewLP);
		
		setOrientation(VERTICAL);
		setPadding(0, dp2Px(ctx,3), dp2Px(ctx,3), dp2Px(ctx,3));
	}
}
