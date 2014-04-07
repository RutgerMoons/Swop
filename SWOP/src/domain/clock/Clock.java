package domain.clock;

/**
 * Represents a clock that stores the current time (in minutes) and the current day.
 */
public class Clock {
	
	private final int MINUTESINADAY = 1440;
	private final int MINUTESSTARTOFDAY= 360;
	private int minutes;
	private int days;
	/**
	 * Get the current time (in minutes).
	 * @return
	 * 			An integer that represents the current time, expressed in minutes.
	 */
	public int getMinutes(){
		return minutes;
	}
	
	private void setMinutes(int min) {
		if (min >= 0) {
			this.minutes = min % MINUTESINADAY;
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
		else if (this.minutes + elapsedTime > MINUTESINADAY){
			setMinutes((this.minutes + elapsedTime) % MINUTESINADAY);
			//TODO: wat als er meer dan 1 dag tegelijk advanced moet worden?
			days++;
		}
		else setMinutes(this.minutes + elapsedTime);
		
		
	}

	/**
	 * Get the current day.
	 * @return
	 * 			An integer that represents the current day. 
	 * 			The 'current day' is interpreted as how many days the program has been running. (first day == 0)
	 */
	public int getDays() {
		return this.days;
	}
	
	/**
	 * the amount of days is incremented with one
	 */
	private void incrementDay() {
		this.days++;
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
	
	public ImmutableClock getImmutableClock() {
		return new ImmutableClock(getDays(), getMinutes());
	}


}
