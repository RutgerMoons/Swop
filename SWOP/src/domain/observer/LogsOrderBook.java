package domain.observer;

import domain.order.IOrder;

public interface LogsOrderBook {
	
	/**
	 * Every observing object will be notified that a new order is placed and receives the placed order
	 */
	public void processNewOrder(IOrder order);
}
