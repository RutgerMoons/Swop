package domain.observer.observable;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.observer.observers.AssemblyLineStateObserver;

public interface ObservableAssemblyLineState {

	/**
	 * The observer will be added to the notify list and is subscribed for
	 * every notification.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void attachObserver(AssemblyLineStateObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void detachObserver(AssemblyLineStateObserver observer);
	
	/**
	 * Method that notifies all the subscribers when an order is completed and sends the current
	 * time to every subscriber.
	 */
	public void updatAssemblyLineState(AssemblyLineState previousState, AssemblyLineState currentState);
}
