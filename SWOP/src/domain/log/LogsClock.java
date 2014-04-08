package domain.log;

import domain.clock.ImmutableClock;

public interface LogsClock {

	public void advanceTime(ImmutableClock currentTime);
	
	public void startNewDay();
	
}
