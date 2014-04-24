package domain.scheduler;

public enum SchedulingAlgorithmType {

	FIFO("Fifo"), BATCH("Batch");
	
	private final String type;
	
	private SchedulingAlgorithmType(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}
	
	public String toString() {
		return this.type;
	}
}
