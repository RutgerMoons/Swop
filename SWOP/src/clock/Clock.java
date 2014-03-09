package clock;

public class Clock {
	
	//public omdat AssemblyLine hier ook aan moet kunnen
	private final int MINUTESINADAY = 1440;
	private int minute;
	
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
		else if (this.minute + elapsedTime > MINUTESINADAY) this.minute = (this.minute + elapsedTime) % MINUTESINADAY;
		else this.minute +=elapsedTime;
	}
	
	/**
	 * Reset the clock to 0 (12am, the beginning of a day)
	 */
	public void reset(){
		this.minute = 0;
	}

}
