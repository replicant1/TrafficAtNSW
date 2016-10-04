package rod.bailey.trafficatnsw.cameras.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import rod.bailey.trafficatnsw.util.DisplayUtils;
import rod.bailey.trafficatnsw.util.MLog;

public class CaptionedImageComponent extends ViewGroup {

	private static final int MARGIN_DP = 10;

	private static final int TEXT_SIZE_SP = 12;

	private static final String TAG = CaptionedImageComponent.class
			.getSimpleName();

	private static final double ASPECT_RATIO = 352.0 / 288.0;

	private Bitmap imageBitmap;
	private ImageView imageView;
	private TextView textView;
	private Bitmap frameBitmap;

	private ImageView frameView;

	public CaptionedImageComponent(Context ctx) {
		this(ctx, null, null, null);

	}

	public CaptionedImageComponent(Context ctx, Bitmap frameBitmap,
			Bitmap imageBitmap, String captionText) {
		super(ctx);

		assert ctx != null;

		imageView = new ImageView(ctx);
		textView = new TextView(ctx);
		frameView = new ImageView(ctx);

		setCaption(captionText);
		setFrameBitmap(frameBitmap);
		setImageBitmap(imageBitmap);

		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextSize(TEXT_SIZE_SP);
		textView.setTextColor(Color.WHITE);

		addView(imageView);
		addView(frameView);
		addView(textView);
	}

	public void setCaption(String value) {
		textView.setText(value == null ? "" : value);
		invalidate();
	}

	public void setFrameBitmap(Bitmap value) {
		this.frameBitmap = value;
		invalidate();
	}

	public void setImageBitmap(Bitmap value) {
		this.imageBitmap = value;
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		MLog.i(TAG,
				String.format(
						"left:%d, top:%d, right:%d, bottom:%d, imageBItmap:%s, frameBitmap:%s",
						left, top, right, bottom, imageBitmap, frameBitmap));

		int margin = DisplayUtils.dp2Px(getContext(), MARGIN_DP);

		int rawWidth = right - left;
		int rawHeight = bottom - top;

		int scaledImageWidth = rawWidth - (margin * 2);
		int scaledImageHeight = (int) (((double) scaledImageWidth) / ASPECT_RATIO);

		if (imageBitmap != null) {
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap,
					scaledImageWidth, scaledImageHeight, false);
			imageView.setImageBitmap(scaledBitmap);
		}

		if (frameBitmap != null) {
			Bitmap scaledFrameBitmap = Bitmap.createScaledBitmap(frameBitmap,
					scaledImageWidth, scaledImageHeight, false);
			frameView.setImageBitmap(scaledFrameBitmap);
		}

		// Work out what the top margin should be so that the combination of
		// camera image
		// and caption appears vertically centered.
		int captionHeightPx = DisplayUtils.sp2Px(getContext(), TEXT_SIZE_SP) * 3;
		int excessHeight = rawHeight - scaledImageHeight - captionHeightPx;

		int topMargin = excessHeight / 2;

		if (imageBitmap != null) {
			imageView.layout(margin, topMargin, margin + scaledImageWidth,
					topMargin + scaledImageHeight);
		}

		if (frameBitmap != null) {
			frameView.layout(margin, topMargin, margin + scaledImageWidth,
					topMargin + scaledImageHeight);
		}

		textView.layout(margin, topMargin + scaledImageHeight, margin
				+ scaledImageWidth, rawHeight - topMargin);
	}
}
