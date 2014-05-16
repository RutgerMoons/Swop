package domain.scheduling;
/**
 * A class representing a shift. Shift has a start hour, an end hour and an amount of overtime.
 */
public class Shift {
	
	private final int start;
	private final int end;
	private int overtime;
	
	/**
	 * Constructs a Shift object.
	 * 
	 * @param 	start
	 * 			The starting hour of a shift in minutes
	 * 
	 * @param 	end
	 * 			The end hour of a shift in minutes
	 * 
	 * @param 	overtime
	 * 			The amount of overtime in a shift in minutes
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the value of end is smaller then start or 
	 * 			when start or end have a negative value
	 */
	public Shift(int start, int end, int overtime) {
		if (end < start || start < 0 || end < 0){
			throw new IllegalArgumentException();
		}
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
	 * Method for setting the overtime for the next day.
	 * 
	 * @param 	newOvertime
	 * 			Value for the overtime in the next day of this shift
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter has a negative value
	 */
	public void setNewOvertime(int newOvertime) {
		if(newOvertime < 0){
			throw new IllegalArgumentException();
		}
		this.overtime = newOvertime;
	}
	
	/**
	 * Returns the start of the shift in minutes.
	 */
	public int getStartOfShift() {
		return this.start;
	}

}
