package rod.bailey.trafficatnsw.hazard.details.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import rod.bailey.trafficatnsw.R

@EViewGroup(R.layout.list_item_hazard_detail_text_field)
open class TextFieldListItemView(ctx: Context,
							val fieldName: String,
							val fieldValue: String) : LinearLayout(ctx) {
	@ViewById(R.id.tv_hazard_detail_field_name)
	@JvmField
	var fieldNameTextView: AppCompatTextView? = null

	@ViewById(R.id.tv_hazard_detail_field_value)
	@JvmField
	var fieldValueTextView: AppCompatTextView? = null

	@AfterViews
	fun afterViews() {
		fieldNameTextView?.text = fieldName
		fieldValueTextView?.text = fieldValue
	}
}
