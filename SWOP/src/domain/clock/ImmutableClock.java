package domain.clock;

/**
 * A class representing a snapshot of a clock at some point in time.
 */
public class ImmutableClock implements Comparable<ImmutableClock> {

	private final int days;
	private final int minutes;
	private final int MINUTESINADAY = 1440;
	
	/**
	 * Create a snapshot of a Clock, given the amount of days and minutes on this Clock.
	 * 
	 * @param 	days
	 * 			The amount of days that has passed.
	 * 
	 * @param 	minutes
	 * 			The amount of minutes that has passed in this current day.s
	 */
	public ImmutableClock(int days, int minutes) {
		this.days = days;
		this.minutes = minutes;
	}
	
	/**
	 * Returns the amount of days.
	 */
	public int getDays() {
		return days;
	}

	/**
	 * Returns the amount of minutes.
	 */
	public int getMinutes() {
		return minutes;
	}
	
	/**
	 * @return True if and only if this ImmutableClock has the earliest time stamp 
	 * 		   or when both ImmutableClocks have the same time stamp
	 */
	public boolean isEarlierThan(ImmutableClock aClock) {
		if (aClock == null) {
			throw new IllegalArgumentException();
		}
		return this.getDays() < aClock.getDays() || 
				(this.getDays() == aClock.getDays() && this.getMinutes() <= aClock.getMinutes());
	}
	
	/**
	 * Returns the difference in time of this clock and the given ImmutableClock in minutes.
	 * 
	 * @return 0 if the given ImmutableClock is not earlier than this ImmutableClock
	 */
	public int minus(ImmutableClock aClock) throws IllegalArgumentException {
		if (this.isEarlierThan(aClock)) {
			return 0;
		}
		return ((this.getDays() - aClock.getDays()) * MINUTESINADAY) + (this.getMinutes() - aClock.getMinutes());
	}
	
	/**
	 * Returns the entire snapshot in minutes. This means the amount of days are converted to a certain amount of minutes
	 * and the amount of minutes in getMinutes is added to this amount. This summation is returned.
	 */
	public int getTotalInMinutes(){
		return this.getDays()*this.MINUTESINADAY + this.getMinutes();
	}

	/**
	 * Constructs a new ImmutableClock by adding to this immutableClock a certain amount of minutes.
	 * 
	 * @param 	extra
	 * 			The amount of minutes that will be added
	 * 
	 * @return 	Returns the newly constructed ImmutableClock
	 */
	public ImmutableClock getImmutableClockPlusExtraMinutes(int extra) {
		int days = getDays() + ((getMinutes() + extra) / MINUTESINADAY);
		int minutes = (getMinutes() + extra) % MINUTESINADAY;
		return new ImmutableClock(days, minutes);
	}
	
	@Override
	public String toString() {
		return 
				"day " + getDays() + ", " +
				(getMinutes() / 60) + " hours, " + 
				(getMinutes() % 60) + " minutes.";	
	}

	@Override
	public int compareTo(ImmutableClock clock) {
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
		ImmutableClock other = (ImmutableClock) obj;
		if (days != other.days)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}