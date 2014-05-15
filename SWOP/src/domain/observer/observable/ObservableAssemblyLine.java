package domain.observer.observable;

import domain.observer.observers.AssemblyLineObserver;
import domain.order.order.IOrder;
/**
 * An interface describing every function needed to implement to be subscribed to a 
 * AssemblyLineObserver. 
 */
public interface ObservableAssemblyLine {

	/**
	 * The observer will be added to the notify list and is subscribed for
	 * every notification.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void attachObserver(AssemblyLineObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachObserver(AssemblyLineObserver observer);
	
	/**
	 * Method that notifies all the subscribers when an order is completed and sends the current
	 * time to every subscriber.
	 */
	public void updateCompletedOrder(IOrder order);
}
