package domain.observer.observers;

import java.util.ArrayList;

import domain.observer.observes.ObservesOrderBook;
import domain.order.order.IOrder;

/**
 * A class representing an observer that notifies every subscriber when a new Order arrives. 
 */
public class OrderBookObserver {

	private ArrayList<ObservesOrderBook> loggers;
	
	/**
	 * Creates a new ClockObserver and initializes it's internal data structures.
	 */
	public OrderBookObserver() {
		this.loggers = new ArrayList<>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification.
	 * 
	 * @param	logger
	 * 			An ObservesOrderBook who wants to subscribe
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void attachLogger(ObservesOrderBook logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified.
	 * 
	 * @param	logger
	 * 			An ObservesOrderBook who wants to unsubscribe
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachLogger(ObservesOrderBook logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified and receives the new Order.
	 * 
	 * @param	order
	 * 			The newly made Order
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the order is null
	 */
	public void notifyNewOrder(IOrder order) {
		if(order==null){
			throw new IllegalArgumentException();
		}
		for (ObservesOrderBook logger : loggers) {
			logger.processNewOrder(order);
		}
	}
}
