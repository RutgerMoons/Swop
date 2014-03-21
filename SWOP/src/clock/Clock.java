package clock;

/**
 * Represents a clock that stores the current time (in minutes) and the current day.
 */
public class Clock {
	
	private final int MINUTESINADAY = 1440;
	private final int MINUTESSTARTOFDAY= 360;
	private int minute;
	private int day;
	/**
	 * Get the current time (in minutes).
	 * @return
	 * 			An integer that represents the current time, expressed in minutes.
	 */
	public int getMinutes(){
		return minute;
	}
	
	private void setMinutes(int min) {
		if (min >= 0) {
			this.minute = min % MINUTESINADAY;
		}
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
			setMinutes((this.minute + elapsedTime) % MINUTESINADAY);
			day++;
		}
		else setMinutes(this.minute + elapsedTime);
		
		
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
	
	/**
	 * the amount of days is incremented with one
	 */
	private void incrementDay() {
		this.day++;
	}
	
	/**
	 * if it is before midnight increment the day with one
	 * set the minutes to MINUTESSTARTOFDAY
	 */
	public void startNewDay() {
		if (getMinutes() < MINUTESINADAY) {
			incrementDay();
		}
		setMinutes(MINUTESSTARTOFDAY);
	}


}
