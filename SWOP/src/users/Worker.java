package users;

import assembly.WorkBench;


/**
 * Represents a Worker that can work at a WorkBench and complete actions to construct a car. 
 *
 */
public class Worker extends User {

	private WorkBench currentWorkBench;
	
	public Worker(String name) {
		super(name);
	}
	
	/**
	 * Get the WorkBench the Worker is currently assigned to.
	 * @return
	 * 			A WorkBench.
	 */
	public WorkBench getCurrentWorkBench() {
		return currentWorkBench;
	}
	
	/**
	 * Assign a Worker to a WorkBench.
	 * @param currentWorkBench
	 * 			The specific WorkBench.
	 */
	public void setCurrentWorkBench(WorkBench currentWorkBench) {
		this.currentWorkBench = currentWorkBench;
	}


}
