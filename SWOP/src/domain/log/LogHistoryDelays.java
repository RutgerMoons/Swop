package domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.order.Delay;

/**
 * This class keeps track of all the delays and offers a detailed view for 
 * a certain amount of latest delays. 
 */
public class LogHistoryDelays extends LogHistory {

	private Delay latestDelay;
	private ArrayList<Delay> history;
	private ArrayList<Integer> completeHistory;
	
	/**
	 * Constructs a new instance with a degree of detail
	 * 
	 * @param numberOfDetails
	 * 		Amount of detailed delays to keep track off
	 */
	public LogHistoryDelays(int numberOfDetails) {
		super(numberOfDetails);
		history = new ArrayList<Delay>();
		completeHistory = new ArrayList<Integer>();
	}
	
	/**
	 * Add the latest detailed delay at the back of the history queue
	 * and remove at the front if the queue gets too long.  
	 */
	@Override
	public void shift() {
		history.add(latestDelay);
		if (history.size() > numberOfDetails) {
			completeHistory.add(history.remove(0).getDelay());
		}
	}

	/**
	 * Returns an unmodifiable list of the complete history.
	 */
	@Override
	public List<Integer> getCompleteHistory() {
		ArrayList<Integer> complete = new ArrayList<>();
		for (Delay d : history) {
			complete.add(d.getDelay());
		}
		for (Integer i : completeHistory) {
			complete.add(i);
		}
		Collections.sort(complete);
		return Collections.unmodifiableList(complete);
	}
	
	/**
	 * Receives the newest delay and adds this to the history.
	 * 
	 * @param newDelay
	 * 		The delay to be added.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the given delay is null.
	 */
	public void addNewDelay(Delay newDelay) {
		if (newDelay == null) {
			throw new IllegalArgumentException();
		}
		if (newDelay.getDelay() != 0) {
			latestDelay = newDelay;
			shift();
		}
	}
	
	/**
	 * Returns an unmodifiable list of the detailed history.
	 */
	public List<Delay> getHistory() {
		return Collections.unmodifiableList(history);
	}

}
