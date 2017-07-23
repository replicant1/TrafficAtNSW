package rod.bailey.trafficatnsw.hazard.details

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import java.text.SimpleDateFormat
import java.util.HashSet
import java.util.LinkedList
import rod.bailey.trafficatnsw.hazard.details.cellrec.CellRec
import rod.bailey.trafficatnsw.hazard.details.cellrec.HeadingCellRec
import rod.bailey.trafficatnsw.hazard.details.cellrec.HtmlFieldCellRec
import rod.bailey.trafficatnsw.hazard.details.cellrec.LineCellRec
import rod.bailey.trafficatnsw.hazard.details.cellrec.TextFieldCellRec
import rod.bailey.trafficatnsw.hazard.details.cellrec.TitleCellRec
import rod.bailey.trafficatnsw.hazard.details.view.HtmlListItemView
import rod.bailey.trafficatnsw.hazard.details.view.LineListItemView
import rod.bailey.trafficatnsw.hazard.details.view.TextFieldListItemView
import rod.bailey.trafficatnsw.ui.view.HazardListItemView
import rod.bailey.trafficatnsw.json.hazard.XHazard
import rod.bailey.trafficatnsw.util.DateUtils
import rod.bailey.trafficatnsw.ui.view.ListHeadingView

class HazardDetailsListAdapter(private val hazard: XHazard) : BaseAdapter(), ListAdapter {
	private val cellRecs = LinkedList<CellRec>()

	init {
		// Title cell (always appears)
		addTitleCellRec()
		// "WHEN" heading
		addHeadingCellRec("WHEN")
		// "Cretaed" and "Last update" cells
		addCreateAndLastUpdatedCellRecs()
		// "Duration"
		addDurationCellRec()
		// "GENERAL" heading
		addHeadingCellRec("GENERAL")
		// "Attending"
		addAttendingCellRec()
		// "RTA Advice" cell
		addRtaAdviceCellRec()
		// "Queues" celll
		addQueueLengthCellRec()
		// "Closures" cells
		addClosureCellRecs()
		// "XLane" cells (each is an instance of LineCellRec in the lineCellRec
		// aray
		addLaneCellRecs()
		// "Traffic volume" and "Delay"
		addTrafficVolumeAndDelayCellRec()
		// "Public transport" hs HTML
		addPublicTransportCellRec()
		// "Arrangement elements" have HTML each one
		addArrangementElementCellRecs()
		// "Other advice" has HTML
		addOtherAdviceCellRec()

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
		// "Attending" row
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
				val cellrec = TextFieldCellRec("Attending",
											   strstr)
				cellRecs.add(cellrec)
			}
		}
	}

	private fun addClosureCellRecs() {
			for (period in hazard.periods) {
				if ("ROAD_CLOSURE" == period.closureType || "LANE_CLOSURE" == period.closureType) {
					// Planned road closure of lane closure
					val str = StringBuffer(period.fromDay)

					if (!isEmptyStr(period.toDay)) {
						str.append(" to ")
						str.append(period.toDay)
					}

					if (!isEmptyStr(period.startTime)) {
						str.append(" (")
						str.append(period.startTime)

						if (!isEmptyStr(period.finishTime)) {
							str.append(" to ")
							str.append(period.finishTime)
						}

						str.append(")")
					}

					if (!isEmptyStr(period.direction)) {
						str.append(". ")
						str.append(period.direction)
						str.append(" affected")
					}
					val fieldValue = str.toString()
					var fieldName: String?

					if ("ROAD_CLOSURE" == period.closureType) {
						fieldName = "Roads closed"
					} else {
						fieldName = "Lanes closed"
					}
					val cellRec = TextFieldCellRec(fieldName,
												   fieldValue)
					cellRecs.add(cellRec)
				}
			}
	}

	private fun addCreateAndLastUpdatedCellRecs() {
		// First CellRec is for "Created" date/time
		if (hazard.created != null) {
			val createdStr = DateUtils.relativeDateAndTime(
				hazard.created, true)
			val createdCellRec = TextFieldCellRec("Started",
												  createdStr)
			cellRecs.add(createdCellRec)
		}
		// Second CellRec is for "Last updated" date/time. But we only create a
		// 'last updated' cellrec if it will show a different date to the
		// 'created' date.
		if (!isLastUpdatedSameAsCreated) {
			val lastUpdatedStr = DateUtils.relativeDateAndTime(
				hazard.lastUpdated, true)
			val lastUpdatedCellRec = TextFieldCellRec(
				"Last checked", lastUpdatedStr)
			cellRecs.add(lastUpdatedCellRec)
		}
	}

	private fun addDurationCellRec() {
		if (hazard.start != null && hazard.end != null) {
			val formatString = StringBuffer("EEE d MMM")
			val sdf = SimpleDateFormat(formatString.toString())
			val startStr = sdf.format(hazard.start)
			val endStr = sdf.format(hazard.end)
			val startToEnd = StringBuffer()
			startToEnd.append(startStr)
			startToEnd.append(" - ")
			startToEnd.append(endStr)
			val cellrec = TextFieldCellRec("Duration",
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

	private fun addLaneCellRecs() {
		if (!isEmptyList(hazard.roads)) {
			val firstRoad = hazard.roads[0]

			if (!isEmptyList(firstRoad.impactedLanes)) {
				for (lane in firstRoad.impactedLanes!!) {
					if (!isEmptyStr(lane.extent)) {
						if ("Affected" == lane.extent) {
							var line: String =
							if ("Both directions" == lane.affectedDirection) {
								"Traffic in both directions is affected."
							} else {
								String.format("%s traffic is affected.", lane.affectedDirection)
							}
							val rec = LineCellRec(line)
							cellRecs.add(rec)
						} else if ("Lanes closed" == lane.extent) {
							val str = StringBuffer()
							// Form a string of form
							// "X of Y <DIR>bound lanes closed"
							// e.g. "2 of 3 eastbound lanes closed".
							// NOTE: Special case where X and Y are blank
							// implies that both
							// X and Y are "1", but instead of saying
							// "1 of 1 <DIR>bound lanes closed"
							// we say "<DIR>bound lane closed".
							if (isEmptyStr(lane.closedLanes) && isEmptyStr(lane.numberOfLanes)) {
								str.append(lane.affectedDirection)
								str.append(" lane closed")
							} else {
								str.append(lane.closedLanes)
								str.append(" of ")
								str.append(lane.numberOfLanes)
								str.append(" ")
								str.append(lane.affectedDirection!!
											   .toLowerCase())
								str.append(" lanes closed")
							}
							// Suffix "(<description>)" e.g. "(right turn lane)"
							if (!isEmptyStr(lane.description)) {
								str.append(" (")
								str.append(lane.description!!.toLowerCase())
								str.append(")")
							}

							str.append(".")
							val rec = LineCellRec(str.toString())
							cellRecs.add(rec)
						} else if ("Closed" == lane.extent) {
							val str = StringBuffer()
							str.append(lane.roadType)
							str.append(" closed in ")

							if ("Both directions" == lane
								.affectedDirection) {
								str.append("both directions.")
							} else {
								str.append(lane.affectedDirection!!
											   .toLowerCase())
								str.append(" direction.")
							}
							val rec = LineCellRec(str.toString())
							cellRecs.add(rec)
						}
					}
				}
			}
		}
	}

	private fun addOtherAdviceCellRec() {
		if (!isEmptyStr(hazard.otherAdvice)) {
			addHeadingCellRec("Other advice")
			if (hazard.otherAdvice != null) {
				addHtmlFieldCellRec(hazard.otherAdvice)
			}
		}
	}

	private fun addPublicTransportCellRec() {
		if (!isEmptyStr(hazard.publicTransport)) {
			addHeadingCellRec("Public transport")
			if (hazard.publicTransport != null) {
				addHtmlFieldCellRec(hazard.publicTransport)
			}
		}
	}

	private fun addQueueLengthCellRec() {
		if (!isEmptyList(hazard.roads)) {
			val firstRoad = hazard.roads[0]

			if ((firstRoad.queueLength != null) && (firstRoad.queueLength > 0)) {
				val fieldName = "Queues"
				val fieldValue: String? = null
				val queueStr = StringBuffer()
				val queueLengthNumber = firstRoad.queueLength

				queueStr.append("" + queueLengthNumber)
				queueStr.append("km")
				val cellrec = TextFieldCellRec(fieldName,
											   fieldValue!!)
				cellRecs.add(cellrec)
			}
		}
	}

	private fun addRtaAdviceCellRec() {
		if (!isEmptyStr(hazard.adviceA) || !isEmptyStr(hazard.adviceB)) {
			val value = StringBuffer()
			val fieldName = "Advice"

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
				str.append(firstRoad.trafficVolume)
				str.append(" traffic.")
			}

			if (!isEmptyStr(firstRoad.delay)) {
				if (str.length > 1) {
					str.append(" ")
				}

				str.append(firstRoad.delay)
				str.append(" delays.")
			}

			if (str.length > 1) {
				val rec = LineCellRec(str.toString())
				cellRecs.add(rec)
			}
		}
	}

//	private fun cellForHtmlCellRecAtIndex(i: Int): View? {
//		return null
//	}
//
//	private fun cellForNonHtmlCellRecAtIndex(cellRecIndex: Int): View? {
//		return null
//	}
//
//	private fun createHtmlFieldCellRec(fieldName: String,
//									   fieldValue: String, tag: Int): HtmlFieldCellRec? {
//		return null
//	}

	override fun getCount(): Int {
		return cellRecs.size
	}

//	private fun getHeightForHtmlCellRecAtIndex(index: Int): Double {
//		return 0.0
//	}

	override fun getItem(position: Int): Any {
		return cellRecs[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var result: View
		val ctx = parent.context
		val cellRec:CellRec = cellRecs[position]

		result = when (cellRec) {
			is TitleCellRec -> HazardListItemView(ctx,
																				  cellRec.hazard,
																				  false, false)
			is HeadingCellRec -> ListHeadingView(ctx,
																				 cellRec.heading,
																				 false)
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

	private fun numberOfRowsInGeneralSection(): Int {
		val numRows = 0
		return numRows
	}

	private fun numberOfRowsInWhenSection(): Int {
		val numRows = 0

		return numRows
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
