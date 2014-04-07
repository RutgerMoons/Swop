package domain.clock;

public class ImmutableClock {

	private final int days;
	private final int minutes;
	private final int MINUTESINADAY = 1440;
	
	public ImmutableClock(int days, int minutes) {
		this.days = days;
		this.minutes = minutes;
	}
	
	public int getDays() {
		return days;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	/**
	 * return if this clock has the earliest time stamp or both clocks have the same time stamp
	 */
	public boolean isEarlierThan(ImmutableClock aClock) {
		if (aClock == null) {
			throw new IllegalArgumentException();
		}
		return this.getDays() < aClock.getDays() || 
				(this.getDays() == aClock.getDays() && this.getMinutes() <= aClock.getMinutes());
	}
	
	/**
	 * returns the difference in time in minutes
	 * if aClock is not earlier than this clock, return 0
	 */
	public int minus(ImmutableClock aClock) throws IllegalArgumentException {
		if (aClock.isEarlierThan(this)) {
			return 0;
		}
		return ((this.getDays() - aClock.getDays()) * MINUTESINADAY) + (this.getMinutes() - aClock.getMinutes());
	}
	
	@Override
	public String toString() {
		return 
				"day " + getDays() +
				(getMinutes() / 60) + " hours " + 
				(getMinutes() % 60) + " minutes.";	
	}
	
}
