package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import rod.bailey.trafficatnsw.R

class TextFieldListItemView(ctx: Context,
							fieldName: String,
							fieldValue: String) : LinearLayout(ctx) {

	init {
		val inflater: LayoutInflater = LayoutInflater.from(ctx)
		val content: View = inflater.inflate(R.layout.list_item_hazard_detail_text_field, this)
		val fieldNameTextView: AppCompatTextView =
			content.findViewById(R.id.tv_hazard_detail_field_name) as AppCompatTextView
		val fieldValueTextView: AppCompatTextView =
			content.findViewById(R.id.tv_hazard_detail_field_value) as AppCompatTextView

		fieldNameTextView.text = fieldName
		fieldValueTextView.text = fieldValue
	}
}
