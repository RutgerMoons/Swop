package domain.observer.observes;

import domain.assembly.assemblyLine.AssemblyLineState;

public interface ObservesAssemblyLineState {

	/**
	 * Receives the previous and the current state of an AssemblyLine each time its state changes.
	 */
	public void updateAssemblylineState(AssemblyLineState previousState, AssemblyLineState currentState);
}
