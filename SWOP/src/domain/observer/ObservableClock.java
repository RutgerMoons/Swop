package domain.observer;


public interface ObservableClock {

	/**
	 * The observer will be added to the notify list and is subscribed for
	 * every notification
	 * 
	 * @throws	IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void attachObserver(ClockObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications
	 * 
	 * @throws	IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void detachObserver(ClockObserver observer);
	
	/**
	 * Method that notifies all the observers when the clock advances and sends the current
	 * time to every observer
	 */
	public void notifyObserversAdvanceTime();
	
	/**
	 * Every observing object will be notified that a new day has started and receives the current time
	 */
	public void notifyObserversStartNewDay();
}
