package domain.observer;

import domain.clock.ImmutableClock;
import domain.log.Logger;

public class ClockObserver extends Observer {
	
	public ClockObserver(Logger logger) {
		super(logger);
	}
	
	/*
	 * Clock wordt geupdate
	 * Clock update ClockObserver
	 * ClockObserver update logger
	 */
	public void advanceTime(ImmutableClock currentTime) {
		logger.advanceClock(currentTime);
	}

	public void startNewDay() {
		logger.startNewDay();
	}
	
	
}
