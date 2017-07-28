package rod.bailey.trafficatnsw.hazard.details

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.hazard.details.cellrec.*
import rod.bailey.trafficatnsw.hazard.details.view.HtmlListItemView
import rod.bailey.trafficatnsw.hazard.details.view.LineListItemView
import rod.bailey.trafficatnsw.hazard.details.view.TextFieldListItemView
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.json.hazard.XLane
import rod.bailey.trafficatnsw.ui.view.HazardListItemView
import rod.bailey.trafficatnsw.ui.view.ListHeadingView
import rod.bailey.trafficatnsw.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class HazardDetailsListAdapter(private val ctx: Context, private val hazard: XHazard) : BaseAdapter(), ListAdapter {
	private val cellRecs: MutableList<CellRec> = LinkedList<CellRec>()

	init {
		// Title cell (always appears)
		addTitleCellRec()
		addHeadingCellRec(ctx.getString(R.string.hazard_details_heading_times))
		addCreateAndLastUpdatedCellRecs()
		addDurationCellRec()
		addHeadingCellRec(ctx.getString(R.string.hazard_details_heading_general))
		addAttendingCellRec()
		addRtaAdviceCellRec()
		addQueueLengthCellRec()
		addClosureCellRecs()
		addLaneCellRecs()
		addTrafficVolumeAndDelayCellRec()

		// Cells containing HTML
		addPublicTransportCellRec()
		addArrangementElementCellRecs()
		addOtherAdviceCellRec()

		// De-duplication of headings
		removeFirstOfConsecutiveHeadings()
	}

	override fun isEnabled(position: Int): Boolean {
		return false
	}

	private fun addArrangementElementCellRecs() {
		if (!isEmptyList(hazard.arrangementElements)) {
			for (element in hazard.arrangementElements) {
				if (element.title != null) {
					addHeadingCellRec(element.title)
				}
				if (element.html != null) {
					addHtmlFieldCellRec(element.html)
				}
			}
		}
	}

	private fun addAttendingCellRec() {
		if (!isEmptyList(hazard.attendingGroups)) {
			val numAttendingGroups = hazard.attendingGroups.size
			val str = StringBuffer()

			for (i in 0..numAttendingGroups - 1) {
				val group = hazard.attendingGroups[i]

				if (!isEmptyStr(group)) {
					str.append(group)
				}

				if (i < numAttendingGroups - 1) {
					str.append(", ")
				}
			} // for i

			val strstr = str.toString()
			if (!isEmptyStr(strstr)) {
				val cellrec = TextFieldCellRec(ctx.getString(R.string.hazard_details_field_label_attending), strstr)
				cellRecs.add(cellrec)
			}
		}
	}

	private fun addClosureCellRecs() {
		for (period in hazard.periods) {
			if ((XHazard.TOKEN_ROAD_CLOSURE == period.closureType) ||
				(XHazard.TOKEN_LANE_CLOSURE == period.closureType)) {
				val str = StringBuffer()

				if (!isEmptyStr(period.toDay)) {
					str.append(ctx.getString(R.string.hazard_details_closure_from_date_to_date),
							   period.fromDay, period.toDay)
				} else {
					str.append(period.fromDay)
				}

				if (!isEmptyStr(period.startTime)) {
					str.append(" ")
					if (!isEmptyStr(period.finishTime)) {
						str.append(ctx.getString(R.string.hazard_details_closure_from_time_to_time),
								   period.startTime, period.finishTime)
					}
				}

				if (!isEmptyStr(period.direction)) {
					str.append(ctx.getString(R.string.hazard_details_closure_direction_affected), period.direction)
					str.append(" ")
				}

				val fieldValue = str.toString()
				var fieldName: String =
					if (XHazard.TOKEN_ROAD_CLOSURE == period.closureType) {
						ctx.getString(R.string.hazard_details_field_label_roads_closed)
					} else {
						ctx.getString(R.string.hazard_details_field_label_lanes_closed)
					}
				val cellRec = TextFieldCellRec(fieldName, fieldValue)
				cellRecs.add(cellRec)
			}
		}
	}

	private fun addCreateAndLastUpdatedCellRecs() {
		// First CellRec is for "Created" date/time
		if (hazard.created != null) {
			val createdStr = DateUtils.relativeDateAndTime(
				hazard.created, true)
			val createdCellRec = TextFieldCellRec(ctx.getString(R.string.hazard_details_field_label_when_started),
												  createdStr)
			cellRecs.add(createdCellRec)
		}

		// Second CellRec is for "Last updated" date/time. But we only create a
		// 'last updated' cellrec if it will show a different date to the
		// 'created' date.
		if ((!isLastUpdatedSameAsCreated) && (hazard.lastUpdated != null)) {
			val lastUpdatedStr = DateUtils.relativeDateAndTime(
				hazard.lastUpdated, true)
			val lastUpdatedCellRec = TextFieldCellRec(
				ctx.getString(R.string.hazard_details_field_label_when_last_checked), lastUpdatedStr)
			cellRecs.add(lastUpdatedCellRec)
		}
	}

	private fun addDurationCellRec() {
		if (hazard.start != null && hazard.end != null) {
			val formatString = StringBuffer("EEE d MMM")
			val sdf = SimpleDateFormat(formatString.toString(), Locale.ENGLISH)
			val startStr = sdf.format(hazard.start)
			val endStr = sdf.format(hazard.end)
			val startToEnd = StringBuffer()
			startToEnd.append(startStr)
			startToEnd.append(" - ")
			startToEnd.append(endStr)
			val cellrec = TextFieldCellRec(ctx.getString(R.string.hazard_details_field_label_duration),
										   startToEnd.toString())
			cellRecs.add(cellrec)
		}
	}

	private fun addHeadingCellRec(heading: String) {
		cellRecs.add(HeadingCellRec(heading))
	}

	private fun addHtmlFieldCellRec(html: String) {
		val rec = HtmlFieldCellRec(html)
		cellRecs.add(rec)
	}

	private fun createLaneCellRecAffected(lane: XLane): CellRec {
		val line: String =
			if (XHazard.TOKEN_EXTENT_BOTH == lane.affectedDirection) {
				ctx.getString(R.string.hazard_details_field_value_extent_both)
			} else {
				ctx.getString(R.string.hazard_details_field_value_extent_one,
							  lane.affectedDirection)
			}
		return LineCellRec(line)
	}

	/**
	 * Form a string of form "X of Y <DIR>bound lanes closed"
	 * e.g. "2 of 3 eastbound lanes closed".
	 * NOTE: Special case where X and Y are blank implies that both
	 * X and Y are "1", but instead of saying "1 of 1 <DIR>bound lanes closed"
	 * we say "<DIR>bound lane closed".
	 */
	private fun createLaneCellRecLanesClosed(lane: XLane): CellRec {
		val str = StringBuffer()

		if (isEmptyStr(lane.closedLanes) && isEmptyStr(lane.numberOfLanes)) {
			str.append(ctx.getString(R.string.hazard_details_field_value_extent_one,
									 lane.affectedDirection))
		} else {
			str.append(ctx.getString(R.string.hazard_details_field_value_one_of_n_lanes_closed,
									 lane.closedLanes, lane.numberOfLanes,
									 lane.affectedDirection?.toLowerCase() ?: ""))
		}

		// Suffix "(<description>)" e.g. "(right turn lane)"
		if (!isEmptyStr(lane.description)) {
			str.append(" ")
			str.append(ctx.getString(R.string.hazard_details_parenthetical_lane_description,
									 lane.description?.toLowerCase() ?: ""))
		}

		str.append(".")
		return LineCellRec(str.toString())
	}

	private fun createLaneCellRecLaneClosed(lane: XLane): CellRec {
		val str = StringBuffer()

		if (XHazard.TOKEN_EXTENT_BOTH == lane.affectedDirection) {
			str.append(ctx.getString(R.string.hazard_details_road_closed_in_both_directions,
									 lane.roadType))
		} else {
			str.append(ctx.getString(R.string.hazard_details_road_closed_in_one_direction,
									 lane.roadType,
									 lane.affectedDirection?.toLowerCase() ?: ""))
		}
		return LineCellRec(str.toString())
	}

	private fun addLaneCellRecs() {
		if (!isEmptyList(hazard.roads)) {
			val firstRoad = hazard.roads[0]

			if (!isEmptyList(firstRoad.impactedLanes)) {
				if (firstRoad.impactedLanes != null) {
					for (lane in firstRoad.impactedLanes) {
						if (!isEmptyStr(lane.extent)) {
							when (lane.extent) {
								XHazard.TOKEN_EXTENT -> cellRecs.add(createLaneCellRecAffected(lane))
								XHazard.TOKEN_LANES_CLOSED -> cellRecs.add(createLaneCellRecLanesClosed(lane))
								XHazard.TOKEN_EXTENT_LANE_CLOSED -> cellRecs.add(createLaneCellRecLaneClosed(lane))
							}
						}
					}
				}
			}
		}
	}

	private fun addOtherAdviceCellRec() {
		if (!isEmptyStr(hazard.otherAdvice)) {
			addHeadingCellRec(ctx.getString(R.string.hazard_details_heading_other_advice))
			if (hazard.otherAdvice != null) {
				addHtmlFieldCellRec(hazard.otherAdvice)
			}
		}
	}

	private fun addPublicTransportCellRec() {
		if (!isEmptyStr(hazard.publicTransport)) {
			addHeadingCellRec(ctx.getString(R.string.hazard_details_heading_public_transport))
			if (hazard.publicTransport != null) {
				addHtmlFieldCellRec(hazard.publicTransport)
			}
		}
	}

	private fun addQueueLengthCellRec() {
		if (!isEmptyList(hazard.roads)) {
			val firstRoad = hazard.roads[0]

			if ((firstRoad.queueLength != null) && (firstRoad.queueLength > 0)) {
				val fieldName = ctx.getString(R.string.hazard_details_field_label_queues)
				val fieldValue: String = ctx.getString(R.string.hazard_details_field_value_queues,
													   firstRoad.queueLength)
				val cellrec = TextFieldCellRec(fieldName, fieldValue)
				cellRecs.add(cellrec)
			}
		}
	}

	private fun addRtaAdviceCellRec() {
		if (!isEmptyStr(hazard.adviceA) || !isEmptyStr(hazard.adviceB)) {
			val value = StringBuffer()
			val fieldName = ctx.getString(R.string.hazard_details_rta_advice)

			if (!isEmptyStr(hazard.adviceA)) {
				value.append(hazard.adviceA)

				if (!isEmptyStr(hazard.adviceB)) {
					value.append(", ")
				}
			}

			if (!isEmptyStr(hazard.adviceB)) {
				value.append(hazard.adviceB)
			}
			val cellRec = TextFieldCellRec(fieldName,
										   value.toString())
			cellRecs.add(cellRec)
		}
	}

	private fun addTitleCellRec() {
		cellRecs.add(TitleCellRec(hazard))
	}

	/**
	 * "Traffic Volume" and "Delay" - adds a single LineCellRec containing a
	 * summary of both like "Heavy traffic. Significan delays" where the stings
	 * "Heavy" and "Significant" are taken from the JSON file.
	 */
	private fun addTrafficVolumeAndDelayCellRec() {
		if (!isEmptyList(hazard.roads)) {
			val firstRoad = hazard.roads[0]
			val str = StringBuffer()

			if (!isEmptyStr(firstRoad.trafficVolume)) {
				str.append(ctx.getString(R.string.hazard_details_traffic_volume, firstRoad.trafficVolume))
			}

			if (!isEmptyStr(firstRoad.delay)) {
				if (str.length > 1) {
					str.append(" ")
				}

				str.append(ctx.getString(R.string.hazard_details_traffic_delays, firstRoad.delay))
			}

			if (str.length > 1) {
				val rec = LineCellRec(str.toString())
				cellRecs.add(rec)
			}
		}
	}

	override fun getCount(): Int {
		return cellRecs.size
	}

	override fun getItem(position: Int): Any {
		return cellRecs[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var result: View
		val ctx = parent.context
		val cellRec: CellRec = cellRecs[position]

		result = when (cellRec) {
			is TitleCellRec -> HazardListItemView(ctx, cellRec.hazard, false, false)
			is HeadingCellRec -> ListHeadingView(ctx, cellRec.heading, false)
			is LineCellRec -> LineListItemView(ctx, cellRec.line)
			is TextFieldCellRec -> TextFieldListItemView(ctx, cellRec.fieldName, cellRec.fieldValue)
			is HtmlFieldCellRec -> HtmlListItemView(ctx, cellRec.fieldHtml)
			else -> View(ctx)
		}

		return result
	}

	private fun isEmptyList(list: List<*>?): Boolean {
		var result = false

		if (list == null) {
			result = true
		} else if (list.isEmpty()) {
			result = true
		}

		return result
	}

	private fun isEmptyStr(str: String?): Boolean {
		var result = false

		if (str == null) {
			result = true
		} else if (str.trim { it <= ' ' }.isEmpty()) {
			result = true
		}

		return result
	}

	private val isLastUpdatedSameAsCreated: Boolean
		get() {
			val lastUpdated = hazard.lastUpdated
			val created = hazard.created

			return lastUpdated != null && created != null
				&& lastUpdated == created
		}

	private fun removeFirstOfConsecutiveHeadings() {
		val cellRecsToRemove = HashSet<CellRec>()
		for (i in cellRecs.indices) {
			val thisrec = cellRecs[i]

			if (i < cellRecs.size - 2) {
				val nextrec = cellRecs[i + 1]

				if (thisrec is HeadingCellRec && nextrec is HeadingCellRec) {
					cellRecsToRemove.add(thisrec)
				}
			}

			if (i == cellRecs.size - 1) {
				if (thisrec is HeadingCellRec) {
					cellRecsToRemove.add(thisrec)
				}
			}
		}

		cellRecs.removeAll(cellRecsToRemove)
	}
}
