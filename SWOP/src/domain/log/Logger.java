package domain.log;

import java.util.List;

import domain.clock.UnmodifiableClock;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.observer.LogsAssemblyLine;
import domain.observer.LogsClock;
import domain.order.Delay;

public class Logger implements LogsClock, LogsAssemblyLine {

	private LogHistoryDays logHistoryDays;
	private LogHistoryDelays logHistoryDelays;
	private UnmodifiableClock currentTime;

	public Logger(int numberOfDaysOfDetailedHistory, ClockObserver clockObserver, AssemblyLineObserver assemblyLineObserver) {
		this.logHistoryDays = new LogHistoryDays(numberOfDaysOfDetailedHistory);
		this.logHistoryDelays = new LogHistoryDelays(numberOfDaysOfDetailedHistory);
		assemblyLineObserver.attachLogger(this);
		clockObserver.attachLogger(this);
	}

	@Override
	public void advanceTime(UnmodifiableClock currentTime) {
		if (currentTime == null) {
			throw new IllegalArgumentException();
		}
		this.currentTime = currentTime;
	}

	public UnmodifiableClock getCurrentTime(){
		return this.currentTime;
	}
	
	@Override
	public void startNewDay(UnmodifiableClock newDay) {
		this.logHistoryDays.shift();
		this.advanceTime(newDay);
	}

	@Override
	public void updateCompletedOrder(UnmodifiableClock estimatedTimeOfCompletion) {
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
