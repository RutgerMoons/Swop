package domain.observer.observers;

import java.util.ArrayList;

import domain.clock.ImmutableClock;
import domain.observer.observes.ObservesClock;

/**
 * A class representing an observer that notifies every subscriber when the observed Clock(s) to which 
 * this observer is attached change time.
 */
public class ClockObserver {
	
	private ArrayList<ObservesClock> loggers;
	
	/**
	 * Creates a new ClockObserver and initializes it's internal data structures.
	 */
	public ClockObserver() {
		this.loggers = new ArrayList<ObservesClock>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void attachLogger(ObservesClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachLogger(ObservesClock logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified and receives the current time.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when currentTime is null
	 */
	public void advanceTime(ImmutableClock currentTime) {
		if(currentTime==null){
			throw new IllegalArgumentException();
		}
		for (ObservesClock logger : loggers) {
			logger.advanceTime(currentTime);
		}
	}

	/**
	 * Every subscribed object will be notified that a new day has started and receives the current time.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when newDay is null
	 */
	public void startNewDay(ImmutableClock newDay) {
		if(newDay==null){
			throw new IllegalArgumentException();
		}
		for (ObservesClock logger : loggers) {
			logger.startNewDay(newDay);
		}
	}
	
	
}
