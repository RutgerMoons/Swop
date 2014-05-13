package domain.log;

import java.util.List;

/**
 * This class provides abstract functions necessary for every logger of history.
 *
 */
public abstract class LogHistory {

	protected final int numberOfDetails;
	
	/**
	 * Constructs a LogHistory object with a degree of detail
	 * 
	 * @param 	numberOfDetails
	 * 				the amount of detailed information this object should keep track off
	 */
	public LogHistory(int numberOfDetails) {
		this.numberOfDetails = numberOfDetails;
	}
	
	/**
	 * Add the latest detailed information at the back of the history queue
	 * and remove at the front if the queue gets too long.  
	 */
	public abstract void shift();
	
	/**
	 * Returns an unmodifiable list of the history.
	 */
	public abstract List<?> getCompleteHistory();
	
	
	
	
}
