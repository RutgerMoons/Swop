package domain.observer.observers;

import java.util.ArrayList;

import domain.observer.observes.ObservesAssemblyLine;
import domain.order.order.IOrder;

/**
 * A class representing an observer that notifies every subscriber when an order is completed by the AssemblyLine(s) to which 
 * this observer is attached.
 */
public class AssemblyLineObserver {

	private ArrayList<ObservesAssemblyLine> loggers;

	/**
	 * Creates a new AssemblyLineObserver and initializes it's internal data structures.
	 */
	public AssemblyLineObserver() {
		this.loggers = new ArrayList<ObservesAssemblyLine>();
	}

	/**
	 * This logger will be added to the notify list and is subscribed for every notification.
	 * 
	 * @param	logger
	 * 			An ObservesAssemblyLine who wants to subscribe
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void attachLogger(ObservesAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}

	/**
	 * This logger is no longer subscribed and will no longer be notified.
	 * 
	 * @param	logger
	 * 			An ObservesAssemblyLine who wants to unsubscribe
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachLogger(ObservesAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}

	/**
	 * Every subscribed object will be notified and receives the completed Order.
	 * 
	 * @param	order
	 * 			The order from which a job is completed
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the order is null
	 */
	public void updateCompletedOrder(IOrder order) {
		if(order==null){
			throw new IllegalArgumentException();
		}
		for (ObservesAssemblyLine logger : this.loggers) {
			logger.updateCompletedOrder(order);
		}
	}
}
