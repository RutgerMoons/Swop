package domain.observer;

import domain.clock.ImmutableClock;

/**
 * ClockObservers use this interface as a gateway for addressing complex objects and notifying 
 * them in case of a change in time by the observed clock
 */
public interface LogsClock {

	public void advanceTime(ImmutableClock currentTime);
	
	public void startNewDay(ImmutableClock newDay);
	
}
