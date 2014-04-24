package domain.clock;

/**
 * Class representing a snapshot of the clock at some point.
 *
 */
public class UnmodifiableClock implements Comparable<UnmodifiableClock> {

	private final int days;
	private final int minutes;
	private final int MINUTESINADAY = 1440;
	
	public UnmodifiableClock(int days, int minutes) {
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
	public boolean isEarlierThan(UnmodifiableClock aClock) {
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
	public int minus(UnmodifiableClock aClock) throws IllegalArgumentException {
		if (this.isEarlierThan(aClock)) {
			return 0;
		}
		return ((this.getDays() - aClock.getDays()) * MINUTESINADAY) + (this.getMinutes() - aClock.getMinutes());
	}
	
	@Override
	public String toString() {
		return 
				"day " + getDays() + ", " +
				(getMinutes() / 60) + " hours, " + 
				(getMinutes() % 60) + " minutes.";	
	}

	@Override
	public int compareTo(UnmodifiableClock clock) {
		if (clock == null) {
			throw new IllegalArgumentException();
		}
		if (clock.getDays() == this.getDays() && clock.getMinutes() == this.getMinutes()) {
			return 0;
		}
		if (isEarlierThan(clock)) {
			return -1;
		}
		return 1;
	}
	
	public UnmodifiableClock getUnmodifiableClockPlusExtraMinutes(int extra) {
		int days = getDays() + ((getMinutes() + extra) / MINUTESINADAY);
		int minutes = (getMinutes() + extra) % MINUTESINADAY;
		return new UnmodifiableClock(days, minutes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + days;
		result = prime * result + minutes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnmodifiableClock other = (UnmodifiableClock) obj;
		if (MINUTESINADAY != other.MINUTESINADAY)
			return false;
		if (days != other.days)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
	
}
