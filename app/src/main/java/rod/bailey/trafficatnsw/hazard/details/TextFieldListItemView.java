package rod.bailey.trafficatnsw.hazard.details;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.text.TextUtils.TruncateAt.END;
import static android.view.Gravity.*;
import static android.view.ViewGroup.LayoutParams.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class TextFieldListItemView extends LinearLayout {

	private static final int TEXT_SIZE_SP = 12;

	@SuppressWarnings("unused")
	private static final String TAG = TextFieldListItemView.class
			.getSimpleName();

	private final Context ctx;

	private TextView fieldNameTextView;

	private TextView fieldValueTextView;

	public TextFieldListItemView(Context ctx, String fieldName,
			String fieldValue) {
		super(ctx);

		this.ctx = ctx;

		fieldNameTextView = new TextView(ctx);
		fieldValueTextView = new TextView(ctx);

		// Field name
		fieldNameTextView.setText(fieldName);
		fieldNameTextView.setTextColor(BLACK);
		fieldNameTextView.setTypeface(DEFAULT);
		// Note that setTextSize() takes an argument in SP, not PX
		fieldNameTextView.setTextSize(TEXT_SIZE_SP);
		fieldNameTextView.setSingleLine();
		fieldNameTextView.setEllipsize(END);
		fieldNameTextView.setBackgroundColor(TRANSPARENT);

		LayoutParams nameLLP = new LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		fieldNameTextView.setLayoutParams(nameLLP);

		// Field value
		fieldValueTextView.setText(fieldValue);
		fieldValueTextView.setTextColor(DKGRAY);
		fieldValueTextView.setTypeface(DEFAULT);
		// Note that setTextSize() takes an argument in SP, not PX
		fieldValueTextView.setTextSize(TEXT_SIZE_SP);
		fieldValueTextView.setSingleLine();
		fieldValueTextView.setEllipsize(END);
		fieldValueTextView.setBackgroundColor(TRANSPARENT);

		// Field name aligned left, field value aligned right
		LayoutParams valueLLP = new LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		valueLLP.weight = 1;
		fieldValueTextView.setLayoutParams(valueLLP);
		fieldValueTextView.setGravity(RIGHT);

		setBackgroundColor(WHITE);
		int p = dp2Px(ctx, 5);
		setPadding(p,p,p,p);
		setOrientation(HORIZONTAL);
		
		addView(fieldNameTextView);
		addView(fieldValueTextView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getDisplaySizePx(ctx).x, dp2Px(ctx,30));
	}
}
