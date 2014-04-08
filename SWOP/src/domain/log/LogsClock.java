package domain.log;

import domain.clock.UnmodifiableClock;

public interface LogsClock {

	public void advanceTime(UnmodifiableClock currentTime);
	
	public void startNewDay();
	
}
