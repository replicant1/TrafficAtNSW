package rod.bailey.trafficatnsw.traveltime;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import rod.bailey.trafficatnsw.traveltime.common.TravelTime;
import rod.bailey.trafficatnsw.util.DisplayUtils;
import rod.bailey.trafficatnsw.util.MLog;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.view.Gravity.*;
import static android.view.ViewGroup.LayoutParams.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class TravelTimesListItemView extends LinearLayout {

	private static final int TEXT_SIZE_SP = 14;

	private final class ItemTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			MLog.i(TAG, "onTouch event for " + travelTime.getFromDisplayName() + " - " + travelTime.getToDisplayName() + ":" + event);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setBackgroundColor(Color.LTGRAY);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				setBackgroundColor(Color.WHITE);
				TravelTimesListItemView ttView = (TravelTimesListItemView) view;
				TravelTime travelTime = ttView.getTravelTime();

				// Clicking on a TT row should toggle it's 'includedInTotal' status.
				// But neither TOTAL rows or any row that is currently inactive can
				// be toggled.
				if (travelTime.isActive() && (!travelTime.isTotal())) {
					travelTime.setIncludedInTotal(!travelTime.isIncludedInTotal());
					updateAppearancePerExclusionState();
				}
			}
			else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				setBackgroundColor(Color.WHITE);
			}

			return true;
		}
	}

	@SuppressWarnings("unused")
	private static final String TAG = TravelTimesListItemView.class
			.getSimpleName();

	private final Context ctx;
	private final TextView leftColView;
	private final TextView rightColView;
	private TravelTime travelTime;

	public TravelTimesListItemView(Context ctx, TravelTime travelTime) {
		super(ctx);

		this.ctx = ctx;
		this.travelTime = travelTime;
		leftColView = new TextView(ctx);
		rightColView = new TextView(ctx);

		addView(leftColView);
		addView(rightColView);

		int m = DisplayUtils.dp2Px(ctx, 5);

		// Left column contains a string of form "<from> - <to>"
		leftColView.setSingleLine();
		leftColView.setBackgroundColor(TRANSPARENT);
		leftColView.setTextSize(TEXT_SIZE_SP);
		leftColView.invalidate();
		leftColView.setPadding(m, m, 0, m);

		LayoutParams leftLLP = new LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		leftColView.setLayoutParams(leftLLP);

		// Right column contains a string of form "<x> mins"
		rightColView.setSingleLine();
		rightColView.setBackgroundColor(TRANSPARENT);
		rightColView.setTextSize(TEXT_SIZE_SP);
		rightColView.invalidate();
		rightColView.setPadding(0, m, m, m);

		LayoutParams rightLLP = new LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		rightLLP.weight = 1;
		rightLLP.gravity = RIGHT;
		rightColView.setLayoutParams(rightLLP);
		rightColView.setGravity(RIGHT);

		setBackgroundColor(Color.WHITE);
		setOnTouchListener(new ItemTouchListener());
		convert(travelTime);
		setOrientation(HORIZONTAL);
		updateAppearancePerExclusionState();
		
		// TOTAL rows are not enabled - all other rows ARE enabled 
		setEnabled(!travelTime.isTotal());
	}

	private void convert(TravelTime travelTime) {
		this.travelTime = travelTime;
		if (travelTime.isTotal()) {
			leftColView.setTypeface(DEFAULT_BOLD);
			leftColView.setText("Total");
		} else {
			leftColView.setTypeface(DEFAULT);
			leftColView.setText(String.format("%s - %s",
					travelTime.getFromDisplayName(),
					travelTime.getToDisplayName()));
		}
		leftColView.invalidate();

		if (travelTime.isTotal()) {
			rightColView.setTypeface(DEFAULT_BOLD);
		} else {
			rightColView.setTypeface(DEFAULT);
		}

		rightColView.setText(String.format("%d mins",
				travelTime.getTravelTimeMinutes()));
		rightColView.invalidate();
	}

	public TravelTime getTravelTime() {
		return travelTime;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(DisplayUtils.getDisplaySizePx(ctx).x,
				dp2Px(ctx, 35));
	}

	public void updateAppearancePerExclusionState() {
		if (!travelTime.isTotal()) {
			if ((!travelTime.isActive()) || (!travelTime.isIncludedInTotal())) {
				leftColView.setTextColor(LTGRAY);
				rightColView.setTextColor(LTGRAY);
			} else {
				leftColView.setTextColor(BLACK);
				rightColView.setTextColor(BLACK);
			}
		}
		leftColView.invalidate();
		rightColView.invalidate();
	}
}
