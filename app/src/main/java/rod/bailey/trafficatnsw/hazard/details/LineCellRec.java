package rod.bailey.trafficatnsw.hazard.details;


public class LineCellRec implements CellRec {
	private final String line;
	public LineCellRec(String line) {
		this.line = line;
	}
	
	public String getLine() {
		return line;
	}
}
