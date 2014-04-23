package domain.log;

import java.util.List;

import domain.clock.UnmodifiableClock;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.observer.LogsAssemblyLine;
import domain.observer.LogsClock;
import domain.order.Delay;

/**
 * This object keeps track of completed orders and their delays. It also keeps track
 * of the amount of completed orders per day and provides the methods to calculate the mean and median
 * of all the accumulated data.
 */
public class Logger implements LogsClock, LogsAssemblyLine {

	private LogHistoryDays logHistoryDays;
	private LogHistoryDelays logHistoryDelays;
	private UnmodifiableClock currentTime;

	/**
	 * A new logger is constructed:
	 * 
	 * @param numberOfDaysOfDetailedHistory
	 * 			This number represents the degree of detail
	 * @param clockObserver
	 * 			The observer to which it subscribes for keeping track of time
	 * @param assemblyLineObserver
	 * 			The observer to which it subscribes for keeping track of completed orders
	 * 
	 * @throws IllegalArgumentException
	 * 			Exception is thrown when one of the observers is null
	 */
	public Logger(int numberOfDaysOfDetailedHistory, ClockObserver clockObserver, AssemblyLineObserver assemblyLineObserver) {
		if(clockObserver == null || assemblyLineObserver == null){
			throw new IllegalArgumentException();
		}
		this.logHistoryDays = new LogHistoryDays(numberOfDaysOfDetailedHistory);
		this.logHistoryDelays = new LogHistoryDelays(numberOfDaysOfDetailedHistory);
		assemblyLineObserver.attachLogger(this);
		clockObserver.attachLogger(this);
	}

	/**
	 * Method for updating the current time.
	 * 
	 * @param currentTime
	 * 			The new value for the current time
	 * 
	 * @throws IllegalArgumentException
	 * 			Exception is thrown when currentTime is null
	 */
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

	/**
	 * Method called on the start of a new day. The time is updated
	 * to the given clock and the helper object logHistoryDays is shifted.
	 */
	@Override
	public void startNewDay(UnmodifiableClock newDay) {
		this.advanceTime(newDay);
		this.logHistoryDays.shift();
	}

	/**
	 * The delay of the order is calculated and the helper objects are notified of the newly completed order
	 * and the associated delay.
	 * This method throws an IllegalArgumentException when the parameter is null.
	 */
	@Override
	public void updateCompletedOrder(UnmodifiableClock estimatedTimeOfCompletion) {
		if (estimatedTimeOfCompletion == null) {
			throw new IllegalArgumentException();
		}
		Delay delay = new Delay(estimatedTimeOfCompletion, this.currentTime);
		this.logHistoryDelays.addNewDelay(delay);
		this.logHistoryDays.incrementAmountOfCarsProducedToday();
	}

	/**
	 * Method for computing the median of completed orders in a day.
	 */
	public int medianDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return median(completeDays);
	}

	/**
	 * Method for computing the median of delays.
	 */
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

	/**
	 * Method for computing the average of completed orders in a day.
	 */
	public int averageDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return average(completeDays);
	}

	/**
	 * Method for computing the average of delays.
	 */
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
