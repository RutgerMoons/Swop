package domain.observer.observes;

import domain.assembly.assemblyLine.AssemblyLineState;
/**
 * An interface used as a gateway for addressing complex objects and notifying 
 * them in case of the state of an AssemblyLine changes.
 */
public interface ObservesAssemblyLineState {

	/**
	 * Receives the previous and the current state of an AssemblyLine each time its state changes.
	 */
	public void updateAssemblylineState(AssemblyLineState previousState, AssemblyLineState currentState);
}
