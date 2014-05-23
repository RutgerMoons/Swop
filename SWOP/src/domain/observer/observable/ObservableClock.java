package domain.observer.observable;

import domain.observer.observers.ClockObserver;
/**
 * An interface describing every function needed to implement to be subscribed to a 
 * ClockObserver. 
 */
public interface ObservableClock {

	/**
	 * The observer will be added to the notify list and is subscribed for
	 * every notification.
	 * 
	 * @param	observer
	 * 			The observer that needs to be attached
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void attachObserver(ClockObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications.
	 * 
	 * @param	observer
	 * 			The observer that needs to be detached
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachObserver(ClockObserver observer);
	
	/**
	 * Method that notifies all the observers when the clock advances and sends the current
	 * time to every observer.
	 */
	public void notifyObserversAdvanceTime();
	
	/**
	 * Every observing object will be notified that a new day has started and receives the current time.
	 */
	public void notifyObserversStartNewDay();
}
