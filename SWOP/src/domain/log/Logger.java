package domain.log;

import java.util.List;

import domain.clock.ImmutableClock;
import domain.order.Delay;

public class Logger implements LogsClock, LogsAssemblyLine {

	private LogHistoryDays logHistoryDays;
	private LogHistoryDelays logHistoryDelays;
	private ImmutableClock currentTime;

	public Logger(int numberOfDaysOfDetailedHistory) {
		this.logHistoryDays = new LogHistoryDays(numberOfDaysOfDetailedHistory);
		this.logHistoryDelays = new LogHistoryDelays(numberOfDaysOfDetailedHistory);
	}

	@Override
	public void advanceTime(ImmutableClock currentTime) {
		if (currentTime == null) {
			throw new IllegalArgumentException();
		}
		this.currentTime = currentTime;
	}

	public ImmutableClock getCurrentTime(){
		return this.currentTime;
	}
	
	@Override
	public void startNewDay() {
		this.logHistoryDays.shift();
	}

	@Override
	public void updateCompletedOrder(ImmutableClock estimatedTimeOfCompletion) {
		if (estimatedTimeOfCompletion == null) {
			throw new IllegalArgumentException();
		}
		Delay delay = new Delay(estimatedTimeOfCompletion, this.currentTime);
		this.logHistoryDelays.addNewDelay(delay);
		this.logHistoryDays.incrementAmountOfCarsProducedToday();
	}
	
	public int medianDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return median(completeDays);
	}
	
	public int medianDelays() {
		List<Integer> completeDelays = logHistoryDelays.getCompleteHistory();
		return median(completeDelays);
	}
	
	private int median(List<Integer> list) {
		if (list.size() % 2 == 1) {
			return list.get(list.size() / 2);
		} else {
			return (list.get(list.size() / 2) + list.get((list.size() / 2) - 1)) / 2;
		}
	}
	
	public int averageDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return average(completeDays);
	}
	
	public int averageDelays() {
		List<Integer> completeDelays = logHistoryDelays.getCompleteHistory();
		return average(completeDelays);
	}
	
	private int average(List<Integer> list) {
		int sum = 0;
		for (Integer i : list) {
			sum += i;
		}
		return sum/list.size();
	}
	
	public List<Integer> getDetailedDays() {
		return logHistoryDays.getHistory();
	}
	
	public List<Delay> getDetailedDelays() {
		return logHistoryDelays.getHistory();
	}

}
