package domain.observer.observers;

import java.util.ArrayList;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.observer.observes.ObservesAssemblyLineState;

public class AssemblyLineStateObserver {

private ArrayList<ObservesAssemblyLineState> loggers;
	
	/**
	 * Creates a new AssemblyLineObserver and initializes it's internal data structures
	 */
	public AssemblyLineStateObserver() {
		this.loggers = new ArrayList<ObservesAssemblyLineState>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification
	 */
	public void attachLogger(ObservesAssemblyLineState logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified
	 */
	public void detachLogger(ObservesAssemblyLineState logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified and receives the completed order
	 */
	public void updateAssemblyLineState(AssemblyLineState previousState, AssemblyLineState currentState) {
		for (ObservesAssemblyLineState logger : this.loggers) {
			logger.updateAssemblylineState(previousState, currentState);
		}
	}
}
