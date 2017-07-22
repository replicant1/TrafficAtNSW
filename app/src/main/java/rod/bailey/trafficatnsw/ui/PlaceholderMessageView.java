package rod.bailey.trafficatnsw.ui;

import android.content.Context;
import android.widget.TextView;

import static android.graphics.Color.*;
import static android.graphics.Typeface.*;
import static android.text.TextUtils.TruncateAt.END;
import static android.view.Gravity.*;
import static rod.bailey.trafficatnsw.util.DisplayUtils.*;

public class PlaceholderMessageView extends TextView {
	
	private static final int TEXT_SIZE_SP = 14;

	public PlaceholderMessageView(Context ctx) {
		super(ctx);
		
		setTextColor(GRAY);
		setTypeface(DEFAULT);
		setBackgroundColor(viewBackgroundColor());
		setGravity(CENTER);
		setTextSize(TEXT_SIZE_SP);
		setClickable(false);
		setEllipsize(END);
	}

	public PlaceholderMessageView(Context ctx, String message) {
		this(ctx);
		setMessage(message);
	}
	
	public void setMessage(String msg) {
		setText(msg);
	}

}
