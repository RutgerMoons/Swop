package domain.scheduler;
/**
 * Object representing a shift. Shift has a start hour, an end hour and an amount of overtime.
 */
public class Shift {
	
	private final int start;
	private final int end;
	private int overtime;
	
	/**
	 * Constructs a Shift object.
	 * 
	 * @param start
	 * 		The starting hour of a shift in minutes
	 * @param end
	 * 		The end hour of a shift in minutes
	 * @param overtime
	 * 		The amount of overtime in a shift in minutes
	 */
	public Shift(int start, int end, int overtime) {
		this.start = start;
		this.end = end;
		this.overtime = overtime;
	}
	
	/**
	 * Returns the difference between the end of the shift and the given overtime.
	 */
	public int getEndOfShift() {
		return this.end - this.overtime;
	}
	
	/**
	 * @param newOvertime
	 * 		Value for the overtime in the next day of this shift.
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter has a negative value
	 */
	public void setNewOvertime(int newOvertime) {
		if(newOvertime < 0){
			throw new IllegalArgumentException();
		}
		this.overtime = newOvertime;
	}
	
	public int getStartOfShift() {
		return this.start;
	}

}
