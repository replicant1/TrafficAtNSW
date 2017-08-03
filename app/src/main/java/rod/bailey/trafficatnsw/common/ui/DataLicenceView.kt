package rod.bailey.trafficatnsw.common.ui

import android.content.Context
import android.widget.FrameLayout
import org.androidannotations.annotations.EViewGroup
import rod.bailey.trafficatnsw.R

/**
 * Footer that appears at bottom of several lists in the app that contains
 * text of the licencing conditions for the web site data.
 */
@EViewGroup(R.layout.list_item_data_licence)
open class DataLicenceView(ctx: Context) : FrameLayout(ctx)
