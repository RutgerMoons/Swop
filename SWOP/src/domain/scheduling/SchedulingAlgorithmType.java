package domain.scheduling;

/**
 * A clean way to differentiate Scheduling Algorithms
 */
public enum SchedulingAlgorithmType {

	FIFO("Fifo"), BATCH("Batch");
	
	private final String type;
	
	private SchedulingAlgorithmType(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
}