package domain.clock;

import java.util.ArrayList;

import domain.observer.observable.ObservableClock;
import domain.observer.observers.ClockObserver;

/**
 * A class representing a clock that stores the current time (in minutes) and the current day.
 */
public class Clock implements ObservableClock {
	
	/**
	 * This represents the amount of minutes in a day.
	 */
	public static final int MINUTESINADAY = 1440;
	private final int MINUTESSTARTOFDAY;
	private int minutes;
	private int days;
	private ArrayList<ClockObserver> observers;
	
	/**
	 * Construct a new Clock.
	 * 
	 * @param 	minutesStartOfDay
	 * 			Each working day starts at this point of time.
	 */
	public Clock(int minutesStartOfDay) {
		observers = new ArrayList<ClockObserver>();
		if (minutesStartOfDay < 0 || minutesStartOfDay >= MINUTESINADAY) {
			throw new IllegalArgumentException();
		}
		this.MINUTESSTARTOFDAY = minutesStartOfDay;
	}
	
	/**
	 * Get the amount of minutes of this day.
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
	 * 			Thrown when elapsedTime is lesser than zero.
	 */
	public void advanceTime(int elapsedTime){
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
	
	@Override
	public void attachObserver(ClockObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.add(observer);
	}
	
	@Override
	public void detachObserver(ClockObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.remove(observer);
	}
	
	@Override
	public void notifyObserversAdvanceTime() {
		ImmutableClock currentTime = getImmutableClock();
		for (ClockObserver observer : observers) {
			observer.advanceTime(currentTime);
		}
	}
	
	@Override
	public void notifyObserversStartNewDay() {
		for (ClockObserver observer : observers) {
			observer.startNewDay(getImmutableClock());
		}
	}
}