package domain.observer.observers;

import java.util.ArrayList;

import domain.clock.ImmutableClock;
import domain.observer.observes.ObservesAssemblyLine;

/**
 * This object can attach and detach any object that implements the LogsAssemblyLine interface
 * Every attached object will be notified when an order completed by the assemblyline(s) to which 
 * this observer is attached
 */
public class AssemblyLineObserver {

	private ArrayList<ObservesAssemblyLine> loggers;
	
	/**
	 * Creates a new AssemblyLineObserver and initializes it's internal data structures
	 */
	public AssemblyLineObserver() {
		this.loggers = new ArrayList<ObservesAssemblyLine>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification
	 */
	public void attachLogger(ObservesAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified
	 */
	public void detachLogger(ObservesAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified and receives the estimated time of the completed order
	 */
	public void updateCompletedOrder(ImmutableClock estimatedTimeOfOrder) {
		for (ObservesAssemblyLine logger : this.loggers) {
			logger.updateCompletedOrder(estimatedTimeOfOrder);
		}
	}
}
