package domain.observer;

import domain.clock.ImmutableClock;
import domain.log.LogsAssemblyLine;

public class AssemblyLineObserver {

	private LogsAssemblyLine logger;
	
	public AssemblyLineObserver(LogsAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		this.logger = logger;
	}
	
	public void updateCompletedOrder(ImmutableClock estimatedTimeOfOrder) {
		logger.updateCompletedOrder(estimatedTimeOfOrder);
	}
	
	
}
