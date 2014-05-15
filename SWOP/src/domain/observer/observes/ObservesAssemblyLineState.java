package domain.observer.observes;

import domain.assembly.assemblyLine.AssemblyLineState;

public interface ObservesAssemblyLineState {

	public void updateAssemblylineState(AssemblyLineState previousState, AssemblyLineState currentState);
}
