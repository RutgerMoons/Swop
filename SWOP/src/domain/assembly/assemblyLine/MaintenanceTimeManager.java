package domain.assembly.assemblyLine;

import domain.clock.ImmutableClock;
import domain.observer.observes.ObservesClock;
/**
 * A class representing the manager of turning an AssemblyLine back on when 4 hours have passed.
 * It's subscribed
 */
public class MaintenanceTimeManager implements ObservesClock{

	private final AssemblyLine line;
	private final ImmutableClock finishTime;
	private final int MAX_MAINTENANCE_TIME = 240;

	public MaintenanceTimeManager(AssemblyLine line, ImmutableClock finishTime){
		this.line = line;
		ImmutableClock time = new ImmutableClock(finishTime.getDays(), finishTime.getMinutes() + this.MAX_MAINTENANCE_TIME);
		this.finishTime = time;
	}

	@Override
	public void advanceTime(ImmutableClock currentTime) {
		if(finishTime.isEarlierThan(currentTime)){
			line.setState(AssemblyLineState.OPERATIONAL);
		}
	}

	@Override
	public void startNewDay(ImmutableClock newDay) {
		if(finishTime.isEarlierThan(newDay)){
			line.setState(AssemblyLineState.OPERATIONAL);
		}
	}
	
}
