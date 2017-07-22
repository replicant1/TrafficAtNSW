package rod.bailey.trafficatnsw.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

/** An item in the navigation bar that is not a heading, but can be tapped on */
public class NavigationListSubheadingView extends TextView {

	private static final int TEXT_SIZE_SP = 14;

	/**
	 * Constructs a NavigationListSubheadingView that appears in the nav bar and
	 * is just a piece of text
	 * 
	 * @param ctx
	 *            Context
	 * @param text
	 *            Text of the subheading
	 */
	public NavigationListSubheadingView(Context ctx, String text) {
		super(ctx);

		setBackgroundColor(Color.TRANSPARENT);
		setTypeface(Typeface.DEFAULT);
		// Note that setTextSize accepts an argument in SP, not pixels
		setTextSize(TEXT_SIZE_SP);
		setText(text);
		setPadding(dp2Px(ctx,8), dp2Px(ctx,5), dp2Px(ctx,10), dp2Px(ctx,10));
		setTextColor(Color.DKGRAY);
	}
}