package rod.bailey.trafficatnsw.hazard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.hazard.details.HazardDetailsActivty;
import rod.bailey.trafficatnsw.json.hazard.Hazard;
import rod.bailey.trafficatnsw.json.hazard.Road;
import rod.bailey.trafficatnsw.util.DateUtils;
import rod.bailey.trafficatnsw.util.DisplayUtils;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.text.TextUtils.TruncateAt.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class HazardListItemView extends ViewGroup {

	private static final int DATE_VIEW_TEXT_SIZE_SP = 12;

	private static final int SUB_SUB_TITLE_TEXT_SIZE_SP = 12;

	private static final int SUBTITLE_VIEW_TEXT_SIZE_SP = 14;

	private static final int TITLE_VIEW_TEXT_SIZE_SP = 18;

	private final class ItemTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setBackgroundColor(Color.LTGRAY);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				setBackgroundColor(Color.WHITE);
				Intent hazardIntent = new Intent(
						HazardListItemView.this.getContext(),
						HazardDetailsActivty.class);
				hazardIntent.putExtra("hazardId",
						HazardListItemView.this.hazard.getHazardId());
				HazardListItemView.this.getContext()
						.startActivity(hazardIntent);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				setBackgroundColor(Color.WHITE);
			}
			return true;
		}
	}

	@SuppressWarnings("unused")
	private static final String TAG = HazardListItemView.class.getSimpleName();

	private final Context ctx;
	private final LinearLayout dateLinearLayout;
	private final TextView dateView;
	private final Hazard hazard;
	private final ImageView imageView;
	private final boolean showLastUpdatedDate;
	private final TextView subSubTitleView;
	private final TextView subTitleView;
	private final TextView titleView;

	public HazardListItemView(Context ctx, Hazard hazard,
			boolean showLastUpdatedDate, boolean clickable) {
		super(ctx);
		this.ctx = ctx;
		this.hazard = hazard;
		this.showLastUpdatedDate = showLastUpdatedDate;

		// Create child components
		imageView = new ImageView(ctx);
		titleView = new TextView(ctx);
		subTitleView = new TextView(ctx);
		subSubTitleView = new TextView(ctx);

		dateLinearLayout = new LinearLayout(ctx);
		dateView = new TextView(ctx);
		LinearLayout.LayoutParams dateViewLLP = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		dateView.setLayoutParams(dateViewLLP);
		dateLinearLayout.setBackgroundColor(Color.TRANSPARENT);
		dateLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		dateLinearLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		dateView.setBackgroundColor(Color.GREEN);
		dateLinearLayout.addView(dateView);

		addView(imageView);
		addView(titleView);
		addView(subTitleView);
		addView(subSubTitleView);

		if (showLastUpdatedDate) {
			addView(dateLinearLayout);
		}

		Road road = hazard.getRoads().get(0);

		imageView
				.setImageResource(hazard.isInitialReport() ? R.drawable.incident_initial_report
						: R.drawable.incident);
		titleView.setText(road.getSuburb());
		subTitleView.setText(road.getMainStreet());
		subSubTitleView.setText(hazard.getDisplayName());

		Date lu = hazard.getLastUpdated();
		if (lu != null) {
			String dateText = "";
			if (DateUtils.isYesterday(lu)) {
				dateText = "Yesterday";
			} else if (DateUtils.isToday(lu)) {
				dateText = new SimpleDateFormat("h:mm a").format(lu).toLowerCase();
			} else {
				dateText = new SimpleDateFormat("dd/MM/yyyy").format(lu);
			}

			if (dateText != null) {
				dateView.setText(dateText);
				
				
			}
		}

		titleView.setTextColor(BLACK);
		titleView.setTypeface(Typeface.DEFAULT_BOLD);
		titleView.setTextSize(TITLE_VIEW_TEXT_SIZE_SP);
		titleView.setSingleLine();
		titleView.setEllipsize(END);

		subTitleView.setTextColor(BLUE);
		subTitleView.setTypeface(DEFAULT);
		subTitleView.setTextSize(SUBTITLE_VIEW_TEXT_SIZE_SP);
		subTitleView.setSingleLine();
		subTitleView.setEllipsize(END);
		subTitleView.setBackgroundColor(TRANSPARENT);

		subSubTitleView.setTextColor(DKGRAY);
		subSubTitleView.setTypeface(DEFAULT);
		subSubTitleView.setTextSize(SUB_SUB_TITLE_TEXT_SIZE_SP);
		subSubTitleView.setSingleLine();
		subSubTitleView.setEllipsize(END);
		subSubTitleView.setBackgroundColor(TRANSPARENT);

		if (showLastUpdatedDate) {
			dateView.setTextColor(GRAY);
			dateView.setTypeface(DEFAULT);
			dateView.setTextSize(DATE_VIEW_TEXT_SIZE_SP);
			dateView.setSingleLine();
			dateView.setBackgroundColor(TRANSPARENT);
		}

		setBackgroundColor(WHITE);

		if (clickable) {
			setOnTouchListener(new ItemTouchListener());
		}
	}

	public Hazard getHazard() {
		return hazard;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {
			int hpadPx = dp2Px(ctx, 4);
			int vpadPx = dp2Px(ctx, 2);
			int availableWidth = right - left - (hpadPx * 2);
			int availableHeight = bottom - top - (vpadPx * 2);

			int imageHeight = dp2Px(ctx, 30);
			int imageWidth = dp2Px(ctx, 30);

			// Layout the icon. Work out the location in local coordinate space
			int iconTop = (availableHeight - imageHeight) / 2;
			int iconBottom = iconTop + imageHeight;
			imageView.layout(hpadPx, iconTop, imageWidth + hpadPx, iconBottom);
			
			// How wide will 'dateTExt' be when rendered.
			Rect dateTextRect = new Rect(); 
			TextPaint paint = DisplayUtils.createTextPaintWithTextSize(DisplayUtils.sp2Px(ctx, DATE_VIEW_TEXT_SIZE_SP));
			paint.getTextBounds(dateView.getText().toString(), 0, dateView.getText().length(), dateTextRect);
			int dateTextWidthPx = (int) ( (dateTextRect.right - dateTextRect.left) * 1.10);

			// Layout the titleView
			int titleLeft = imageWidth + (hpadPx * 3);
			int titleTop = vpadPx;
			int titleRight = hpadPx + availableWidth;
			int titleBottom = (int) (availableHeight * 0.4);
			titleView.layout(titleLeft, titleTop, titleRight - dateTextWidthPx, titleBottom);

			// Layout the subTitleView
			int subTitleLeft = titleLeft;
			int subTitleTop = titleBottom;
			int subTitleRight = titleRight;
			int subTitleBottom = (int) (availableHeight * 0.7);
			subTitleView.layout(subTitleLeft, subTitleTop, subTitleRight,
					subTitleBottom);
			
			// Layout the subSubTitleView
			int subSubTitleLeft = titleLeft;
			int subSubTitleTop = subTitleBottom;
			int subSubTitleRight = titleRight;
			int subSubTitleBottom = availableHeight - vpadPx;
			subSubTitleView.layout(subSubTitleLeft, subSubTitleTop,
					subSubTitleRight, subSubTitleBottom);

			// Layout the dateView
			if (showLastUpdatedDate) {
				int dateLeft = titleRight - availableWidth;
				int dateRight = titleRight;
				int dateTop = titleTop;
				int dateBottom = titleBottom;
				dateLinearLayout.layout(dateLeft, dateTop, dateRight,
						dateBottom);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getDisplaySizePx(ctx).x, dp2Px(ctx, 75));

		int dateWidthSpec = MeasureSpec.makeMeasureSpec(dp2Px(ctx, 200),
				MeasureSpec.AT_MOST);
		int dateHeightSpec = MeasureSpec.makeMeasureSpec(dp2Px(ctx, 75),
				MeasureSpec.AT_MOST);
		dateLinearLayout.measure(dateWidthSpec, dateHeightSpec);
	}

	public void setDate(Date date) {
		if (showLastUpdatedDate) {
			dateView.setText("12/03/2014");
			dateView.invalidate();
		}
	}

	public void setSubSubTitle(String title) {
		subSubTitleView.setText(title);
		subSubTitleView.invalidate();
	}

	public void setSubTitle(String title) {
		subTitleView.setText(title);
		subTitleView.invalidate();
	}

	public void setTitle(String title) {
		titleView.setText(title);
		titleView.invalidate();
	}
}
