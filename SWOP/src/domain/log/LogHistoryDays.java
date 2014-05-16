package domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Class representing  to keeps track of all the completed Orders and offers a detailed view for 
 * a certain amount of latest days. 
 */
public class LogHistoryDays extends LogHistory {
	
	private int vehiclesProducedToday;
	private ArrayList<Integer> history;
	private ArrayList<Integer> completeHistory;
	
	/**
	 * Constructs a new instance with a degree of detail.
	 * 
	 * @param 	numberOfDetails
	 * 			Amount of detailed days to keep track off
	 */
	public LogHistoryDays(int numberOfDetails) {
		super(numberOfDetails);
		this.history = new ArrayList<Integer>();
		this.completeHistory = new ArrayList<Integer>();
	}

	/**
	 * Add the latest detailed day at the back of the history queue
	 * and remove at the front if the queue gets too long.  
	 */
	@Override
	public void shift() {
		this.history.add(this.vehiclesProducedToday);
		this.vehiclesProducedToday = 0;
		if (this.history.size() > this.numberOfDetails) {
			completeHistory.add(this.history.remove(0));
		}
	}
	
	/**
	 * The amount of vehicle produced in a day is incremented with one
	 */
	public void incrementAmountOfVehiclesProducedToday() {
		this.vehiclesProducedToday++;
	}

	/**
	 * Returns an unmodifiable list of the complete history.
	 */
	@Override
	public List<Integer> getCompleteHistory() {
		ArrayList<Integer> complete = new ArrayList<>();
		for (Integer i : this.history) {
			complete.add(i);
		}
		for (Integer i : this.completeHistory) {
			complete.add(i);
		}
		Collections.sort(complete);
		return Collections.unmodifiableList(complete);
	}
	
	/**
	 * Returns an unmodifiable list of the detailed history.
	 */
	public List<Integer> getHistory() {
		return Collections.unmodifiableList(this.history);
	}
}