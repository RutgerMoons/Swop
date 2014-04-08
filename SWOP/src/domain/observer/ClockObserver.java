package domain.observer;

import domain.clock.UnmodifiableClock;

public class ClockObserver {
	
	private LogsClock logger;
	
	public ClockObserver(LogsClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		this.logger = logger;
	}
	
	/*
	 * Clock wordt geupdate
	 * Clock update ClockObserver
	 * ClockObserver update logger
	 */
	public void advanceTime(UnmodifiableClock currentTime) {
		logger.advanceTime(currentTime);
	}

	public void startNewDay() {
		logger.startNewDay();
	}
	
	
}
