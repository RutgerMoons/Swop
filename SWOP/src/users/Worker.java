package users;

import assembly.WorkBench;

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
