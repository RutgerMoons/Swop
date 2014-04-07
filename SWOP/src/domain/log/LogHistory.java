package domain.log;

import java.util.List;



public abstract class LogHistory {

	protected final int numberOfDays;
	
	public LogHistory(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	/**
	 * add latest completed day at the back and remove  
	 */
	public abstract void shift();
	
	public abstract List<?> getCompleteHistory();
	
	
	
	
}
