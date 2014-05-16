package domain.observer.observers;

import java.util.ArrayList;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.observer.observes.ObservesAssemblyLineState;

/**
 * A class representing an observer that notifies every subscriber when the state of an AssemblyLine changes. 
 *
 */
public class AssemblyLineStateObserver {

private ArrayList<ObservesAssemblyLineState> loggers;
	
	/**
	 * Creates a new AssemblyLineObserver and initializes it's internal data structures.
	 */
	public AssemblyLineStateObserver() {
		this.loggers = new ArrayList<ObservesAssemblyLineState>();
	}
	
	/**
	 * This logger will be added to the notify list and is subscribed for every notification.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void attachLogger(ObservesAssemblyLineState logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	/**
	 * This logger is no longer subscribed and will no longer be notified.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void detachLogger(ObservesAssemblyLineState logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	/**
	 * Every subscribed object will be notified ad receives the current and previous state of the AssemblyLine.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when one or both of the states is null
	 */
	public void updateAssemblyLineState(AssemblyLineState previousState, AssemblyLineState currentState) {
		if(previousState==null || currentState==null){
			throw new IllegalArgumentException();
		}
		for (ObservesAssemblyLineState logger : this.loggers) {
			logger.updateAssemblylineState(previousState, currentState);
		}
	}
}
