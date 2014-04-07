package domain.log;

import domain.clock.ImmutableClock;
import domain.order.Delay;

public class Logger {

	private LogHistoryDays logHistoryDays;
	private LogHistoryDelays logHistoryDelays;
	private ImmutableClock currentTime;

	public Logger(int numberOfDaysOfDetailedHistory) {
		this.logHistoryDays = new LogHistoryDays(numberOfDaysOfDetailedHistory);
		this.logHistoryDelays = new LogHistoryDelays(numberOfDaysOfDetailedHistory);
	}

	public void advanceClock(ImmutableClock currentTime) {
		if (currentTime == null) {
			throw new IllegalArgumentException();
		}
		this.currentTime = currentTime;
	}

	public void startNewDay() {
		this.logHistoryDays.shift();
	}

	public void updateCompletedOrder(ImmutableClock estimatedTimeOfCompletion) {
		if (estimatedTimeOfCompletion == null) {
			throw new IllegalArgumentException();
		}
		Delay delay = new Delay(estimatedTimeOfCompletion, this.currentTime);
		this.logHistoryDelays.addNewDelay(delay);
		this.logHistoryDays.incrementAmountOfCarsProducedToday();

	}

}
