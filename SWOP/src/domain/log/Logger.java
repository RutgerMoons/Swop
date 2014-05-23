package domain.log;

import java.util.Collections;
import java.util.List;

import domain.clock.ImmutableClock;
import domain.observer.observes.ObservesAssemblyLine;
import domain.observer.observes.ObservesClock;
import domain.order.Delay;
import domain.order.order.IOrder;

/**
 * A class representing an object that keeps track of completed orders and their delays. 
 * It also keeps track of the amount of completed orders per day and the amount of delays. Moreover it
 * provides the methods to calculate the mean and median of all the accumulated data. 
 * Logger is subscribed on ObservesClock and on ObservesAssemblyLine and will be notified each time the Clock is 
 * changed and each time a Job is completed. 
 */
public class Logger implements ObservesClock, ObservesAssemblyLine {

	private LogHistoryDays logHistoryDays;
	private LogHistoryDelays logHistoryDelays;
	private ImmutableClock currentTime;

	/**
	 * A new Logger is constructed.
	 * 
	 * @param 	amountOfDetails
	 * 			This number represents the degree of detail
	 *
	 * @param	currentTime
	 * 			The current time
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Exception is thrown when amountOfDetails is smaller than 1
	 */
	public Logger(int amountOfDetails, ImmutableClock currentTime) {
		if(amountOfDetails < 1 || currentTime == null){
			throw new IllegalArgumentException();
		}
		this.logHistoryDays = new LogHistoryDays(amountOfDetails);
		this.logHistoryDelays = new LogHistoryDelays(amountOfDetails);
		this.currentTime = currentTime;
	}

	/**
	 * Method for updating the current time.
	 * 
	 * @param 	currentTime
	 * 			The new value for the current time
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Exception is thrown when currentTime is null
	 */
	@Override
	public void advanceTime(ImmutableClock currentTime) {
		if (currentTime == null) {
			throw new IllegalArgumentException();
		}
		this.currentTime = currentTime;
	}

	/**
	 * Retrieves the current time of the logger.  
	 */
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
	 * 
	 * @param	order
	 * 			The order of the Job that just has been completed
	 * 
	 * @throws 	IllegalArgumentException 
	 * 			Thrown when the parameter is null.
	 */
	@Override
	public void updateCompletedOrder(IOrder order) {
		if (order == null) {
			throw new IllegalArgumentException();
		}
		ImmutableClock estimatedTimeOfCompletion = order.getEstimatedTime();
		Delay delay = new Delay(estimatedTimeOfCompletion, this.currentTime);
		this.logHistoryDelays.addNewDelay(delay);
		this.logHistoryDays.incrementAmountOfVehiclesProducedToday();
	}

	/**
	 * Method for computing the median of completed Orders in a day.
	 */
	public int medianDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return median(completeDays);
	}

	/**
	 * Method for computing the median of Delays.
	 */
	public int medianDelays() {
		List<Integer> completeDelays = logHistoryDelays.getCompleteHistory();
		return median(completeDelays);
	}

	/**
	 * Method for computing the median of a collection of numbers.
	 * 
	 * @param 	list
	 * 			The list of numbers of which the median will be calculated.
	 * 
	 */
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
	 * Method for computing the average of completed Orders in a day.
	 */
	public int averageDays() {
		List<Integer> completeDays = logHistoryDays.getCompleteHistory();
		return average(completeDays);
	}

	/**
	 * Method for computing the average of Delays.
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

	/**
	 * Returns an unmodifiable list of all the detailed days.  
	 */
	public List<Integer> getDetailedDays() {
		return Collections.unmodifiableList(logHistoryDays.getHistory());
	}

	/**
	 * Returns an unmodifiable list of all the detailed Delays. 
	 */
	public List<Delay> getDetailedDelays() {
		return Collections.unmodifiableList(logHistoryDelays.getHistory());
	}
}