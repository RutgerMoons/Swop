package domain.assembly.assemblyLine;

import domain.clock.ImmutableClock;
import domain.observer.observes.ObservesClock;

public class MaintenanceTimeManager implements ObservesClock{

	private final AssemblyLine line;
	private final ImmutableClock finishTime;

	public MaintenanceTimeManager(AssemblyLine line, ImmutableClock finishTime){
		this.line = line;
		ImmutableClock time = new ImmutableClock(finishTime.getDays(), finishTime.getMinutes() + 240);
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
	}
	
}
