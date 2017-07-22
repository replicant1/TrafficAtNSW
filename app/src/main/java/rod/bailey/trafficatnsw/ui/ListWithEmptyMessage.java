package rod.bailey.trafficatnsw.ui;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import rod.bailey.trafficatnsw.ui.predicate.IEmptyMessagePredicate;

public class ListWithEmptyMessage extends FrameLayout {
	
	@SuppressWarnings("unused")
	private static final String TAG = ListWithEmptyMessage.class.getSimpleName();
	
	private final FooterCancellingListView listView;
	private PlaceholderMessageView messageView;
	private final IEmptyMessagePredicate predicate;
	
	public ListWithEmptyMessage(Context ctx, String emptyMessage, IEmptyMessagePredicate predicate) {
		super(ctx);
		listView = new FooterCancellingListView(ctx);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEnabled(true);
		
		messageView = new PlaceholderMessageView(ctx, emptyMessage);
		
		this.predicate = predicate;
		
		addView(listView);
		addView(messageView);
	}
	
	public void setEmptyMessage(String message) {
		messageView.setMessage(message);
	}

	public void setAdapter(ListAdapter adapter) {
		if (predicate.showEmptyMessage(adapter)) {
			bringChildToFront(messageView);
		} else {
			bringChildToFront(listView);
		}
		listView.setAdapter(adapter);
	}
	

}
