package domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.order.Delay;

/**
 * A class representing to keeps track of all the Delays and offers a detailed view for 
 * a certain amount of latest Delays. 
 */
public class LogHistoryDelays extends LogHistory {

	private Delay latestDelay;
	private ArrayList<Delay> history;
	private ArrayList<Integer> completeHistory;
	
	/**
	 * Constructs a new instance with a given degree of detail.
	 * 
	 * @param 	numberOfDetails
	 * 			Amount of detailed Delays to keep track off
	 */
	public LogHistoryDelays(int numberOfDetails) {
		super(numberOfDetails);
		this.history = new ArrayList<Delay>();
		this.completeHistory = new ArrayList<Integer>();
	}
	
	/**
	 * Add the latest detailed Delay at the back of the history queue
	 * and remove at the front if the queue gets too long.  
	 */
	@Override
	public void shift() {
		this.history.add(this.latestDelay);
		if (this.history.size() > this.numberOfDetails) {
			this.completeHistory.add(this.history.remove(0).getDelay());
		}
	}

	@Override
	public List<Integer> getCompleteHistory() {
		ArrayList<Integer> complete = new ArrayList<>();
		for (Delay d : this.history) {
			complete.add(d.getDelay());
		}
		for (Integer i : completeHistory) {
			complete.add(i);
		}
		Collections.sort(complete);
		return Collections.unmodifiableList(complete);
	}
	
	/**
	 * Receives the newest Delay and adds this to the history.
	 * 
	 * @param 	newDelay
	 * 			The Delay to be added
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the given Delay is null
	 */
	public void addNewDelay(Delay newDelay) {
		if (newDelay == null) {
			throw new IllegalArgumentException();
		}
		if (newDelay.getDelay() != 0) {
			this.latestDelay = newDelay;
			shift();
		}
	}
	
	/**
	 * Returns an unmodifiable list of the detailed history.
	 */
	public List<Delay> getHistory() {
		return Collections.unmodifiableList(this.history);
	}
}