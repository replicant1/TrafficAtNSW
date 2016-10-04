package rod.bailey.trafficatnsw.hazard.details;


public class TextFieldCellRec implements CellRec {
	public static final int LANE_CLOSURE_TYPE_CODE = 2;
	public static final int ROAD_CLOSURE_TYPE_CODE = 3;
	
	public final String fieldName;
	public final String fieldValue;
	public final int typeCode;
	
	public TextFieldCellRec(String fieldName, String fieldValue, int typeCode) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.typeCode = typeCode;
	}
	
	public TextFieldCellRec(String fieldName, String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.typeCode = -1;
	}
}
