package domain.observer;

import domain.order.IOrder;

public interface ObservableOrderBook {

	/**
	 * The observer will be added to the notify list and is subscribed for
	 * every notification.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void attachObserver(OrderBookObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void detachObserver(OrderBookObserver observer);
	
	/**
	 * Method that notifies all the subscribers when an order is placed and sends the placed
	 * order to every subscriber.
	 */
	public void updatePlacedOrder(IOrder order);
}