package domain.observer;

import domain.clock.UnmodifiableClock;

public class AssemblyLineObserver {

	private LogsAssemblyLine logger;
	
	public AssemblyLineObserver(LogsAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		this.logger = logger;
	}
	
	public void updateCompletedOrder(UnmodifiableClock estimatedTimeOfOrder) {
		logger.updateCompletedOrder(estimatedTimeOfOrder);
	}
	
	
}
