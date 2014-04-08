package domain.log;

import domain.clock.UnmodifiableClock;

public interface LogsAssemblyLine {

	public void updateCompletedOrder(UnmodifiableClock estimatedTimeOfOrder);
	
}
