package domain.observer;

import java.util.ArrayList;

import domain.clock.UnmodifiableClock;

/**
 * This object can attach and detach any object that implements the LogsClock interface
 * Every attached object will be notified when the observed Clock(s) to which 
 * this observer is attached change time
 */
public class ClockObserver {
	
	private ArrayList<LogsClock> loggers;
	
	/**
	 * Creates a new ClockObserver and initializes it's internal data structures
	 */
	public ClockObserver() {
		this.loggers = new ArrayList<LogsClock>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification
	 */
	public void attachLogger(LogsClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified
	 */
	public void detachLogger(LogsClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified and receives the current time
	 */
	public void advanceTime(UnmodifiableClock currentTime) {
		for (LogsClock logger : loggers) {
			logger.advanceTime(currentTime);
		}
	}

	/**
	 * Every subscribed object will be notified that a new day has started and receives the current time
	 */
	public void startNewDay(UnmodifiableClock newDay) {
		for (LogsClock logger : loggers) {
			logger.startNewDay(newDay);
		}
	}
	
	
}
