package domain.observer.observes;

import domain.clock.ImmutableClock;

/**
 * An interface used as a gateway for addressing complex objects and notifying 
 * them in case of a change in time by the observed Clock.
 */
public interface ObservesClock {

	/**
	 * Every observing object will be notified that the time has advanced.
	 * 
	 * @param	currentTime
	 *			The immutableClock that represents the newly current time
	 */
	public void advanceTime(ImmutableClock currentTime);
	
	/**
	 * Every observing object will be notified that a new day has started.
	 * 
	 * @param	newDay
	 * 			The immutableClock that represents the newly current time
	 */
	public void startNewDay(ImmutableClock newDay);
	
}
