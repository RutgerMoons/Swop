package domain.observer.observes;

import domain.order.order.IOrder;
/**
 * An interface used as a gateway for addressing complex objects and notifying 
 * them in case of a new Order is placed. 
 */
public interface ObservesOrderBook {
	
	/**
	 * Every observing object will be notified that a new Order is placed and receives the placed Order.
	 */
	public void processNewOrder(IOrder order);
}
