package domain.observer;

import java.util.ArrayList;

import domain.clock.UnmodifiableClock;

public class ClockObserver {
	
	private ArrayList<LogsClock> loggers;
	
	public ClockObserver() {
		this.loggers = new ArrayList<LogsClock>();
	}
	
	public void attachLogger(LogsClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	public void detachLogger(LogsClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/*
	 * Clock wordt geupdate
	 * Clock update ClockObserver
	 * ClockObserver update logger
	 */
	public void advanceTime(UnmodifiableClock currentTime) {
		for (LogsClock logger : loggers) {
			logger.advanceTime(currentTime);
		}
	}

	public void startNewDay(UnmodifiableClock newDay) {
		for (LogsClock logger : loggers) {
			logger.startNewDay(newDay);
		}
	}
	
	
}
