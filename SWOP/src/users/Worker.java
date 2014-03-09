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
	
	public WorkBench getCurrentWorkBench() {
		return currentWorkBench;
	}
	
	public void setCurrentWorkBench(WorkBench currentWorkBench) {
		this.currentWorkBench = currentWorkBench;
	}

}
