package domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class keeps track of all the completed orders and offers a detailed view for 
 * a certain amount of latest days. 
 */
public class LogHistoryDays extends LogHistory {
	
	private int carsProducedToday;
	private ArrayList<Integer> history;
	private ArrayList<Integer> completeHistory;
	
	/**
	 * Constructs a new instance with a degree of detail
	 * 
	 * @param 	numberOfDetails
	 * 				Amount of detailed delays to keep track off
	 */
	public LogHistoryDays(int numberOfDetails) {
		super(numberOfDetails);
		history = new ArrayList<Integer>();
		completeHistory = new ArrayList<Integer>();
	}

	/**
	 * Add the latest detailed day at the back of the history queue
	 * and remove at the front if the queue gets too long.  
	 */
	@Override
	public void shift() {
		history.add(carsProducedToday);
		this.carsProducedToday = 0;
		if (history.size() > numberOfDetails) {
			completeHistory.add(history.remove(0));
		}
	}
	
	/**
	 * The amount of cars produced to day is incremented with one
	 */
	public void incrementAmountOfCarsProducedToday() {
		carsProducedToday++;
	}

	/**
	 * Returns an unmodifiable list of the complete history.
	 */
	@Override
	public List<Integer> getCompleteHistory() {
		ArrayList<Integer> complete = new ArrayList<>();
		for (Integer i : history) {
			complete.add(i);
		}
		for (Integer i : completeHistory) {
			complete.add(i);
		}
		Collections.sort(complete);
		return Collections.unmodifiableList(complete);
	}
	
	/**
	 * Returns an unmodifiable list of the detailed history.
	 */
	public List<Integer> getHistory() {
		return Collections.unmodifiableList(history);
	}

}