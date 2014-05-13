package domain.log;

import java.util.List;

import domain.clock.ImmutableClock;
import domain.observer.observes.ObservesAssemblyLine;
import domain.observer.observes.ObservesClock;
import domain.order.Delay;
import domain.order.IOrder;

/**
 * This object keeps track of completed orders and their delays. It also keeps track
 * of the amount of completed orders per day and provides the methods to calculate the mean and median
 * of all the accumulated data.
 */
public class Logger implements ObservesClock, ObservesAssemblyLine {

	private LogHistoryDays logHistoryDays;
	private LogHistoryDelays logHistoryDelays;
	private ImmutableClock currentTime;

	/**
	 * A new logger is constructed:
	 * 
	 * @param 	numberOfDaysOfDetailedHistory
	 * 				This number represents the degree of detail
	 *
	 * @throws 	IllegalArgumentException
	 * 				Exception is thrown when numbersOfDaysOfDetailedHistory is smaller than 1
	 */
	public Logger(int numberOfDaysOfDetailedHistory) {
		if(numberOfDaysOfDetailedHistory < 1 ){
			throw new IllegalArgumentException();
		}
		this.logHistoryDays = new LogHistoryDays(numberOfDaysOfDetailedHistory);
		this.logHistoryDelays = new LogHistoryDelays(numberOfDaysOfDetailedHistory);
	}

	/**
	 * Method for updating the current time.
	 * 
	 * @param 	currentTime
	 * 				The new value for the current time
	 * 
	 * @throws 	IllegalArgumentException
	 * 				Exception is thrown when currentTime is null
	 */
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

	/**
	 * Method called on the start of a new day. The time is updated
	 * to the given clock and the helper object logHistoryDays is shifted.
	 */
	@Override
	public void startNewDay(ImmutableClock newDay) {
		this.advanceTime(newDay);
		this.logHistoryDays.shift();
	}

	/**
	 * The delay of the order is calculated and the helper objects are notified of the newly completed order
	 * and the associated delay.
	 * This method throws an IllegalArgumentException when the parameter is null.
	 */
	@Override
	public void updateCompletedOrder(IOrder order) {
		ImmutableClock estimatedTimeOfCompletion = order.getEstimatedTime();
		if (estimatedTimeOfCompletion == null) {
			throw new IllegalArgumentException();
		}
		Delay delay = new Delay(estimatedTimeOfCompletion, this.currentTime);
		this.logHistoryDelays.addNewDelay(delay);
		this.logHistoryDays.incrementAmountOfCarsProducedToday();
	}

	/**
	 * Method for computing the median of completed orders in a day
	 */
	public int medianDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return median(completeDays);
	}

	/**
	 * Method for computing the median of delays
	 */
	public int medianDelays() {
		List<Integer> completeDelays = logHistoryDelays.getCompleteHistory();
		return median(completeDelays);
	}

	private int median(List<Integer> list) {
		if(list.size() == 0){
			return 0;
		}
		
		if (list.size() % 2 == 1) {
			return list.get(list.size() / 2);
		} else {
			return (list.get(list.size() / 2) + list.get((list.size() / 2) - 1)) / 2;
		}
	}

	/**
	 * Method for computing the average of completed orders in a day
	 */
	public int averageDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return average(completeDays);
	}

	/**
	 * Method for computing the average of delays
	 */
	public int averageDelays() {
		List<Integer> completeDelays = logHistoryDelays.getCompleteHistory();
		return average(completeDelays);
	}

	private int average(List<Integer> list) {
		if(list.size() == 0){
			return 0;
		}
		
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