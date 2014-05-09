package domain.observer;

import java.util.ArrayList;

import domain.order.IOrder;

public class OrderBookObserver {

	private ArrayList<LogsOrderBook> loggers;
	
	/**
	 * Creates a new ClockObserver and initializes it's internal data structures
	 */
	public OrderBookObserver() {
		this.loggers = new ArrayList<>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification
	 */
	public void attachLogger(LogsOrderBook logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified
	 */
	public void detachLogger(LogsOrderBook logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified and receives the new order
	 */
	public void notifyNewOrder(IOrder order) {
		for (LogsOrderBook logger : loggers) {
			logger.processNewOrder(order);
		}
	}
}
