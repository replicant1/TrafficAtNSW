package rod.bailey.trafficatnsw.hazard.details;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import rod.bailey.trafficatnsw.hazard.HazardListItemView;
import rod.bailey.trafficatnsw.json.hazard.XArrangementElement;
import rod.bailey.trafficatnsw.json.hazard.XHazard;
import rod.bailey.trafficatnsw.json.hazard.XLane;
import rod.bailey.trafficatnsw.json.hazard.XPeriod;
import rod.bailey.trafficatnsw.json.hazard.XRoad;
import rod.bailey.trafficatnsw.util.DateUtils;
import rod.bailey.trafficatnsw.util.ListHeadingView;

public class HazardDetailsListAdapter extends BaseAdapter implements
		ListAdapter {

	private List<CellRec> cellRecs = new LinkedList<CellRec>();

	private final XHazard hazard;

	public HazardDetailsListAdapter(XHazard hazard) {
		assert hazard != null;
		this.hazard = hazard;

		// Title cell (always appears)
		addTitleCellRec();

		// "WHEN" heading
		addHeadingCellRec("WHEN");

		// "Cretaed" and "Last update" cells
		addCreateAndLastUpdatedCellRecs();

		// "Duration"
		addDurationCellRec();

		// "GENERAL" heading
		addHeadingCellRec("GENERAL");

		// "Attending"
		addAttendingCellRec();

		// "RTA Advice" cell
		addRtaAdviceCellRec();

		// "Queues" celll
		addQueueLengthCellRec();

		// "Closures" cells
		addClosureCellRecs();

		// "XLane" cells (each is an instance of LineCellRec in the lineCellRec
		// aray
		addLaneCellRecs();

		// "Traffic volume" and "Delay"
		addTrafficVolumeAndDelayCellRec();

		// "Public transport" hs HTML
		addPublicTransportCellRec();

		// "Arrangement elements" have HTML each one
		addArrangementElementCellRecs();

		// "Other advice" has HTML
		addOtherAdviceCellRec();

		removeFirstOfConsecutiveHeadings();
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
	
	private void addArrangementElementCellRecs() {
		if (!isEmptyList(hazard.getArrangementElements())) {
			for (XArrangementElement element : hazard.getArrangementElements()) {
				addHeadingCellRec(element.getTitle());
				addHtmlFieldCellRec(element.getHtml());
			}
		}
	}

	private void addAttendingCellRec() {
		// "Attending" row
		if (!isEmptyList(hazard.getAttendingGroups())) {
			int numAttendingGroups = hazard.getAttendingGroups().size();

			StringBuffer str = new StringBuffer();

			for (int i = 0; i < numAttendingGroups; i++) {
				String group = hazard.getAttendingGroups().get(i);

				if (!isEmptyStr(group)) {
					str.append(group);
				}

				if (i < (numAttendingGroups - 1)) {
					str.append(", ");
				}
			} // for i

			String strstr = str.toString();
			if (!isEmptyStr(strstr)) {
				TextFieldCellRec cellrec = new TextFieldCellRec("Attending",
						strstr);
				cellRecs.add(cellrec);
			}
		}
	}

	private void addClosureCellRecs() {
		if (hazard.getPeriods() != null) {
			for (XPeriod period : hazard.getPeriods()) {
				if ("ROAD_CLOSURE".equals(period.getClosureType())
						|| "LANE_CLOSURE".equals(period.getClosureType())) {
					// Planned road closure of lane closure

					StringBuffer str = new StringBuffer(period.getFromDay());

					if (!isEmptyStr(period.getToDay())) {
						str.append(" to ");
						str.append(period.getToDay());
					}

					if (!isEmptyStr(period.getStartTime())) {
						str.append(" (");
						str.append(period.getStartTime());

						if (!isEmptyStr(period.getFinishTime())) {
							str.append(" to ");
							str.append(period.getFinishTime());
						}

						str.append(")");
					}

					if (!isEmptyStr(period.getDirection())) {
						str.append(". ");
						str.append(period.getDirection());
						str.append(" affected");
					}

					String fieldValue = str.toString();
					String fieldName = null;

					if ("ROAD_CLOSURE".equals(period.getClosureType())) {
						fieldName = "Roads closed";
					} else {
						fieldName = "Lanes closed";
					}

					TextFieldCellRec cellRec = new TextFieldCellRec(fieldName,
							fieldValue);
					cellRecs.add(cellRec);
				}
			}
		}
	}

	private void addCreateAndLastUpdatedCellRecs() {
		// First CellRec is for "Created" date/time
		if (hazard.getCreated() != null) {
			String createdStr = DateUtils.relativeDateAndTime(
					hazard.getCreated(), true);
			TextFieldCellRec createdCellRec = new TextFieldCellRec("Started",
					createdStr);
			cellRecs.add(createdCellRec);
		}

		// Second CellRec is for "Last updated" date/time. But we only create a
		// 'last updated' cellrec if it will show a different date to the
		// 'created' date.
		if (!isLastUpdatedSameAsCreated()) {
			String lastUpdatedStr = DateUtils.relativeDateAndTime(
					hazard.getLastUpdated(), true);
			TextFieldCellRec lastUpdatedCellRec = new TextFieldCellRec(
					"Last checked", lastUpdatedStr);
			cellRecs.add(lastUpdatedCellRec);
		}
	}

	private void addDurationCellRec() {
		if ((hazard.getStart() != null) && (hazard.getEnd() != null)) {
			StringBuffer formatString = new StringBuffer("EEE d MMM");
			SimpleDateFormat sdf = new SimpleDateFormat(formatString.toString());

			String startStr = sdf.format(hazard.getStart());
			String endStr = sdf.format(hazard.getEnd());

			StringBuffer startToEnd = new StringBuffer();
			startToEnd.append(startStr);
			startToEnd.append(" - ");
			startToEnd.append(endStr);

			TextFieldCellRec cellrec = new TextFieldCellRec("Duration",
					startToEnd.toString());
			cellRecs.add(cellrec);
		}
	}

	private void addHeadingCellRec(String heading) {
		cellRecs.add(new HeadingCellRec(heading));
	}

	private void addHtmlFieldCellRec(String html) {
		HtmlFieldCellRec rec = new HtmlFieldCellRec(html, null, 0);
		cellRecs.add(rec);
	}

	private void addLaneCellRecs() {
		if (!isEmptyList(hazard.getRoads())) {
			XRoad firstRoad = hazard.getRoads().get(0);

			if (!isEmptyList(firstRoad.getImpactedLanes())) {
				for (XLane lane : firstRoad.getImpactedLanes()) {
					if (!isEmptyStr(lane.getExtent())) {
						String line = null;

						if ("Affected".equals(lane.getExtent())) {
							if ("Both directions".equals(lane
									.getAffectedDirection())) {
								line = "Traffic in both directions is affected.";
							} else {
								line = String.format("%s traffic is affected.",
										lane.getAffectedDirection());
							}
						} else if ("Lanes closed".equals(lane.getExtent())) {
							StringBuffer str = new StringBuffer();

							// Form a string of form
							// "X of Y <DIR>bound lanes closed"
							// e.g. "2 of 3 eastbound lanes closed".
							// NOTE: Special case where X and Y are blank
							// implies that both
							// X and Y are "1", but instead of saying
							// "1 of 1 <DIR>bound lanes closed"
							// we say "<DIR>bound lane closed".
							if (isEmptyStr(lane.getClosedLanes())
									&& isEmptyStr(lane.getNumberOfLanes())) {
								str.append(lane.getAffectedDirection());
								str.append(" lane closed");
							} else {
								str.append(lane.getClosedLanes());
								str.append(" of ");
								str.append(lane.getNumberOfLanes());
								str.append(" ");
								str.append(lane.getAffectedDirection()
										.toLowerCase());
								str.append(" lanes closed");
							}

							// Suffix "(<description>)" e.g. "(right turn lane)"
							if (!isEmptyStr(lane.getDescription())) {
								str.append(" (");
								str.append(lane.getDescription().toLowerCase());
								str.append(")");
							}

							str.append(".");
							LineCellRec rec = new LineCellRec(str.toString());
							cellRecs.add(rec);
						} else if ("Closed".equals(lane.getExtent())) {
							StringBuffer str = new StringBuffer();
							str.append(lane.getRoadType());
							str.append(" closed in ");

							if ("Both directions".equals(lane
									.getAffectedDirection())) {
								str.append("both directions.");
							} else {
								str.append(lane.getAffectedDirection()
										.toLowerCase());
								str.append(" direction.");
							}

							LineCellRec rec = new LineCellRec(str.toString());
							cellRecs.add(rec);
						}
					}
				}
			}
		}
	}

	private void addOtherAdviceCellRec() {
		if (!isEmptyStr(hazard.getOtherAdvice())) {
			addHeadingCellRec("Other advice");
			addHtmlFieldCellRec(hazard.getOtherAdvice());
		}
	}

	private void addPublicTransportCellRec() {
		if (!isEmptyStr(hazard.getPublicTransport())) {
			addHeadingCellRec("Public transport");
			addHtmlFieldCellRec(hazard.getPublicTransport());
		}
	}

	private void addQueueLengthCellRec() {
		if (!isEmptyList(hazard.getRoads())) {
			XRoad firstRoad = hazard.getRoads().get(0);

			if ((firstRoad != null) && (firstRoad.getQueueLength() > 0)) {
				String fieldName = "Queues";
				String fieldValue = null;

				StringBuffer queueStr = new StringBuffer();
				int queueLengthNumber = firstRoad.getQueueLength();

				queueStr.append("" + queueLengthNumber);
				queueStr.append("km");

				TextFieldCellRec cellrec = new TextFieldCellRec(fieldName,
						fieldValue);
				cellRecs.add(cellrec);
			}
		}
	}

	private void addRtaAdviceCellRec() {
		if (!isEmptyStr(hazard.getAdviceA())
				|| !isEmptyStr(hazard.getAdviceB())) {
			StringBuffer value = new StringBuffer();

			String fieldName = "Advice";

			if (!isEmptyStr(hazard.getAdviceA())) {
				value.append(hazard.getAdviceA());

				if (!isEmptyStr(hazard.getAdviceB())) {
					value.append(", ");
				}
			}

			if (!isEmptyStr(hazard.getAdviceB())) {
				value.append(hazard.getAdviceB());
			}

			TextFieldCellRec cellRec = new TextFieldCellRec(fieldName,
					value.toString());
			cellRecs.add(cellRec);
		}
	}

	private void addTitleCellRec() {
		cellRecs.add(new TitleCellRec(hazard));
	}

	/**
	 * "Traffic Volume" and "Delay" - adds a single LineCellRec containing a
	 * summary of both like "Heavy traffic. Significan delays" where the stings
	 * "Heavy" and "Significant" are taken from the JSON file.
	 */
	private void addTrafficVolumeAndDelayCellRec() {
		if (!isEmptyList(hazard.getRoads())) {
			XRoad firstRoad = hazard.getRoads().get(0);

			StringBuffer str = new StringBuffer();

			if (!isEmptyStr(firstRoad.getTrafficVolume())) {
				str.append(firstRoad.getTrafficVolume());
				str.append(" traffic.");
			}

			if (!isEmptyStr(firstRoad.getDelay())) {
				if (str.length() > 1) {
					str.append(" ");
				}

				str.append(firstRoad.getDelay());
				str.append(" delays.");
			}

			if (str.length() > 1) {
				LineCellRec rec = new LineCellRec(str.toString());
				cellRecs.add(rec);
			}
		}
	}

	private View cellForHtmlCellRecAtIndex(int i) {
		return null;
	}

	private View cellForNonHtmlCellRecAtIndex(int cellRecIndex) {
		return null;
	}

	private HtmlFieldCellRec createHtmlFieldCellRec(String fieldName,
			String fieldValue, int tag) {
		return null;
	}

	@Override
	public int getCount() {
		return cellRecs.size();
	}

	private double getHeightForHtmlCellRecAtIndex(int index) {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return cellRecs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = null;
		Context ctx = parent.getContext();

		CellRec cellRec = cellRecs.get(position);

		if (cellRec instanceof TitleCellRec) {
			TitleCellRec titleCellRec = (TitleCellRec) cellRec;
			result = new HazardListItemView(ctx, titleCellRec.hazard, false,
					false);
		} else if (cellRec instanceof HeadingCellRec) {
			HeadingCellRec headingCellRec = (HeadingCellRec) cellRec;
			result = new ListHeadingView(ctx, headingCellRec.heading, false);
		} else if (cellRec instanceof LineCellRec) {
			LineCellRec lineCellRec = (LineCellRec) cellRec;
			result = new LineListItemView(ctx, lineCellRec.getLine());
		} else if (cellRec instanceof TextFieldCellRec) {
			TextFieldCellRec textCellRec = (TextFieldCellRec) cellRec;
			result = new TextFieldListItemView(ctx, textCellRec.fieldName,
					textCellRec.fieldValue);
		} else if (cellRec instanceof HtmlFieldCellRec) {
			HtmlFieldCellRec htmlCellRec = (HtmlFieldCellRec) cellRec;
			result = new HtmlListItemView(ctx, htmlCellRec.fieldHtml);
		}

		return result;
	}

	private boolean isEmptyList(List list) {
		boolean result = false;

		if (list == null) {
			result = true;
		} else if (list.isEmpty()) {
			result = true;
		}

		return result;
	}

	private boolean isEmptyStr(String str) {
		boolean result = false;

		if (str == null) {
			result = true;
		} else if (str.trim().isEmpty()) {
			result = true;
		}

		return result;
	}

	private boolean isLastUpdatedSameAsCreated() {
		Date lastUpdated = hazard.getLastUpdated();
		Date created = hazard.getCreated();

		return (lastUpdated != null) && (created != null)
				&& (lastUpdated.equals(created));
	}

	private int numberOfRowsInGeneralSection() {
		int numRows = 0;
		return numRows;
	}

	private int numberOfRowsInWhenSection() {
		int numRows = 0;

		return numRows;
	}

	private void removeFirstOfConsecutiveHeadings() {
		Set<CellRec> cellRecsToRemove = new HashSet<CellRec>();
		for (int i = 0; i < cellRecs.size(); i++) {
			CellRec thisrec = cellRecs.get(i);

			if (i < (cellRecs.size() - 2)) {
				CellRec nextrec = cellRecs.get(i + 1);

				if ((thisrec instanceof HeadingCellRec)
						&& (nextrec instanceof HeadingCellRec)) {
					cellRecsToRemove.add(thisrec);
				}
			}

			if (i == (cellRecs.size() - 1)) {
				if (thisrec instanceof HeadingCellRec) {
					cellRecsToRemove.add(thisrec);
				}
			}
		}

		cellRecs.removeAll(cellRecsToRemove);
	}

}
