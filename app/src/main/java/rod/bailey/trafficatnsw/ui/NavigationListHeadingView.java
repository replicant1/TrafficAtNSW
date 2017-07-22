package rod.bailey.trafficatnsw.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class NavigationListHeadingView extends RelativeLayout {

	/** Size of text in heading in SP units */
	private static final int TEXT_SIZE_SP = 14;

	/** Icon to the left of the heading */
	private final ImageView imageView;

	/** Text to the right of the icon */
	private final TextView textView;

	/**
	 * Constructs a NavigationListHeadingView. A single line heading with an
	 * icon on the left, text ontheright and a horizontal underline.
	 * 
	 * @param ctx
	 *            Context
	 * @param text
	 *            Text of heading
	 * @param resId
	 *            Id of the icon
	 * @param addTopPadding
	 *            If true a large gap appears above this heading, otherwise,
	 *            only a small gap appears.
	 */
	public NavigationListHeadingView(Context ctx, String text, int resId,
			boolean addTopPadding) {
		super(ctx);

		LinearLayout bothRows = new LinearLayout(ctx);
		bothRows.setOrientation(LinearLayout.VERTICAL);

		LinearLayout topRow = new LinearLayout(ctx);
		topRow.setGravity(Gravity.CENTER_VERTICAL);

		// Icon
		imageView = new ImageView(ctx);
		imageView.setImageResource(resId);
		topRow.addView(imageView);

		// Text
		textView = new TextView(ctx);
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTypeface(Typeface.DEFAULT_BOLD);
		// Note that text size is already in SP so we don't have to
		// call DisplayUtils.sp2Px
		textView.setTextSize(TEXT_SIZE_SP);
		textView.setText(text);
		textView.setAllCaps(true);
		textView.setSingleLine();
		textView.setPadding(dp2Px(ctx, 4), dp2Px(ctx, 12), 0, 4);
		textView.setTextColor(Color.DKGRAY);
		topRow.addView(textView);

		// Divider
		View divider = new View(ctx);
		divider.setBackgroundColor(Color.LTGRAY);

		LayoutParams dividerRLP = new LayoutParams(
				LayoutParams.MATCH_PARENT, dp2Px(ctx, 2));
		divider.setLayoutParams(dividerRLP);

		bothRows.addView(topRow);
		bothRows.addView(divider);

		addView(bothRows);

		setPadding(dp2Px(ctx, 4),
				addTopPadding ? dp2Px(ctx, 10) : dp2Px(ctx, 5),
				dp2Px(ctx, 10), dp2Px(ctx, 10));
	}
}