package domain.clock;

import java.util.ArrayList;

import domain.observer.observable.ObservableClock;
import domain.observer.observers.ClockObserver;

/**
 * Represents a clock that stores the current time (in minutes) and the current day.
 */
public class Clock implements ObservableClock {
	
	public static final int MINUTESINADAY = 1440;
	private final int MINUTESSTARTOFDAY= 360;
	private int minutes;
	private int days;
	private ArrayList<ClockObserver> observers;
	
	public Clock() {
		observers = new ArrayList<ClockObserver>();
	}
	
	/**
	 * Get the current time (in minutes).
	 * @return
	 * 			An integer that represents the current time, expressed in minutes.
	 */
	public int getMinutes(){
		return minutes;
	}
	
	private void setMinutes(int min) {
		this.minutes = min % MINUTESINADAY;
	}
	
	/**
	 * Advance the clock.
	 * @param 	elapsedTime
	 * 				An integer that represents how much the clock has to be advanced, expressed in minutes.
	 * @throws 	IllegalArgumentException
	 * 				If elapsedTime<0
	 */
	public void advanceTime(int elapsedTime) throws IllegalArgumentException{
		if (elapsedTime < 0) {
			throw new IllegalArgumentException("argument can't be negative");
		}
		else if (this.minutes + elapsedTime > MINUTESINADAY){
			setMinutes(this.minutes + elapsedTime);
			incrementDay();
		}
		else {
			setMinutes(this.minutes + elapsedTime);
		}
		notifyObserversAdvanceTime();
	}

	/**
	 * Get the current day.
	 * @return
	 * 			An integer that represents the current day. 
	 * 			The 'current day' is interpreted as how many days the program has been running. (first day == 0)
	 */
	public int getDays() {
		return this.days;
	}
	
	/**
	 * the amount of days is incremented with one
	 */
	private void incrementDay() {
		this.days++;
	}
	
	/**
	 * if it is before midnight increment the day with one
	 * set the minutes to MINUTESSTARTOFDAY
	 */
	public void startNewDay() {
		incrementDay();
		setMinutes(MINUTESSTARTOFDAY);
		notifyObserversStartNewDay();
	}
	
	public ImmutableClock getImmutableClock() {
		return new ImmutableClock(getDays(), getMinutes());
	}
	
	/**
	 * Add an observer to this clock's observers
	 */
	@Override
	public void attachObserver(ClockObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.add(observer);
	}
	
	/**
	 * Remove an observer from this clock's observers.
	 */
	@Override
	public void detachObserver(ClockObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.remove(observer);
	}
	
	/**
	 * Notify the observers that the time has advanced.
	 */
	@Override
	public void notifyObserversAdvanceTime() {
		ImmutableClock currentTime = getImmutableClock();
		for (ClockObserver observer : observers) {
			observer.advanceTime(currentTime);
		}
	}
	
	/**
	 * Notify the observers that a new day has started.
	 */
	@Override
	public void notifyObserversStartNewDay() {
		for (ClockObserver observer : observers) {
			observer.startNewDay(getImmutableClock());
		}
	}


}
