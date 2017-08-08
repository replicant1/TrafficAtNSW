package rod.bailey.trafficatnsw.hazard.details

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import rod.bailey.trafficatnsw.R
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.common.ui.ListHeadingView_
import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XLane
import rod.bailey.trafficatnsw.hazard.data.XProperties
import rod.bailey.trafficatnsw.hazard.details.ui.cellrec.*
import rod.bailey.trafficatnsw.hazard.details.ui.view.HtmlListItemView_
import rod.bailey.trafficatnsw.hazard.details.ui.view.LineListItemView_
import rod.bailey.trafficatnsw.hazard.details.ui.view.TextFieldListItemView_
import rod.bailey.trafficatnsw.hazard.ui.HazardListItemView_
import rod.bailey.trafficatnsw.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HazardDetailsListAdapter(private val ctx: Context, private val hazard: XHazard) : BaseAdapter(),
	ListAdapter {
	private val cellRecs = LinkedList<CellRec>()

	@Inject
	lateinit var config: ConfigSingleton

	init {
		TrafficAtNSWApplication.graph.inject(this)

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

	override fun isEnabled(position: Int): Boolean = cellRecs[position] !is HeadingCellRec
	override fun areAllItemsEnabled(): Boolean = false

	private fun addArrangementElementCellRecs() {
		val props: XProperties? = hazard.properties
		if (props != null) {
			val elts = props.arrangementElements
			if ((elts != null) && (elts.size > 0)) {
				for (element in elts) {
					if (element.title != null) {
						addHeadingCellRec(element.title)
					}
					if (element.html != null) {
						addHtmlFieldCellRec(element.html)
					}
				}
			}
		}
	}

	private fun addAttendingCellRec() {
		val props: XProperties? = hazard.properties
		if (props != null) {
			val groups = props.attendingGroups
			if ((groups != null) && (groups.size > 0)) {
				val numAttendingGroups = groups.size
				val str = StringBuffer()

				for (i in 0..numAttendingGroups - 1) {
					val group = groups[i]

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
	}

	private fun addClosureCellRecs() {
		val props: XProperties? = hazard.properties
		if (props != null) {
			val periods = props.periods
			if (periods != null) {
				for (period in periods) {
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
							str.append(ctx.getString(R.string.hazard_details_closure_direction_affected),
									   period.direction)
							str.append(" ")
						}

						val fieldValue = str.toString()
						val fieldName: String =
							if (XHazard.TOKEN_ROAD_CLOSURE == period.closureType) {
								ctx.getString(R.string.hazard_details_field_label_roads_closed)
							} else {
								ctx.getString(R.string.hazard_details_field_label_lanes_closed)
							}
						val cellRec = TextFieldCellRec(fieldName, fieldValue)
						cellRecs.add(cellRec)
					}
				} // for
			}
		}
	}

	private fun addCreateAndLastUpdatedCellRecs() {
		// First CellRec is for "Created" date/time
		val props: XProperties? = hazard.properties
		if ((props != null) && (props.created != null)) {
			val createdStr = DateUtils.relativeDateAndTime(
				Date(props.created), capitalize = true)
			val createdCellRec = TextFieldCellRec(
				fieldName = ctx.getString(R.string.hazard_details_field_label_when_started),
				fieldValue = createdStr)
			cellRecs.add(createdCellRec)
		}

		// Second CellRec is for "Last updated" date/time. But we only create a
		// 'last updated' cellrec if it will show a different date to the
		// 'created' date.
		if ((props != null) && (!isLastUpdatedSameAsCreated) && (props.lastUpdated != null)) {
			val lastUpdatedStr = DateUtils.relativeDateAndTime(
				Date(props.lastUpdated), capitalize = true)
			val lastUpdatedCellRec = TextFieldCellRec(
				fieldName = ctx.getString(R.string.hazard_details_field_label_when_last_checked),
				fieldValue = lastUpdatedStr)
			cellRecs.add(lastUpdatedCellRec)
		}
	}

	private fun addDurationCellRec() {
		val props: XProperties? = hazard.properties
		if (props != null) {
			if (props.start != null && props.end != null) {
				val formatString = StringBuffer(config.durationTimeFormat())
				val sdf = SimpleDateFormat(formatString.toString(), Locale.ENGLISH)
				val startStr = sdf.format(props.start)
				val endStr = sdf.format(props.end)
				val startToEnd = StringBuffer()
				startToEnd.append(startStr)
				startToEnd.append(" - ")
				startToEnd.append(endStr)
				val cellrec = TextFieldCellRec(ctx.getString(R.string.hazard_details_field_label_duration),
											   startToEnd.toString())
				cellRecs.add(cellrec)
			}
		}
	}

	private fun addHeadingCellRec(heading: String) = cellRecs.add(HeadingCellRec(heading))
	private fun addHtmlFieldCellRec(html: String) = cellRecs.add(HtmlFieldCellRec(html))

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
		val props: XProperties? = hazard.properties
		if ((props != null) && (props.roads != null) && (props.roads.size > 0)) {
			val firstRoad = props.roads[0]

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
		val props: XProperties? = hazard.properties
		if ((props != null) && (props.otherAdvice != null) && (!isEmptyStr(props.otherAdvice))) {
			addHeadingCellRec(ctx.getString(R.string.hazard_details_heading_other_advice))
			addHtmlFieldCellRec(props.otherAdvice)
		}
	}

	private fun addPublicTransportCellRec() {
		val props: XProperties? = hazard.properties
		if ((props != null) && !isEmptyStr(props.publicTransport)) {
			addHeadingCellRec(ctx.getString(R.string.hazard_details_heading_public_transport))
			if (props.publicTransport != null) {
				addHtmlFieldCellRec(props.publicTransport)
			}
		}
	}

	private fun addQueueLengthCellRec() {
		val props: XProperties? = hazard.properties
		if ((props != null) && (props.roads != null) && !isEmptyList(props.roads)) {
			val firstRoad = props.roads[0]

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
		val props: XProperties? = hazard.properties
		if (props != null) {
			if (!isEmptyStr(props.adviceA) || !isEmptyStr(props.adviceB)) {
				val value = StringBuffer()
				val fieldName = ctx.getString(R.string.hazard_details_rta_advice)

				if (!isEmptyStr(props.adviceA)) {
					value.append(props.adviceA)

					if (!isEmptyStr(props.adviceB)) {
						value.append(", ")
					}
				}

				if (!isEmptyStr(props.adviceB)) {
					value.append(props.adviceB)
				}
				val cellRec = TextFieldCellRec(fieldName, fieldValue = value.toString())
				cellRecs.add(cellRec)
			}
		}
	}

	private fun addTitleCellRec() = cellRecs.add(TitleCellRec(hazard))

	/**
	 * "Traffic Volume" and "Delay" - adds a single LineCellRec containing a
	 * summary of both like "Heavy traffic. Significan delays" where the stings
	 * "Heavy" and "Significant" are taken from the JSON file.
	 */
	private fun addTrafficVolumeAndDelayCellRec() {
		val props: XProperties? = hazard.properties
		if ((props != null) && (props.roads != null) && !isEmptyList(props.roads)) {
			val firstRoad = props.roads[0]
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

	override fun getCount(): Int = cellRecs.size
	override fun getItem(position: Int): Any = cellRecs[position]
	override fun getItemId(position: Int): Long = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val result: View
		val ctx = parent.context
		val cellRec: CellRec = cellRecs[position]

		result = when (cellRec) {
			is TitleCellRec -> HazardListItemView_.build(ctx, cellRec.hazard, false, false)
			is HeadingCellRec -> ListHeadingView_.build(ctx, cellRec.heading)
			is LineCellRec -> LineListItemView_.build(ctx, cellRec.line)
			is TextFieldCellRec -> TextFieldListItemView_.build(ctx, cellRec.fieldName, cellRec.fieldValue)
			is HtmlFieldCellRec -> HtmlListItemView_.build(ctx, cellRec.fieldHtml)
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
			var result = false
			val props: XProperties? = hazard.properties

			if (props != null) {
				val lastUpdated: Long? = props.lastUpdated
				val created: Long? = props.created

				result = (lastUpdated != null) && (created != null) && (lastUpdated == created)
			}

			return result
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
