package domain.observer.observes;

import domain.clock.ImmutableClock;

/**
 * ClockObservers use this interface as a gateway for addressing complex objects and notifying 
 * them in case of a change in time by the observed clock
 */
public interface ObservesClock {

	public void advanceTime(ImmutableClock currentTime);
	
	public void startNewDay(ImmutableClock newDay);
	
}
