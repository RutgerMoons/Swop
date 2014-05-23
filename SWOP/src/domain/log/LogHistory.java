package domain.log;

import java.util.List;

/**
 * A class representing an abstract class who provides functions necessary for every implementation
 * of LogHistory.
 */
public abstract class LogHistory {

	protected final int numberOfDetails;
	
	/**
	 * Constructs a LogHistory object with a given degree of detail.
	 * 
	 * @param 	numberOfDetails
	 * 			The amount of detailed information this object should keep track off
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when numberOfDetails is smaller than 1
	 */
	public LogHistory(int numberOfDetails) {
		if( numberOfDetails <1 ){
			throw new IllegalArgumentException();
		}
		this.numberOfDetails = numberOfDetails;
	}
	
	/**
	 * Add the latest detailed information at the back of the history queue
	 * and remove at the front if the queue gets too long.  
	 */
	public abstract void shift();
	
	/**
	 * Returns a list of the history.
	 */
	public abstract List<?> getCompleteHistory();
}
