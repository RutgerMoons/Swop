package domain.clock;

import java.util.ArrayList;

import domain.observer.observable.ObservableClock;
import domain.observer.observers.ClockObserver;

/**
 * A class representing a clock that stores the current time (in minutes) and the current day.
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
	 * Get the amount of minutes of this day (in minutes).
	 */
	public int getMinutes(){
		return minutes;
	}

	private void setMinutes(int min) {
		this.minutes = min % MINUTESINADAY;
	}
	
	/**
	 * Advance the clock.
	 * 
	 * @param 	elapsedTime
	 * 			An integer that represents how much the clock has to be advanced, expressed in minutes
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown if elapsedTime is smaller than zero.
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
	 * 
	 * @return  An integer that represents the current day. The 'current day' is interpreted as 
	 * 			how many days the program has been running. (first day == 0)
	 */
	public int getDays() {
		return this.days;
	}
	
	/**
	 * @post The amount of days is incremented with one.
	 */
	private void incrementDay() {
		this.days++;
	}
	
	/**
	 * If it's before midnight, the day attribute will be incremented with one and
	 * the minutes will be set to MINUTESSTARTOFDAY.
	 */
	public void startNewDay() {
		incrementDay();
		setMinutes(MINUTESSTARTOFDAY);
		notifyObserversStartNewDay();
	}
	
	/**
	 * Returns an immutable snapshot of this clock.
	 */
	public ImmutableClock getImmutableClock() {
		return new ImmutableClock(getDays(), getMinutes());
	}
	
	/**
	 * Add an observer to this clock's observers;
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