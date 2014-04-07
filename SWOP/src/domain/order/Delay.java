package domain.order;

import domain.clock.ImmutableClock;

public class Delay {
	
	private final ImmutableClock estimated;
	private final ImmutableClock completed;
	private final int delay;
	
	public Delay(ImmutableClock estimated, ImmutableClock completed) {
		if (estimated == null || completed == null) {
			throw new IllegalArgumentException();
		}
		this.estimated = estimated;
		this.completed = completed;
		delay = calculateDelay();
	}
	
	private int calculateDelay() {
		return this.completed.minus(this.estimated);
	}
	
	public int getDelay() {
		return delay;
	}
	
	/**
	 * returns the delay and the day on which it occurred as:
	 * <DAY>: <DELAY>
	 */
	@Override
	public String toString() {
		return this.completed.getDays() + ": " + this.getDelay();
		
	}
}
