package domain.log;

import domain.clock.ImmutableClock;

public interface LogsAssemblyLine {

	public void updateCompletedOrder(ImmutableClock estimatedTimeOfOrder);
	
}
