package clock;

/**
 * Represents a clock that stores the current time (in minutes) and the current day.
 */
public class Clock {
	
	private final int MINUTESINADAY = 1440;
	private int minute;
	private int day;
	/**
	 * Get the current time (in minutes).
	 * @return
	 * 			An integer that represents the current time, expressed in minutes.
	 */
	public int getTime(){
		return minute;
	}
	
	/**
	 * Advance the clock.
	 * @param elapsedTime
	 * 			An integer that represents how much the clock has to be advanced, expressed in minutes.
	 * @throws IllegalArgumentException
	 * 			If elapsedTime<0
	 */
	public void advanceTime(int elapsedTime) throws IllegalArgumentException{
		if (elapsedTime < 0) throw new IllegalArgumentException("argument can't be negative");
		else if (this.minute + elapsedTime > MINUTESINADAY){
			this.minute = (this.minute + elapsedTime) % MINUTESINADAY;
			day++;
		}
		else this.minute +=elapsedTime;
		
		
	}
	
	/**
	 * Reset the clock to 0 (12am, the beginning of a day)
	 */
	public void reset(){
		this.minute = 0;
	}

	/**
	 * Get the current day.
	 * @return
	 * 			An integer that represents the current day. 
	 * 			The 'current day' is interpreted as how many days the program has been running. (first day == 0)
	 */
	public int getDay() {
		return this.day;
	}


}
