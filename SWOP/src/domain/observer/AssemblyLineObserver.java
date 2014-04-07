package domain.observer;

import domain.clock.ImmutableClock;
import domain.log.Logger;

public class AssemblyLineObserver extends Observer {

	public AssemblyLineObserver(Logger logger) {
		super(logger);
	}
	
	public void updateCompletedOrder(ImmutableClock estimatedTimeOfOrder) {
		logger.updateCompletedOrder(estimatedTimeOfOrder);
	}
	
	
}
