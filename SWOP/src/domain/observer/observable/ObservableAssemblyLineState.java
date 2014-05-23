package domain.observer.observable;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.observer.observers.AssemblyLineStateObserver;
/**
 * An interface describing every function needed to implement to be subscribed to a 
 * AssemblyLineStateObserver. 
 */
public interface ObservableAssemblyLineState {

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
	public void attachObserver(AssemblyLineStateObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications.
	 * 
	 * @param	observer
	 * 			The observer that needs to be detached
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachObserver(AssemblyLineStateObserver observer);
	
	/**
	 * Method that notifies all the subscribers when the state of an AssemblyLine changes and sends the both 
	 * the current and previous state to every subscriber.
	 * 
	 * @param	previousState
	 * 			The previous state of the AssemblyLine
	 * 
	 * @param	currentState
	 * 			The new state of the AssemblyLine
	 */
	public void updatAssemblyLineState(AssemblyLineState previousState, AssemblyLineState currentState);
}
