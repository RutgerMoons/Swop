package domain.observer;

import domain.clock.UnmodifiableClock;

/**
 * AssemblyLineObservers use this interface as a gateway for addressing complex objects and notifying 
 * them in case of a completed order by the observed assemblyLine
 */
public interface LogsAssemblyLine {

	public void updateCompletedOrder(UnmodifiableClock estimatedTimeOfOrder);
	
}
