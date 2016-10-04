package rod.bailey.trafficatnsw.cameras;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import rod.bailey.trafficatnsw.R;
import rod.bailey.trafficatnsw.cameras.details.TrafficCameraImageActivity;
import rod.bailey.trafficatnsw.util.DisplayUtils;

import static android.graphics.Color.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class TrafficCameraListItemView extends ViewGroup {

	private static final int SUB_TITLE_TEXT_SIZE_SP = 14;

	private static final int TITLE_TEXT_SIZE_SP = 16;

	private final class ItemTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				setBackgroundColor(Color.LTGRAY);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				setBackgroundColor(Color.WHITE);

				Intent imageIntent = new Intent(ctx,
						TrafficCameraImageActivity.class);
				imageIntent
						.putExtra("index", String.valueOf(camera.getIndex()));
				imageIntent.putExtra("street", camera.getStreet());
				imageIntent.putExtra("suburb", camera.getSuburb());
				imageIntent.putExtra("description", camera.getDescription());
				imageIntent.putExtra("url", camera.getUrl());
				imageIntent.putExtra("favourite",
						String.valueOf(camera.isFavourite()));

				// Change to startActivityForResult
				ctx.startActivity(imageIntent);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				setBackgroundColor(Color.WHITE);
			}
			return true;
		}
	}

	private static final String TAG = TrafficCameraListItemView.class
			.getSimpleName();

	private final TrafficCamera camera;
	private final Context ctx;
	private boolean favourite;
	private final ImageView imageView;
	private final TextView subtitleTextView;
	private final TextView titleTextView;

	public TrafficCameraListItemView(Context ctx, TrafficCamera camera,
			boolean favourite) {
		super(ctx);
		this.ctx = ctx;
		this.camera = camera;
		this.favourite = favourite;

		// create child components
		imageView = new ImageView(ctx);
		titleTextView = new TextView(ctx);
		subtitleTextView = new TextView(ctx);

		addView(imageView);
		addView(titleTextView);
		addView(subtitleTextView);

		titleTextView.setText(camera.getStreet());
		titleTextView.setSingleLine();
		titleTextView.setBackgroundColor(TRANSPARENT);
		titleTextView.setTextSize(TITLE_TEXT_SIZE_SP);
		titleTextView.setTextColor(BLACK);
		titleTextView.invalidate();

		subtitleTextView.setSingleLine();
		subtitleTextView.setBackgroundColor(TRANSPARENT);
		subtitleTextView.invalidate();
		subtitleTextView.setText(camera.getSuburb());
		subtitleTextView.setTextColor(DKGRAY);
		subtitleTextView.setTextSize(SUB_TITLE_TEXT_SIZE_SP);

		setFavourite(favourite);
		imageView.invalidate();

		setBackgroundColor(WHITE);
		setOnTouchListener(new ItemTouchListener());
	}

	/**
	 * Assign a size and position to each child view. Call layout on each child.
	 * all children within this layout.
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {
			int hpadPx = dp2Px(ctx, 6);
			
			int availableWidth = right - left;
			int availableHeight = bottom - top;

			int imageHeight = dp2Px(ctx, 30);
			int imageWidth = dp2Px(ctx, 30);

			// Layout the icon. Work out the location in our coordinate space.
			int iconTop = (availableHeight - imageHeight) / 2;
			int iconBottom = iconTop + imageHeight;
			imageView.layout(hpadPx, iconTop, imageWidth, iconBottom);

			// Layout the TextView containing the title
			int titleLeft = 2 * hpadPx + imageWidth;
			int titleBottom = availableHeight / 2;
			titleTextView.layout(titleLeft, 0, availableWidth, titleBottom);

			// Layout the TextView containing the subtitle
			int subtitleLeft = titleLeft;
			subtitleTextView.layout(subtitleLeft, titleBottom, availableWidth,
					availableHeight);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(DisplayUtils.getDisplaySizePx(ctx).x,
				DisplayUtils.dp2Px(ctx, 50));
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
		imageView.setImageResource(favourite ? R.drawable.ic_action_important
				: R.drawable.ic_action_not_important);
		invalidate();
	}
}
