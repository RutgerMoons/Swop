package domain.observer;

import domain.clock.UnmodifiableClock;
/**
 * This class needs to be implemented by classes who want to use these functions.
 *
 */
public interface LogsClock {

	public void advanceTime(UnmodifiableClock currentTime);
	
	public void startNewDay(UnmodifiableClock newDay);
	
}
