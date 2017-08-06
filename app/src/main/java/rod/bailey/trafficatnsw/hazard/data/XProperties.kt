package rod.bailey.trafficatnsw.hazard.data

import android.location.Location
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.*

/**
 * Created by rodbailey on 6/8/17.
 */
data class XProperties (
	@SerializedName("adviceA")
	val adviceA: String?,

	@SerializedName("adviceB")
	val adviceB: String?,

	@SerializedName("arrangedElements")
	val arrangementElements: LinkedList<XArrangementElement>,

	@SerializedName("attendingGroups")
	val attendingGroups: LinkedList<String>,

	@SerializedName("created")
	val created: Date?,

	@SerializedName("displayName")
	val displayName: String?,

	@SerializedName("end")
	val end: Date?,

	@SerializedName("isEnded")
	val isEnded: Boolean?,

	@SerializedName("headline")
	val headline: String?,

	@SerializedName("isImpacingNetwork")
	val isImpactingNetwork: Boolean?,

	@SerializedName("isInitialReport")
	val isInitialReport: Boolean?,

	@SerializedName("isMajor")
	val isMajor: Boolean?,

	@SerializedName("lastUpdated")
	val lastUpdated: Date?,

	@SerializedName("latlng")
	val latlng: Location?,

	@SerializedName("mainCategory")
	val mainCategory: String?,

	@SerializedName("otherAdvice")
	val otherAdvice: String?,

	@SerializedName("periods")
	val periods: LinkedList<XPeriod>,

	@SerializedName("properties")
	val properties: JSONObject?,

	@SerializedName("publicTransport")
	val publicTransport: String?,

	@SerializedName("roads")
	val roads: LinkedList<XRoad>,

	@SerializedName("start")
	val start: Date?,

	@SerializedName("subCategoryA")
	val subCategoryA: String?,

	@SerializedName("subCategoryB")
	val subCategoryB: String?,

	@SerializedName("webLinkName")
	val webLinkName: String?,

	@SerializedName("webLinkUrl")
	val webLinkUrl: String?)