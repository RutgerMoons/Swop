package domain.observer.observes;

import domain.order.IOrder;

public interface ObservesOrderBook {
	
	/**
	 * Every observing object will be notified that a new order is placed and receives the placed order
	 */
	public void processNewOrder(IOrder order);
}
